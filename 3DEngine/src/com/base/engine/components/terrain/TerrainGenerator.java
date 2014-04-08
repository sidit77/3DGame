package com.base.engine.components.terrain;

public interface TerrainGenerator {

    public float getHeight(float x, float z, float res);

    public String getName();

}
