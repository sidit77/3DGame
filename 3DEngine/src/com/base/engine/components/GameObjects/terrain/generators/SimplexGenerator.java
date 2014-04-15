package com.base.engine.components.GameObjects.terrain.generators;

import com.base.engine.components.GameObjects.terrain.TerrainGenerator;
import com.base.engine.components.GameObjects.terrain.generators.simplex.SimplexNoise;

public class SimplexGenerator implements TerrainGenerator{

    private SimplexNoise noise;
    private float sen;

    public SimplexGenerator(int largestFeature,double persistence, int seed, float sen){
        noise = new SimplexNoise(largestFeature, persistence, seed);
        this.sen = sen;
    }

    @Override
    public float getHeight(float x, float z, float res) {
        return (float) noise.getNoise(Math.round(x*res),Math.round(z*res))*sen;
    }

    @Override
    public String getName() {
        return "Simplex-Noise-Generator";
    }
}
