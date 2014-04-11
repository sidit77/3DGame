package com.base.engine.components.terrain.generators;

import com.base.engine.components.terrain.ColorGenerator;
import com.base.engine.components.terrain.TerrainGenerator;
import com.base.engine.core.math.Vector3f;

public class TestGenerator implements TerrainGenerator, ColorGenerator{

    @Override
    public float getHeight(float x, float z, float res) {
        return 0;
    }

    @Override
    public String getName() {
        return "Test-Generator";
    }

    @Override
    public Vector3f getTextureSplatting(float sx, float sy, float sz) {
        return new Vector3f(0,0,0);
    }
}
