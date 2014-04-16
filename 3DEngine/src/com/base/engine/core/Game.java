package com.base.engine.core;

import com.base.engine.components.GameComponents.Node;
import com.base.engine.components.GameObjects.PostProgressEffect;

import java.util.ArrayList;

public abstract class Game{

	private Node root = new Node();
    private ArrayList<PostProgressEffect> postProgressEffects = new ArrayList<PostProgressEffect>();

	public void init() {

    }

	public void input(float delta){
		getRootNode().input(delta);
	}

	public void update(float delta){
		getRootNode().update(delta);
	}

	public void render(RenderingEngine renderingEngine){
        renderingEngine.render(getRootNode(), postProgressEffects);
	}

	public Node getRootNode(){
		return root;
	}

	public void setEngine(CoreEngine engine) {
        getRootNode().setEngine(engine);
    }

    public void addPostProgressEffect(PostProgressEffect effect){
        postProgressEffects.add(effect);
    }

    public void removePostProgressEffect(PostProgressEffect effect){
        postProgressEffects.remove(effect);
    }

}
