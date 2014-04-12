package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector3f;

public class Camera extends GameComponent{
	public static final Vector3f yAxis = new Vector3f(0,1,0);

	private Matrix4f projection;
    private float fov, aspect, zNear, zFar;

	public Camera(float fov, float aspect, float zNear, float zFar){
        this.fov = fov;
        this.aspect = aspect;
        this.zNear = zNear;
        this.zFar = zFar;
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}

    public void setAspect(float aspect){
        this.aspect = aspect;
        this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
    }

	public Matrix4f getViewProjection(){
		Matrix4f cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
		Vector3f cameraPos = getTransform().getTransformedPos().mul(-1);

		Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	@Override
	public void addToEngine(CoreEngine engine)
	{
		engine.getRenderingEngine().addCamera(this);
	}

}
