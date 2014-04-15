package com.base.engine.components.GameComponents;

import com.base.engine.core.szenegraph.GameComponent;
import com.base.engine.core.Input;
import com.base.engine.core.math.Vector3f;

public class FreeMove extends GameComponent {

    private float speed;
    private int forwardKey;
    private int backKey;
    private int leftKey;
    private int rightKey;
    private int sprintKey;

    public FreeMove(float speed)
    {
        this(speed, Input.KEY_W, Input.KEY_S, Input.KEY_A, Input.KEY_D, Input.KEY_LSHIFT);
    }

    public FreeMove(float speed, int forwardKey, int backKey, int leftKey, int rightKey, int sprintKey)
    {
        this.speed = speed;
        this.forwardKey = forwardKey;
        this.backKey = backKey;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.sprintKey = sprintKey;
    }

    @Override
    public void input(float delta)
    {
        float movAmt = speed * delta;

        if(Input.getKey(Input.KEY_W)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getForward(), movAmt * 10);
            } else {
                move(getTransform().getRot().getForward(), movAmt);
            }
        }

        if(Input.getKey(Input.KEY_S)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getForward(), -movAmt * 10);
            } else {
                move(getTransform().getRot().getForward(), -movAmt);
            }
        }

        if(Input.getKey(Input.KEY_A)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getLeft(), movAmt * 10);
            } else {
                move(getTransform().getRot().getLeft(), movAmt);
            }
        }

        if(Input.getKey(Input.KEY_D)) {
            if (Input.getKey(Input.KEY_LSHIFT)) {
                move(getTransform().getRot().getRight(), movAmt * 10);
            } else {
                move(getTransform().getRot().getRight(), movAmt);
            }
        }
    }

    private void move(Vector3f dir, float amt)
    {
        getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
    }
}
