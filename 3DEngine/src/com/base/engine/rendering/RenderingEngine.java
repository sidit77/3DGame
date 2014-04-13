package com.base.engine.rendering;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.core.GameObject;
import com.base.engine.core.Transform;
import com.base.engine.core.math.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;

public class RenderingEngine{

	private ArrayList<BaseLight> lights = new ArrayList<BaseLight>();
	private BaseLight activeLight;

	private Shader forwardAmbient;
	private Camera mainCamera;
    private Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f);

	public RenderingEngine(){
		super();

		forwardAmbient = new Shader("forward/AmbientLightVertexShader.glsl", "forward/AmbientLightFragmentShader.glsl");

		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL32.GL_DEPTH_CLAMP);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType){
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
	}

    public void setWireframe(boolean enabled){
        if(!enabled){
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }else{
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
    }

	public void render(GameObject object){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        for(int i = 10; i > 0; i -= 1) {

            float trans = (float)i/10;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            object.renderAll(forwardAmbient, this, trans);


            //GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDepthMask(false);
            GL11.glDepthFunc(GL11.GL_EQUAL);

            for (BaseLight light : lights) {
                activeLight = light;
                object.renderAll(light.getShader(), this, trans);
            }

            GL11.glDepthFunc(GL11.GL_LESS);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_BLEND);
        }

	}

	public static String getOpenGLVersion(){
        String[] version = GL11.glGetString(GL11.GL_VERSION).split("\\.");
        return version[0]+"."+version[1];
	}

	public void addLight(BaseLight light)
	{
		lights.add(light);
	}

	public void addCamera(Camera camera)
	{
		mainCamera = camera;
	}

	public BaseLight getActiveLight()
	{
		return activeLight;
	}

	public Camera getMainCamera()
	{
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera)
	{
		this.mainCamera = mainCamera;
	}

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }
}
