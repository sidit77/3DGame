package com.base.game;

import com.base.engine.components.Camera;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.terrain.Terrain;
import com.base.engine.components.terrain.generators.SimplexGenerator;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;

public class TestGame extends Game{

	public void init(){

        getRootObject().addChild(new GameObject().addComponent(new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)));

        GameObject lights  = new GameObject();

        GameObject dlo = new GameObject();
        DirectionalLight dl = new DirectionalLight(new Vector3f(1,1,1), 0.7f);
        dlo.addComponent(dl);
        dl.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
        lights.addChild(dlo);

        Material floorMaterial = new Material();
            floorMaterial.addTexture("diffuse", new Texture("gravel.png"));
            floorMaterial.addTexture("normal", new Texture("gravel_normal.png"));
            floorMaterial.addFloat("specularIntensity", 1);
            floorMaterial.addFloat("specularPower", 8);
            floorMaterial.addVector3f("basisColor", new Vector3f(1,1,1));

        GameObject terrain = new GameObject();
        Terrain t = new Terrain(512, 1, floorMaterial, new SimplexGenerator(1000, 0.35, (int) System.currentTimeMillis(), 150));//("height2.jpg", 8, 1, floorMaterial);//
        terrain.addComponent(t);
        terrain.getTransform().setPos(new Vector3f(-128, -20, -128));

        getRootObject().addChild(terrain);

        getRootObject().addChild(lights);
	}
}
