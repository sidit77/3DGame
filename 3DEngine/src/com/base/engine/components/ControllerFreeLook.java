package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.math.Vector3f;
import org.pmw.tinylog.Logger;

public class ControllerFreeLook extends GameComponent{

    private static final Vector3f yAxis = new Vector3f(0,1,0);

    boolean calib0, calib1;
    private float sensitivity;
    private int LYAxis;
    private int LXAxis;

    public ControllerFreeLook(float sensitivity){
        this(sensitivity, 6, 7);
    }

    public ControllerFreeLook(float sensitivity, int LYAxis, int LXAxis) {
        this.sensitivity = sensitivity;
        this.LYAxis = LYAxis;
        this.LXAxis = LXAxis;
    }

    @Override
    public void input(float delta) {
        if(Input.isControllerConnected()){

            if(calib0 && calib1) {

                float rotY = -Input.getAxis(LYAxis);
                float rotX = Input.getAxis(LXAxis);

                boolean isRotY = rotY != 0;
                boolean isRotX = rotX != 0;

                if (isRotY)
                    getTransform().rotate(yAxis, (float) Math.toRadians(rotX * sensitivity));
                if (isRotX) {
                    if (!(getTransform().getRot().getForward().getY() <= -0.99f) && !(getTransform().getRot().getForward().getY() >= 0.99f)) {
                        getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-rotY * sensitivity));
                    } else
                        //TODO Make it better;
                        if (getTransform().getRot().getForward().getY() <= -0.99f && rotY > 0) {
                            getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-rotY));
                        } else if (getTransform().getRot().getForward().getY() >= 0.99f && rotY < 0) {
                            Logger.debug("CAMERA: you turned to fast!");
                            getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(-rotY));
                        }

                }
            }else{
                if(Math.abs(Input.getAxis(LXAxis))<1.0f && !calib0){
                    calib0 = true;
                    Logger.info("CONTROLLER: Calibrated axis "+ LXAxis);
                }
                if(Math.abs(Input.getAxis(LYAxis))<1.0f && !calib1){
                    calib1 = true;
                    Logger.info("CONTROLLER: Calibrated axis "+ LYAxis);
                }
            }
        }
    }
}
