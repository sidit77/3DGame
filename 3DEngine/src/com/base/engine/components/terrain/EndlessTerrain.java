package com.base.engine.components.terrain;

import com.base.engine.components.GameComponent;
import com.base.engine.components.terrain.Terrain;
import com.base.engine.components.terrain.TerrainGenerator;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;

public class EndlessTerrain extends GameComponent{

    TerrainGenerator generator;
    int size;
    float res;
    Material material;
    Vector3f playerPos = new Vector3f(0,0,0);
    ArrayList<Terrain> terrains = new ArrayList<Terrain>();

    public EndlessTerrain(int size, float res, Material material, TerrainGenerator generator){
        this.generator = generator;
        this.size = size;
        this.res = res;
        this.material = material;
    }

    public void create(){

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine) {

    }

    public void setPlayerPos(Vector3f playerPos) {
        this.playerPos = playerPos;
    }
}
