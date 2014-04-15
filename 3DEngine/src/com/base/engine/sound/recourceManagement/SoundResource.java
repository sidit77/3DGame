package com.base.engine.sound.recourceManagement;

import org.lwjgl.openal.AL10;

public class SoundResource {
    private int id;
    private int refCount;

    public SoundResource(){
        this.id = AL10.alGenBuffers();
        this.refCount = 1;
    }

    @Override
    protected void finalize(){
        AL10.alDeleteBuffers(id);
    }

    public void addReference(){
        refCount++;
    }

    public boolean removeReference(){
        refCount--;
        return refCount == 0;
    }

    public int getId() {
        return id;
    }
}
