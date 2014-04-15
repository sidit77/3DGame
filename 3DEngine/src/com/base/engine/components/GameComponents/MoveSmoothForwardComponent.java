package com.base.engine.components.GameComponents;

import com.base.engine.core.szenegraph.GameComponent;
import com.base.engine.core.math.Vector3f;

public class MoveSmoothForwardComponent extends GameComponent {

    private float speed;
    private float near;

    public MoveSmoothForwardComponent(float speed, float near){
        this.speed = speed;
        this.near = near;
    }

    @Override
    public void update(float delta) {
        if(getRenderingEngine() != null) {

            Vector3f way = getRenderingEngine().getMainCamera().getTransform().getTransformedPos().sub(getTransform().getPos());
            if(way.length() > near){
                getTransform().setPos(getTransform().getPos().add(getTransform().getRot().getForward().normalized().mul((way.length()/10)*speed)));
            }

        }
    }

}
