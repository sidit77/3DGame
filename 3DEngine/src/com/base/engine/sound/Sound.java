package com.base.engine.sound;

import com.base.engine.sound.recourceManagement.SoundResource;
import org.lwjgl.openal.AL10;
import org.pmw.tinylog.Logger;

import java.util.HashMap;

public class Sound {

    private static HashMap<String, SoundResource> loadedSounds = new HashMap<String, SoundResource>();
    private SoundResource resource;
    private String fileName;

    public Sound(String fileName){
        this.fileName = fileName;
        SoundResource oldResource = loadedSounds.get(fileName);

        if(oldResource != null){
            resource = oldResource;
            resource.addReference();
        }else{
            resource = loadSound(fileName);
            loadedSounds.put(fileName, resource);
        }
    }

    private SoundResource loadSound(String fileName) {
        Logger.debug("SOUNDS: Starting to load sound " + fileName);
        SoundResource soundResource = new SoundResource();
        OggData oggFile = new OggData("res/sounds/"+fileName);
        AL10.alBufferData(soundResource.getId(), oggFile.format, oggFile.data, oggFile.rate);
        oggFile.dispose();
        Logger.debug("SOUNDS: Finished to load sound");
        return soundResource;
    }

    @Override
    protected void finalize(){
        if(resource.removeReference() && !fileName.isEmpty()){
            loadedSounds.remove(fileName);
        }
    }

    public int getID(){
        return resource.getId();
    }

}
