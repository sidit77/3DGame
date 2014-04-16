package com.base.engine.components.GameObjects;

import com.base.engine.rendering.FrameBuffer;
import com.base.engine.rendering.PostProgressShader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;

public class PostProgressEffect{

    private int fbo_vbo;
    private PostProgressShader shader;
    private long startTime;

    public PostProgressEffect(String fragmentShader) {
        shader = new PostProgressShader("post/PostProgressVertexShader.glsl", fragmentShader);
        startTime = System.currentTimeMillis();

        float[] vertices = new float[]{
                -1,-1,
                -1, 1,
                1,-1,

                1, 1,
                1,-1,
                -1, 1
        };
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        for(int i = 0; i < vertices.length; i++){
            buffer.put(vertices[i]);
        }
        buffer.flip();
        fbo_vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, fbo_vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

    }

    public void renderFrameBuffer(FrameBuffer frameBuffer){
        shader.bind();
        shader.setUniformi("fbo_texture", 0);
        shader.setUniformf("offset", (float)((System.currentTimeMillis() - startTime) / 1000.0 * 2 * (float)Math.PI * 0.75f));
        shader.setUniformf("saturation", 0.5f);
        shader.setUniformf("InverseAmount", 1f);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBuffer.getTexture());

        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, fbo_vbo);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6);

        GL20.glDisableVertexAttribArray(0);
    }

    @Override
    protected void finalize() {
        GL15.glDeleteBuffers(fbo_vbo);
    }
}
