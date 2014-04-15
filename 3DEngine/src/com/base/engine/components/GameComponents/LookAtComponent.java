package com.base.engine.components.GameComponents;

import com.base.engine.core.szenegraph.GameComponent;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;

public class LookAtComponent extends GameComponent {

    @Override
    public void update(float delta) {
        if(getRenderingEngine() != null) {
            Quaternion newRot = getTransform().getLookAtRotation(getRenderingEngine().getMainCamera().getTransform().getTransformedPos(),new Vector3f(0,1,0));

            getTransform().setRot(getTransform().getRot().nlerp(newRot, delta * 5.0f, true));

        }
    }

}
