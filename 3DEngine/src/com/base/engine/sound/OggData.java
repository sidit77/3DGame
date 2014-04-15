package com.base.engine.sound;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OggData {

    private int convsize = 4096 * 4;
    private byte[] convbuffer = new byte[convsize];


    public ByteBuffer data;
    public int rate;
    public int channels;
    public int format;

    public OggData(String fileName){
        this(getFileInputStream(fileName));
    }

    public OggData(InputStream input){
        ByteArrayOutputStream dataout = new ByteArrayOutputStream();

        SyncState oy = new SyncState();
        StreamState os = new StreamState();
        Page og = new Page();
        Packet op = new Packet();

        Info vi = new Info();
        Comment vc = new Comment();
        DspState vd = new DspState();
        Block vb = new Block(vd);

        byte[] buffer;
        int bytes = 0;

        boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);

        oy.init();

        while (true) {
            int eos = 0;

            int index = oy.buffer(4096);

            buffer = oy.data;
            try {
                bytes = input.read(buffer, index, 4096);
            } catch (Exception e) {
                Logger.error("SOUNDS: Failure reading in vorbis");
                System.exit(1);
            }
            oy.wrote(bytes);

            if (oy.pageout(og) != 1) {
                if (bytes < 4096)
                    break;

                Logger.error("SOUNDS: Input does not appear to be an Ogg bitstream.");
                System.exit(1);
            }

            os.init(og.serialno());

            vi.init();
            vc.init();
            if (os.pagein(og) < 0) {
                Logger.error("SOUNDS: Error reading first page of Ogg bitstream data.");
                System.exit(1);
            }

            if (os.packetout(op) != 1) {
                Logger.error("SOUNDS: Error reading initial header packet.");
                System.exit(11);
            }

            if (vi.synthesis_headerin(vc, op) < 0) {
                Logger.error("SOUNDS: This Ogg bitstream does not contain Vorbis audio data.");
                System.exit(1);
            }

            int i = 0;
            while (i < 2) {
                while (i < 2) {

                    int result = oy.pageout(og);
                    if (result == 0)
                        break;
                    if (result == 1) {
                        os.pagein(og);
                        while (i < 2) {
                            result = os.packetout(op);
                            if (result == 0)
                                break;
                            if (result == -1) {
                                Logger.error("SOUNDS: Corrupt secondary header.  Exiting.");
                                System.exit(1);
                            }
                            vi.synthesis_headerin(vc, op);
                            i++;
                        }
                    }
                }
                index = oy.buffer(4096);
                buffer = oy.data;
                try {
                    bytes = input.read(buffer, index, 4096);
                } catch (Exception e) {
                    System.out.println("Failed to read Vorbis: ");
                    System.out.println(e);
                    System.exit(0);
                }
                if (bytes == 0 && i < 2) {
                    Logger.error("SOUNDS: End of file before finding all Vorbis headers!");
                    System.exit(1);
                }
                oy.wrote(bytes);
            }

            convsize = 4096 / vi.channels;

            vd.synthesis_init(vi);
            vb.init(vd);

            float[][][] _pcm = new float[1][][];
            int[] _index = new int[vi.channels];
            while (eos == 0) {
                while (eos == 0) {

                    int result = oy.pageout(og);
                    if (result == 0)
                        break;
                    if (result == -1) {
                        Logger.error("SOUNDS: Corrupt or missing data");
                    } else {
                        os.pagein(og);
                        while (true) {
                            result = os.packetout(op);

                            if (result == 0)
                                break;
                            if (result == -1) {
                            } else {
                                int samples;
                                if (vb.synthesis(op) == 0) {
                                    vd.synthesis_blockin(vb);
                                }

                                while ((samples = vd.synthesis_pcmout(_pcm,
                                        _index)) > 0) {
                                    float[][] pcm = _pcm[0];
                                    int bout = (samples < convsize ? samples
                                            : convsize);

                                    for (i = 0; i < vi.channels; i++) {
                                        int ptr = i * 2;
                                        int mono = _index[i];
                                        for (int j = 0; j < bout; j++) {
                                            int val = (int) (pcm[i][mono + j] * 32767.);
                                            if (val > 32767) {
                                                val = 32767;
                                            }
                                            if (val < -32768) {
                                                val = -32768;
                                            }
                                            if (val < 0)
                                                val = val | 0x8000;

                                            if (bigEndian) {
                                                convbuffer[ptr] = (byte) (val >>> 8);
                                                convbuffer[ptr + 1] = (byte) (val);
                                            } else {
                                                convbuffer[ptr] = (byte) (val);
                                                convbuffer[ptr + 1] = (byte) (val >>> 8);
                                            }
                                            ptr += 2 * (vi.channels);
                                        }
                                    }

                                    dataout.write(convbuffer, 0, 2 * vi.channels * bout);

                                    vd.synthesis_read(bout);
                                }
                            }
                        }
                        if (og.eos() != 0)
                            eos = 1;
                    }
                }
                if (eos == 0) {
                    index = oy.buffer(4096);
                    if (index >= 0) {
                        buffer = oy.data;
                        try {
                            bytes = input.read(buffer, index, 4096);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        bytes = 0;
                    }
                    oy.wrote(bytes);
                    if (bytes == 0)
                        eos = 1;
                }
            }

            os.clear();

            vb.clear();
            vd.clear();
            vi.clear();
        }

        if(vi.channels == 1){
            format = AL10.AL_FORMAT_MONO16;
        }else if(vi.channels == 2){
            format = AL10.AL_FORMAT_STEREO16;
        }else{
            Logger.warn("SOUND: Too many channels");
        }

        oy.clear();
        channels = vi.channels;
        rate = vi.rate;

        byte[] Data = dataout.toByteArray();
        data = ByteBuffer.allocateDirect(Data.length);
        data.put(Data);
        data.rewind();

    }

    public void dispose(){
        data.clear();
    }

    private static FileInputStream getFileInputStream(String fileName){
        try{
            return new FileInputStream(fileName);
        }catch(IOException e){
            Logger.error("SOUNDS: Could not find " + fileName);
            return null;
        }
    }

}
