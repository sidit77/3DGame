package com.base.engine.components.terrain.generators;

import com.base.engine.components.terrain.TerrainGenerator;

public class TestGenerator implements TerrainGenerator{

    @Override
    public float getHeight(float x, float z, float res) {
        return 0;
    }

    @Override
    public String getName() {
        return "Test-Generator";
    }

}
