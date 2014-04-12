package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Window;
import org.pmw.tinylog.Logger;

public class FreeLook extends GameComponent{

    private static final Vector3f yAxis = new Vector3f(0,1,0);

    private boolean mouseLocked = false;
    private float sensitivity;
    private int unlockMouseKey;

    public FreeLook(float sensitivity){
        this(sensitivity, Input.KEY_ESCAPE);
    }

    public FreeLook(float sensitivity, int unlockMouseKey){
        this.sensitivity = sensitivity;
        this.unlockMouseKey = unlockMouseKey;
    }

    @Override
    public void input(float delta){
        Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);

        if(Input.getKey(unlockMouseKey)){
            Input.setCursor(true);
            mouseLocked = false;
        }
        if(Input.getMouseDown(0)){
            Input.setMousePosition(centerPosition);
            Input.setCursor(false);
            mouseLocked = true;
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
                        Logger.debug("CAMERA: you turned to fast!");
                        getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-deltaPos.getY()));
                    }

            }

            if(rotY || rotX)
                Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
        }
    }
}
