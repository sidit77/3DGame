package com.base.engine.components.GameComponents;

import com.base.engine.core.math.Vector3f;
import com.base.engine.core.szenegraph.GameComponent;
import com.base.engine.sound.Sound;
import org.lwjgl.openal.AL10;

public class SoundSource extends GameComponent{

    private int source;

    private boolean looping = false;
    private float gain = 1.0f;
    private float pitch = 1.0f;

    private Vector3f lastPos;
    private Vector3f velocity;

    public SoundSource(Sound sound) {
        lastPos = new Vector3f(0,0,0);
        velocity = new Vector3f(0,0,0);

        source = AL10.alGenSources();

        AL10.alSourcei(source, AL10.AL_BUFFER, sound.getID());
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
        AL10.alSourcef(source, AL10.AL_PITCH, pitch);
        AL10.alSourcef(source, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, 5f);
        AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, 120f);
        AL10.alSource3f(source, AL10.AL_POSITION, lastPos.getX(), lastPos.getY(), lastPos.getZ());
        AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    public void update(float delta) {
        velocity = getTransform().getPos().sub(lastPos);
        lastPos = getTransform().getPos();

        AL10.alSource3f(source, AL10.AL_POSITION, lastPos.getX(), lastPos.getY(), lastPos.getZ());
        AL10.alSource3f(source, AL10.AL_VELOCITY, velocity.getX(), velocity.getY(), velocity.getZ());
    }

    @Override
    protected void finalize(){
        AL10.alDeleteSources(source);
    }

    public SoundSource setGain(float gain){
        this.gain = gain;
        AL10.alSourcef(source, AL10.AL_GAIN, gain);
        return this;
    }

    public SoundSource setPitch(float pitch){
        this.pitch = pitch;
        AL10.alSourcef(source, AL10.AL_PITCH, pitch);
        return this;
    }

    public SoundSource play(){
        AL10.alSourcePlay(source);
        return this;
    }

    public SoundSource stop(){
        AL10.alSourceStop(source);
        return this;
    }

    public SoundSource pause(){
        AL10.alSourcePause(source);
        return this;
    }

    public SoundSource setLooping(boolean enabled){
        looping = enabled;
        AL10.alSourcef(source, AL10.AL_LOOPING, looping ? AL10.AL_TRUE : AL10.AL_FALSE);
        return this;
    }

    public SoundSource setReferenceDistance(float distance){
        AL10.alSourcef(source, AL10.AL_REFERENCE_DISTANCE, distance);
        return this;
    }

    public SoundSource setMaxDistance(float distance){
        AL10.alSourcef(source, AL10.AL_MAX_DISTANCE, distance);
        return this;
    }

}
