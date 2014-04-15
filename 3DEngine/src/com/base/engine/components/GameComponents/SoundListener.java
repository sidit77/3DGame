package com.base.engine.components.GameComponents;

import com.base.engine.core.math.Vector3f;
import com.base.engine.core.szenegraph.GameComponent;
import org.lwjgl.openal.AL10;

public class SoundListener extends GameComponent{

    private Vector3f lastPos;
    private Vector3f velocity;

    public SoundListener(){
        lastPos = new Vector3f(0,0,0);
        velocity = new Vector3f(0,0,0);

        AL10.alListener3f(AL10.AL_POSITION, lastPos.getX(), lastPos.getY(), lastPos.getZ());
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public void update(float delta) {
        velocity = getTransform().getPos().sub(lastPos);
        lastPos = getTransform().getPos();

        AL10.alListener3f(AL10.AL_POSITION, lastPos.getX(), lastPos.getY(), lastPos.getZ());
        AL10.alListener3f(AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());

    }
}
