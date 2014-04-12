package com.base.engine.components;

import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class LookAtComponent extends GameComponent {
    RenderingEngine renderingEngine;

    @Override
    public void update(float delta) {
        if(renderingEngine != null) {
            Quaternion newRot = getTransform().getLookAtRotation(renderingEngine.getMainCamera().getTransform().getTransformedPos(),new Vector3f(0,1,0));
            //getTransform().getRot().getUp());

            getTransform().setRot(getTransform().getRot().nlerp(newRot, delta * 5.0f, true));
            //getTransform().setRot(getTransform().getRot().slerp(newRot, delta * 5.0f, true));

            //System.out.println(getTransform().getRot());

        }
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine)
    {
        this.renderingEngine = renderingEngine;
    }
}
