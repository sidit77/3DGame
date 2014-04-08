package com.base.engine.components.terrain;

import com.base.engine.components.GameComponent;
import com.base.engine.components.terrain.generators.HeightMapGenerator;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import org.pmw.tinylog.Logger;

public class Terrain extends GameComponent{

    private Mesh mesh;
    private Material material;

    public Terrain(float x, float z, String fileName, float sen, float res, Material material){

        Logger.debug("TERRAIN: Stating to create terrain on " + x + "/" + z + " from heightmap " + fileName);

        if(res < 1.0f){
            Logger.error("Error, on heightmaps are only resolutions of 1 and above allowed");
            System.exit(1);
        }

        HeightMapGenerator gen = new HeightMapGenerator(fileName, sen);

        mesh = new Mesh(TerrainUtil.createVertices(x, z, gen.getSize(), res, gen), TerrainUtil.createIndices(gen.getSize(), res), true);
        this.material = material;

        Logger.debug("TERRAIN: Creation finished");
    }

    public Terrain(String fileName, float sen, float res, Material material){
        this(0, 0, fileName, sen, res, material);
    }

    public Terrain(int size, float res, Material material, TerrainGenerator generator){
       this(0, 0, size, res, material, generator);
    }

    public Terrain(float x, float z, int size, float res, Material material, TerrainGenerator generator){
        Logger.debug("TERRAIN: Stating to create terrain on " + x + "/" + z + " with the generator: " + generator.getName());
        mesh = new Mesh(TerrainUtil.createVertices(x ,z, size, res, generator), TerrainUtil.createIndices(size, res), true);
        this.material = material;
        Logger.debug("TERRAIN: Creation finished");
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine) {
        shader.bind();
        shader.updateUniforms(getTransform(), material, renderingEngine);
        mesh.draw();
    }
}
