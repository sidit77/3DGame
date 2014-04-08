package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Input;
import com.base.engine.core.math.Matrix4f;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Window;

public class Camera extends GameComponent
{
	public static final Vector3f yAxis = new Vector3f(0,1,0);

	private Matrix4f projection;

	public Camera(float fov, float aspect, float zNear, float zFar)
	{
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}

	public Matrix4f getViewProjection()
	{
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

	boolean mouseLocked = false;

	@Override
	public void input(float delta)
	{
		float sensitivity = 0.2f;
		float movAmt = (float)(10 * delta);
		
		if(Input.getKey(Input.KEY_ESCAPE)){
			Input.setCursor(true);
			mouseLocked = false;
		}
		if(Input.getMouseDown(0)){
			Input.setCursor(false);
			mouseLocked = true;
		}
		
		if(Input.getKey(Input.KEY_W)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getForward(), movAmt * 5);
            } else {
                move(getTransform().getRot().getForward(), movAmt);
            }
        }

		if(Input.getKey(Input.KEY_S)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getForward(), -movAmt * 5);
            } else {
                move(getTransform().getRot().getForward(), -movAmt);
            }
        }

		if(Input.getKey(Input.KEY_A)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getLeft(), movAmt * 5);
            } else {
                move(getTransform().getRot().getLeft(), movAmt);
            }
        }

		if(Input.getKey(Input.KEY_D)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getRight(), movAmt * 5);
            } else {
                move(getTransform().getRot().getRight(), movAmt);
            }
        }
		
		if(mouseLocked){
			Vector2f deltaPos = Input.getRelativMousePosition();
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if(rotY)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotX) {
                if(!(getTransform().getRot().getForward().getY() <= -0.99f) && !(getTransform().getRot().getForward().getY() >= 0.99f)){
                    getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));
                }else
                //TODO Make it better;
                if(getTransform().getRot().getForward().getY() <= -0.99f && deltaPos.getY() > 0){
                    getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-deltaPos.getY()));
                }else if(getTransform().getRot().getForward().getY() >= 0.99f && deltaPos.getY() < 0){
                    System.out.println("dd");
                    getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-deltaPos.getY()));
                }

            }
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
		}
	}

	public void move(Vector3f dir, float amt)
	{
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}
}
