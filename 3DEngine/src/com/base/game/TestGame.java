package com.base.game;

import com.base.engine.components.*;
import com.base.engine.components.terrain.Terrain;
import com.base.engine.components.terrain.generators.HeightbasedColorGenerator;
import com.base.engine.components.terrain.generators.SimplexGenerator;
import com.base.engine.components.terrain.generators.TestGenerator;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;

public class TestGame extends Game{

	public void init(){

        getRootObject().addChild(new GameObject().addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)).addComponent(new FreeLook(0.1f)).addComponent(new FreeMove(5)));

        GameObject lights  = new GameObject();

        GameObject dlo = new GameObject();
        DirectionalLight dl = new DirectionalLight(new Vector3f(1,1,1), 0.7f);
        dlo.addComponent(dl);
        dl.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
        lights.addChild(dlo);


        Material modelMaterial = new Material();
        //modelMaterial.addTexture("diffuse", new Texture("bedrock.png"));
        //modelMaterial.addTexture("normal", new Texture("bedrock_normal.png"));
        modelMaterial.addFloat("specularIntensity", 1);
        modelMaterial.addFloat("specularPower", 8);
        modelMaterial.addVector3f("diffuseColor", new Vector3f(1,1,0));
        modelMaterial.addFloat("transparency", 0.6f);

        Material waterMaterial = new Material();
        waterMaterial.addTexture("diffuse", new Texture("water.png"));
        //modelMaterial.addTexture("normal", new Texture("bedrock_normal.png"));
        waterMaterial.addFloat("specularIntensity", 1);
        waterMaterial.addFloat("specularPower", 8);
        waterMaterial.addVector3f("diffuseColor", new Vector3f(1,1,1));
        waterMaterial.addFloat("transparency", 0.7f);

        GameObject model = new GameObject();
        Mesh modelMesh = new Mesh("monkey3.obj");
        MeshRenderer meshRenderer = new MeshRenderer(modelMesh, modelMaterial);
        model.addComponent(meshRenderer);
        model.addComponent(new LookAtComponent());
        model.addComponent(new MoveSmoothForwardComponent(0.07f, 8f));
        model.getTransform().setPos(new Vector3f(1,-2,2));
        getRootObject().addChild(model);


        Material floorMaterial = new Material();
            floorMaterial.addTexture("diffuse", new Texture("sand.png"));
            floorMaterial.addTexture("diffuse2", new Texture("grass.png"));
            floorMaterial.addTexture("diffuse3", new Texture("stone.png"));
            floorMaterial.addTexture("diffuse4", new Texture("snow.png"));

            floorMaterial.addTexture("normal", new Texture("sand_normal.png"));
            floorMaterial.addTexture("normal2", new Texture("grass_normal.png"));
            floorMaterial.addTexture("normal3", new Texture("stone_normal.png"));
            floorMaterial.addTexture("normal4", new Texture("snow_normal.png"));

            floorMaterial.addVector3f("diffuseColor", new Vector3f(1,1,1));
            floorMaterial.addVector3f("diffuse2Color", new Vector3f(0,1,0));
            floorMaterial.addVector3f("diffuse3Color", new Vector3f(1,1,1));
            floorMaterial.addVector3f("diffuse4Color", new Vector3f(1,1,1));

            floorMaterial.addFloat("specularIntensity", 1);
            floorMaterial.addFloat("specularPower", 8);
            floorMaterial.addFloat("transparency", 1f);

        GameObject terrain = new GameObject();
        Terrain t = new Terrain(512, 1, floorMaterial, new SimplexGenerator(1000, 0.35, (int) System.currentTimeMillis(), 150), new HeightbasedColorGenerator());//("height2.jpg", 8, 1, floorMaterial);//
        terrain.addComponent(t);
        terrain.getTransform().setPos(new Vector3f(-256, -20, -256));

        getRootObject().addChild(terrain);


        GameObject water = new GameObject();
        Terrain w = new Terrain(576, 64, waterMaterial, new TestGenerator(), new TestGenerator());//("height2.jpg", 8, 1, floorMaterial);//
        water.addComponent(w);
        water.getTransform().setPos(new Vector3f(-256, -45, -256));

        getRootObject().addChild(water);

        getRootObject().addChild(lights);
	}
}
