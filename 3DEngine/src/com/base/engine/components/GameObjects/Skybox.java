package com.base.engine.components.GameObjects;

import com.base.engine.core.RenderingEngine;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.core.szenegraph.GameObject;
import com.base.engine.rendering.*;

public class Skybox extends GameObject{

    private RenderingEngine renderingEngine;

    private Shader skyboxShader;
    private Material skyboxMaterial;

    private Mesh skyboxMeshForward;
    private Mesh skyboxMeshBackward;
    private Mesh skyboxMeshBottom;
    private Mesh skyboxMeshTop;
    private Mesh skyboxMeshLeft;
    private Mesh skyboxMeshRight;

    private final float x0 = 0f/4f;
    private final float x1 = 1f/4f;
    private final float x2 = 2f/4f;
    private final float x3 = 3f/4f;
    private final float x4 = 4f/4f;

    private final float y0 = 0f/3f;
    private final float y1 = 1f/3f;
    private final float y2 = 2f/3f;
    private final float y3 = 3f/3f;

    public Skybox(float distance, float brightness, Texture texture) {

        skyboxShader = new Shader("special/SkyboxVertexShader.glsl", "special/SkyboxFragmentShader.glsl");
        skyboxMaterial = new Material();
        skyboxMaterial.addTexture("diffuse", texture);
        skyboxMaterial.addFloat("brightness", brightness);

        int[] indicesCW = new int[]{
                1,3,2,
                0,1,2
        };

        int[] indicesCCW = new int[]{
                3,1,2,
                1,0,2
        };

        Vertex[] vertices0 = new Vertex[]{
                new Vertex(new Vector3f(-distance,-distance, distance), new Vector2f(x1,y2)),
                new Vertex(new Vector3f(-distance, distance, distance), new Vector2f(x1,y1)),
                new Vertex(new Vector3f( distance,-distance, distance), new Vector2f(x2,y2)),
                new Vertex(new Vector3f( distance, distance, distance), new Vector2f(x2,y1))
        };

        Vertex[] vertices1 = new Vertex[]{
                new Vertex(new Vector3f(-distance,-distance,-distance), new Vector2f(x4,y2)),
                new Vertex(new Vector3f(-distance, distance,-distance), new Vector2f(x4,y1)),
                new Vertex(new Vector3f( distance,-distance,-distance), new Vector2f(x3,y2)),
                new Vertex(new Vector3f( distance, distance,-distance), new Vector2f(x3,y1))
        };

        Vertex[] vertices2 = new Vertex[]{
                new Vertex(new Vector3f(-distance,-distance,-distance), new Vector2f(x1,y3)),
                new Vertex(new Vector3f(-distance,-distance, distance), new Vector2f(x1,y2)),
                new Vertex(new Vector3f( distance,-distance,-distance), new Vector2f(x2,y3)),
                new Vertex(new Vector3f( distance,-distance, distance), new Vector2f(x2,y2))
        };

        Vertex[] vertices3 = new Vertex[]{
                new Vertex(new Vector3f(-distance, distance,-distance), new Vector2f(x1,y0)),
                new Vertex(new Vector3f(-distance, distance, distance), new Vector2f(x1,y1)),
                new Vertex(new Vector3f( distance, distance,-distance), new Vector2f(x2,y0)),
                new Vertex(new Vector3f( distance, distance, distance), new Vector2f(x2,y1))
        };

        Vertex[] vertices4 = new Vertex[]{
                new Vertex(new Vector3f( distance,-distance,-distance), new Vector2f(x3,y2)),
                new Vertex(new Vector3f( distance,-distance, distance), new Vector2f(x2,y2)),
                new Vertex(new Vector3f( distance, distance,-distance), new Vector2f(x3,y1)),
                new Vertex(new Vector3f( distance, distance, distance), new Vector2f(x2,y1))
        };

        Vertex[] vertices5 = new Vertex[]{
                new Vertex(new Vector3f(-distance,-distance,-distance), new Vector2f(x0,y2)),//00->01
                new Vertex(new Vector3f(-distance,-distance, distance), new Vector2f(x1,y2)),//01->11
                new Vertex(new Vector3f(-distance, distance,-distance), new Vector2f(x0,y1)),//10->00
                new Vertex(new Vector3f(-distance, distance, distance), new Vector2f(x1,y1))//11->10
        };

        skyboxMeshForward = new Mesh(vertices0, indicesCW, false);
        skyboxMeshBackward = new Mesh(vertices1, indicesCCW, false);
        skyboxMeshBottom = new Mesh(vertices2, indicesCW, false);
        skyboxMeshTop = new Mesh(vertices3, indicesCCW, false);
        skyboxMeshLeft = new Mesh(vertices4, indicesCW, false);
        skyboxMeshRight = new Mesh(vertices5, indicesCCW, false);

    }

    @Override
    public void update(float delta) {
        if(renderingEngine != null) {
            getTransform().setPos(renderingEngine.getMainCamera().getTransform().getPos());
        }
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine, float transparencyLevel) {
         if(transparencyLevel == 1.0f){
             skyboxShader.bind();
             skyboxShader.updateUniforms(getTransform(), skyboxMaterial, renderingEngine);
             skyboxMeshForward.draw();
             skyboxMeshBackward.draw();
             skyboxMeshBottom.draw();
             skyboxMeshTop.draw();
             skyboxMeshLeft.draw();
             skyboxMeshRight.draw();
             this.renderingEngine = renderingEngine;
         }
    }

}
