package com.base.engine.components;

import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class MoveForwardComponent extends GameComponent {

    private RenderingEngine renderingEngine;
    private float speed;
    private float near;

    public MoveForwardComponent(float speed, float near){
        this.speed = speed;
        this.near = near;
    }

    @Override
    public void update(float delta) {
        if(renderingEngine != null) {

            Vector3f way = renderingEngine.getMainCamera().getTransform().getTransformedPos().sub(getTransform().getPos());
            if(way.length() > near){
                getTransform().setPos(getTransform().getPos().add(getTransform().getRot().getForward().normalized().mul(speed)));
            }

        }
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
    }
}
