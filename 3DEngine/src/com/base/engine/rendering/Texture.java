package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.rendering.resourceManagement.TextureResource;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class Texture{

	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	private TextureResource resource;
	private String fileName;
	
	public Texture(String fileName){
		this.fileName = fileName;
		TextureResource oldResource = loadedTextures.get(fileName);

		if(oldResource != null){
			resource = oldResource;
			resource.addReference();
		}else{
			resource = loadTexture(fileName);
			loadedTextures.put(fileName, resource);
		}
	}

    public Texture(){

        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        for(int x = 0; x < 32; x++){
            for(int y = 0; y < 32; y++){
                image.setRGB(x, y, new Color(1f,1f,1f).getRGB());
            }
        }

        TextureResource oldResource = loadedTextures.get("dummy");

        if(oldResource != null){
            resource = oldResource;
            resource.addReference();
        }else{
            resource = loadTexture(image);
            loadedTextures.put("dummy", resource);
        }

    }

	@Override
	protected void finalize(){
		if(resource.removeReference() && !fileName.isEmpty()){
			loadedTextures.remove(fileName);
		}
	}

	public void bind(){
		bind(0);
	}

	public void bind(int samplerSlot){
		assert(samplerSlot >= 0 && samplerSlot <= 31);
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getId());
	}
	
	public int getID(){
		return resource.getId();
	}
	
	private static TextureResource loadTexture(String fileName){

        Logger.debug("TEXTURE: Starting to load texture from file " + fileName);

		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		try{
			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			boolean hasAlpha = image.getColorModel().hasAlpha();

			for(int y = 0; y < image.getHeight(); y++){
				for(int x = 0; x < image.getWidth(); x++){
					int pixel = pixels[y * image.getWidth() + x];

					buffer.put((byte)((pixel >> 16) & 0xFF));
					buffer.put((byte)((pixel >> 8) & 0xFF));
					buffer.put((byte)((pixel) & 0xFF));
					if(hasAlpha)
						buffer.put((byte)((pixel >> 24) & 0xFF));
					else
						buffer.put((byte)(0xFF));
				}
			}

			buffer.flip();

			TextureResource resource = new TextureResource();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getId());

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            Logger.debug("TEXTURE: Finished to load texture from file");

			return resource;
		}catch(Exception e){
			Logger.error(e);
			System.exit(1);
		}

		return null;
	}

    private static TextureResource loadTexture(BufferedImage image){

        Logger.debug("TEXTURE: Starting to load texture from a BufferedImage");

        try{
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

            ByteBuffer buffer = Util.createByteBuffer(image.getHeight() * image.getWidth() * 4);
            boolean hasAlpha = image.getColorModel().hasAlpha();

            for(int y = 0; y < image.getHeight(); y++){
                for(int x = 0; x < image.getWidth(); x++){
                    int pixel = pixels[y * image.getWidth() + x];

                    buffer.put((byte)((pixel >> 16) & 0xFF));
                    buffer.put((byte)((pixel >> 8) & 0xFF));
                    buffer.put((byte)((pixel) & 0xFF));
                    if(hasAlpha)
                        buffer.put((byte)((pixel >> 24) & 0xFF));
                    else
                        buffer.put((byte)(0xFF));
                }
            }

            buffer.flip();

            TextureResource resource = new TextureResource();
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, resource.getId());

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            Logger.debug("TEXTURE: Finished to load texture from a BufferedImage");

            return resource;
        }catch(Exception e){
            Logger.error(e);
            System.exit(1);
        }

        return null;
    }

    public static int getTextureSlot(String name){
        if(name.equalsIgnoreCase("diffuse")){
            return 0;
        }else if(name.equalsIgnoreCase("normal")){
            return 1;
        } else if(name.equalsIgnoreCase("bump")){
            return 2;
        }
        return 0;
    }

}
