package com.base.engine.rendering;

import com.base.engine.core.Util;
import org.lwjgl.opengl.*;
import org.pmw.tinylog.Logger;

import java.nio.ByteBuffer;

public class FrameBuffer {

    private int fbo;
    private int fbo_texture;
    private int rbo_depth;

    public FrameBuffer(int sizeX, int sizeY){

        ByteBuffer byteBuffer = Util.createByteBuffer(sizeX * sizeY * 4);
        byteBuffer.rewind();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        fbo_texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo_texture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, sizeX, sizeY, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        rbo_depth = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo_depth);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, sizeX, sizeY);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);

        fbo = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, rbo_depth);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, fbo_texture, 0);
        if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
            Logger.error("Could not create a fbo");
            System.exit(1);
        }
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

    }

    public void reshape(int sizeX, int sizeY){

        ByteBuffer byteBuffer = Util.createByteBuffer(sizeX*sizeY*4);
        byteBuffer.rewind();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo_texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, sizeX, sizeY, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo_depth);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, sizeX, sizeY);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
    }

    @Override
    protected void finalize(){
        GL30.glDeleteRenderbuffers(rbo_depth);
        GL11.glDeleteTextures(fbo_texture);
        GL30.glDeleteFramebuffers(fbo);
    }

    public void bind(){
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
    }

    public void unbind(){
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
    }

    public int getTexture() {
        return fbo_texture;
    }
}
