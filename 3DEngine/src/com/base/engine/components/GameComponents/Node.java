package com.base.engine.components.GameComponents;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.szenegraph.GameObject;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;

public class Node extends GameObject {

    private ArrayList<GameObject> children;

    public Node(){
        children = new ArrayList<GameObject>();
    }

    public void addChild(GameObject child){
        children.add(child);
        child.setEngine(getEngine());
        child.getTransform().setParent(getTransform());
    }

    public void removeChild(GameObject child){
        children.remove(child);
        //child.setEngine(getEngine());
        child.getTransform().setParent(null);
    }

    @Override
    public void input(float delta) {
        for(GameObject child : children)
            child.input(delta);

        super.input(delta);
    }

    @Override
    public void update(float delta) {

        for(GameObject child : children)
            child.update(delta);

        super.update(delta);
    }

    @Override
    public void render(Shader shader, RenderingEngine renderingEngine, float transparencyLevel) {

        for(GameObject child : children){
            child.render(shader, renderingEngine, transparencyLevel);
        }

        super.render(shader, renderingEngine, transparencyLevel);
    }

    @Override
    public void setEngine(CoreEngine engine) {
        if(this.getEngine() != engine){

            super.setEngine(engine);

            for(GameObject child : children)
                child.setEngine(engine);

        }

    }
}
