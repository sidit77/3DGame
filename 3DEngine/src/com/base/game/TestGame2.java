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

public class TestGame2 extends Game{
    Material floorMaterial = new Material();

    public void init(){
        getRootObject().addChild(new GameObject().addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)));

        GameObject lights  = new GameObject();

        GameObject dlo = new GameObject();
        DirectionalLight dl = new DirectionalLight(new Vector3f(1,1,1), 0.7f);
        dlo.addComponent(dl);
        dl.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-20)));
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
        floorMaterial.addVector3f("diffuseColor", new Vector3f(1,1,1));
        floorMaterial.addFloat("transparency", 1f);

        Material modelMaterial = new Material();
        //modelMaterial.addTexture("diffuse", new Texture("bedrock.png"));
        //modelMaterial.addTexture("normal", new Texture("bedrock_normal.png"));
        modelMaterial.addFloat("specularIntensity", 1);
        modelMaterial.addFloat("specularPower", 8);
        modelMaterial.addVector3f("diffuseColor", new Vector3f(1,1,0));
        modelMaterial.addFloat("transparency", 0.4f);

        GameObject model = new GameObject();
        Mesh modelMesh = new Mesh("monkey3.obj");
        MeshRenderer meshRenderer = new MeshRenderer(modelMesh, modelMaterial);
        model.addComponent(meshRenderer);
        model.addComponent(new LookAtComponent());
        model.addComponent(new MoveSmoothForwardComponent(0.07f, 5f));
        model.getTransform().setPos(new Vector3f(1,-2,2));
        getRootObject().addChild(model);

        GameObject terrain = new GameObject();
        Terrain t = new Terrain(60, 5, floorMaterial, new TestGenerator(), new TestGenerator());
        terrain.addComponent(t);
        terrain.getTransform().setPos(new Vector3f(-30, -4, -30));
        getRootObject().addChild(terrain);

        getRootObject().addChild(lights);

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
        if(Input.getKeyDown(Input.KEY_6)){
            floorMaterial.addTexture("normal", new Texture("diamondblock_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("diamondblock.png"));
        }
        if(Input.getKeyDown(Input.KEY_7)){
            floorMaterial.addTexture("normal", new Texture("lapis_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("lapis.png"));
        }
        if(Input.getKeyDown(Input.KEY_8)){
            floorMaterial.addTexture("normal", new Texture("netherrack_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("netherrack.png"));
        }
        if(Input.getKeyDown(Input.KEY_9)){
            floorMaterial.addTexture("normal", new Texture("quartzblockchiseled2_normal.png"));
            floorMaterial.addTexture("diffuse", new Texture("quartzblockchiseled2.png"));
        }
    }
}