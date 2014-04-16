package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;
import org.lwjgl.opengl.GL20;
import org.pmw.tinylog.Logger;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

public class PostProgressShader {

    private int id;

    public PostProgressShader(String vertexShader, String fragmentShader) {
        id = glCreateProgram();

        if(id == 0){
            Logger.error("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }

        String vertexShaderText = loadShader(vertexShader);
        String fragmentShaderText = loadShader(fragmentShader);

        addVertexShader(vertexShaderText);
        addFragmentShader(fragmentShaderText);

        compileShader();
    }

    private void addVertexShader(String text){
        addProgram(text, GL_VERTEX_SHADER);
    }

    private void addGeometryShader(String text){
        addProgram(text, GL_GEOMETRY_SHADER);
    }

    private void addFragmentShader(String text){
        addProgram(text, GL_FRAGMENT_SHADER);
    }

    private void compileShader(){
        glLinkProgram(id);

        if(glGetProgrami(id, GL_LINK_STATUS) == 0)
        {
            Logger.error(glGetProgramInfoLog(id, 1024));
            System.exit(1);
        }

        glValidateProgram(id);

        if(glGetProgrami(id, GL_VALIDATE_STATUS) == 0)
        {
            Logger.error(glGetProgramInfoLog(id, 1024));
            System.exit(1);
        }
    }

    private void addProgram(String text, int type){
        int shader = glCreateShader(type);

        if(shader == 0)
        {
            Logger.error("Shader creation failed: Could not find valid memory location when adding shader");
            System.exit(1);
        }

        glShaderSource(shader, text);
        glCompileShader(shader);

        if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0)
        {
            Logger.error(glGetShaderInfoLog(shader, 1024));
            System.exit(1);
        }

        glAttachShader(id, shader);
    }

    private static String loadShader(String fileName){
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        final String INCLUDE_DIRECTIVE = "#include";

        try
        {
            shaderReader = new BufferedReader(new FileReader("res/shaders/" + fileName));
            String line;

            while((line = shaderReader.readLine()) != null)
            {
                if(line.startsWith(INCLUDE_DIRECTIVE))
                {
                    shaderSource.append(loadShader(line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1)));
                }
                else
                    shaderSource.append(line).append("\n");
            }

            shaderReader.close();
        }
        catch(Exception e)
        {
            Logger.error(e);
            System.exit(1);
        }


        return shaderSource.toString();
    }

    public void setUniformi(String uniformName, int value){
        glUniform1i(glGetUniformLocation(id, uniformName), value);
    }

    public void setUniformf(String uniformName, float value){
        glUniform1f(glGetUniformLocation(id, uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value){
        glUniform3f(glGetUniformLocation(id, uniformName), value.getX(), value.getY(), value.getZ());
    }

    public void setUniform(String uniformName, Matrix4f value){
        glUniformMatrix4(glGetUniformLocation(id, uniformName), true, Util.createFlippedBuffer(value));
    }

    public void bind(){
        GL20.glUseProgram(id);
    }

    @Override
    protected void finalize() {
        glDeleteProgram(id);
    }
}
