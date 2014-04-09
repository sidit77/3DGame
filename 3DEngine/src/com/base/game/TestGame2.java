package com.base.game;

import com.base.engine.components.*;
import com.base.engine.components.terrain.Terrain;
import com.base.engine.components.terrain.generators.TestGenerator;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.*;

public class TestGame2 extends Game
{
    GameObject root = new GameObject();
    Material floorMaterial = new Material();

    public void init()
    {
        root.addChild(new GameObject().addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)));

        GameObject lights  = new GameObject();

        GameObject dlo = new GameObject();
        DirectionalLight dl = new DirectionalLight(new Vector3f(1,1,1), 0.7f);
        dlo.addComponent(dl);
        dl.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-20)));
        //dl.getTransform().setPos(new Vector3f(0,10,0));
        lights.addChild(dlo);

        GameObject plo = new GameObject();
        PointLight pl = new PointLight(new Vector3f(0,1,1), 0.9f, new Attenuation(0,0,1));
        plo.addComponent(pl);
        pl.getTransform().setPos(new Vector3f(0, -3.5f, 0));
        lights.addChild(plo);

        GameObject slo = new GameObject();
        SpotLight sl = new SpotLight(new Vector3f(1,1,1), 0.7f, new Attenuation(0,0,0.1f), 0.7f);
        slo.addComponent(sl);
        sl.getTransform().setPos(new Vector3f(2, -3.5f, 0));
        sl.getTransform().setRot(new Quaternion(new Vector3f(0,1,0), (float)Math.toRadians(180)));
        lights.addChild(slo);

        floorMaterial.addTexture("normal", new Texture("brick_normal.png"));
        floorMaterial.addTexture("diffuse", new Texture("brick.png"));
        floorMaterial.addFloat("specularIntensity", 1);
        floorMaterial.addFloat("specularPower", 5);
        floorMaterial.addVector3f("basisColor", new Vector3f(1,1,1));

        Material modelMaterial = new Material();
        modelMaterial.addTexture("diffuse", new Texture("bedrock.png"));
        modelMaterial.addFloat("specularIntensity", 1);
        modelMaterial.addFloat("specularPower", 8);
        modelMaterial.addVector3f("basisColor", new Vector3f(1,1,1));

        GameObject model = new GameObject();
        Mesh modelMesh = new Mesh("monkey3.obj");
        MeshRenderer meshRenderer = new MeshRenderer(modelMesh, modelMaterial);
        model.addComponent(meshRenderer);
        model.getTransform().setPos(new Vector3f(1,-2,2));
        root.addChild(model);

        GameObject terrain = new GameObject();
        Terrain t = new Terrain(60, 10, floorMaterial, new TestGenerator());//("height2.jpg", 8, 1, floorMaterial);//
        terrain.addComponent(t);
        terrain.getTransform().setPos(new Vector3f(-30, -4, -30));
        root.addChild(terrain);

        root.addChild(lights);
        addObject(root);
    }

    @Override
    public void input(float delta) {
        super.input(delta);
        if(Input.getKeyDown(Input.KEY_1)){
            floorMaterial.addTexture("normal", new Texture("brick_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("brick.png"));
        }
        if(Input.getKeyDown(Input.KEY_2)){
            floorMaterial.addTexture("normal", new Texture("clay_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("clay.png"));
        }
        if(Input.getKeyDown(Input.KEY_3)){
            floorMaterial.addTexture("normal", new Texture("stonebrick_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("stonebrick.png"));
        }
        if(Input.getKeyDown(Input.KEY_4)){
            floorMaterial.addTexture("normal", new Texture("grass_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("grass2.png"));
        }
        if(Input.getKeyDown(Input.KEY_5)){
            floorMaterial.addTexture("normal", new Texture("netherbrick_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("netherbrick.png"));
        }
    }
}