package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.math.Vector3f;
import org.pmw.tinylog.Logger;

public class ControllerFreeMove extends GameComponent{

    private float speed;
    private int RYAxis;
    private int RXAxis;
    private int speedbutton;
    private boolean calib0, calib1;

    public ControllerFreeMove(float speed){
        this(speed, 3, 4, 7);
    }

    public ControllerFreeMove(float speed, int RYAxis, int RXAxis, int speedbutton){

        this.speed = speed;
        this.RYAxis = RYAxis;
        this.RXAxis = RXAxis;
        this.speedbutton = speedbutton;

    }

    @Override
    public void input(float delta) {
        if(Input.isControllerConnected()) {

            if(calib0 && calib1){

                float movAmt = speed * delta;

                float moveZ = -Input.getAxis(RYAxis);
                float moveX = -Input.getAxis(RXAxis);

                boolean isMoveZ = moveZ != 0;
                boolean isMoveX = moveX != 0;

                if (isMoveZ) {
                    if (Input.getButton(speedbutton)) {
                        move(getTransform().getRot().getForward(), movAmt * 10 * moveZ);
                    } else {
                        move(getTransform().getRot().getForward(), movAmt * moveZ);
                    }
                }

                if (isMoveX) {
                    if (Input.getButton(speedbutton)) {
                        move(getTransform().getRot().getLeft(), movAmt * 10 * moveX);
                    } else {
                        move(getTransform().getRot().getLeft(), movAmt * moveX);
                    }
                }

                if (Input.getKeyDown(Input.KEY_T)) {
                    getTransform().setPos(new Vector3f(0, 0, 0));
                }

            }else{

                if(Math.abs(Input.getAxis(RXAxis))<1.0f && !calib0){
                    calib0 = true;
                    Logger.info("CONTROLLER: Calibrated axis "+ RXAxis);
                }
                if(Math.abs(Input.getAxis(RYAxis))<1.0f && !calib1){
                    calib1 = true;
                    Logger.info("CONTROLLER: Calibrated axis "+ RYAxis);
                }
            }
        }
    }

    private void move(Vector3f dir, float amt)
    {
        getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
    }

}
