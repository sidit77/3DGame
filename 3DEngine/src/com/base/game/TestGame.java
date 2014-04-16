package com.base.game;

import com.base.engine.components.GameComponents.*;
import com.base.engine.components.GameObjects.Camera;
import com.base.engine.components.GameObjects.DirectionalLight;
import com.base.engine.components.GameObjects.MeshRenderer;
import com.base.engine.components.GameObjects.terrain.Terrain;
import com.base.engine.components.GameObjects.terrain.generators.HeightbasedColorGenerator;
import com.base.engine.components.GameObjects.terrain.generators.SimplexGenerator;
import com.base.engine.components.GameObjects.terrain.generators.TestGenerator;
import com.base.engine.core.Game;
import com.base.engine.core.Input;
import com.base.engine.core.Window;
import com.base.engine.core.math.Quaternion;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;
import com.base.engine.sound.Sound;

public class TestGame extends Game{

    private SoundSource monkey;
    private boolean playing = true;
    private Terrain water;
    private boolean isWater = true;
    private MoveSmoothForwardComponent move;
    private boolean isMoving = true;
    private MeshRenderer monkeyModel;

	public void init(){

        //addPostProgressEffect(new PostProgressEffect("post/Wave.glsl"));

        getRootNode().addChild(
                new Camera((float) Math.toRadians(70.0f), (float) Window.getWidth() / (float) Window.getHeight(), 0.01f, 1000.0f)
                        .addComponent(new FreeLook(0.1f))
                        .addComponent(new FreeMove(5))
                        .addComponent(new ControllerFreeLook(2))
                        .addComponent(new ControllerFreeMove(5))
                        .addComponent(new SoundListener())
        );

        DirectionalLight dl = new DirectionalLight(new Vector3f(1,1,1), 0.7f);
        dl.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float) Math.toRadians(-45)));
        getRootNode().addChild(dl);

        //getRootNode().addChild(new Skybox(575, 0.5f, new Texture("skybox/Above_The_Sea.png")));

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


        Material modelMaterial = new Material();
            modelMaterial.addFloat("specularIntensity", 1);
            modelMaterial.addFloat("specularPower", 8);
            modelMaterial.addVector3f("diffuseColor", new Vector3f(1,1,0));
            modelMaterial.addFloat("transparency", 0.6f);

        Material waterMaterial = new Material();
            waterMaterial.addTexture("diffuse", new Texture("water.png"));
            waterMaterial.addFloat("specularIntensity", 1);
            waterMaterial.addFloat("specularPower", 8);
            waterMaterial.addVector3f("diffuseColor", new Vector3f(1,1,1));
            waterMaterial.addFloat("transparency", 0.7f);


        monkey = new SoundSource(new Sound("haggle.ogg")).setLooping(true).setGain(0.3f);
        move = new MoveSmoothForwardComponent(0.07f, 8f);

        Mesh modelMesh = new Mesh("monkey3.obj");
        monkeyModel = new MeshRenderer(modelMesh, modelMaterial);
        monkeyModel.addComponent(new LookAtComponent());
        monkeyModel.addComponent(move);
        monkeyModel.addComponent(monkey.play());
        monkeyModel.getTransform().setPos(new Vector3f(1, -2, 2));
        getRootNode().addChild(monkeyModel);

        Terrain terrain = new Terrain(512, 1, floorMaterial, new SimplexGenerator(1000, 0.35, (int) System.currentTimeMillis(), 150), new HeightbasedColorGenerator());//("height2.jpg", 8, 1, floorMaterial);//
        terrain.getTransform().setPos(new Vector3f(-256, -20, -256));
        getRootNode().addChild(terrain);

        water = new Terrain(576, 64, waterMaterial, new TestGenerator(), new TestGenerator());//("height2.jpg", 8, 1, floorMaterial);//
        water.getTransform().setPos(new Vector3f(-256, -45, -256));
        getRootNode().addChild(water);

	}

    @Override
    public void input(float delta) {
        super.input(delta);
        if(Input.getKeyDown(Input.KEY_F3) || Input.getButtonDown(5)){
            if(playing){
                monkey.pause();
            }else{
                monkey.play();
            }
            playing = !playing;
        }
        if(Input.getKeyDown(Input.KEY_F4)){
            if(isWater){
                getRootNode().removeChild(water);
            }else{
                getRootNode().addChild(water);
            }
            isWater = !isWater;
        }
        if(Input.getKeyDown(Input.KEY_F5)){
            if(isMoving){
                monkeyModel.removeComponent(move);
            }else{
                monkeyModel.addComponent(move);
            }
            isMoving = !isMoving;
        }
    }
}
