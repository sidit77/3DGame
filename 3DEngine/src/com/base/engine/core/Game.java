package com.base.engine.core;

import com.base.engine.components.GameComponents.Node;

public abstract class Game{

	private Node root = new Node();

	public void init() {

    }

	public void input(float delta){
		getRootNode().input(delta);
	}

	public void update(float delta){
		getRootNode().update(delta);
	}

	public void render(RenderingEngine renderingEngine){
        renderingEngine.render(getRootNode());
	}

	public Node getRootNode(){
		return root;
	}

	public void setEngine(CoreEngine engine) {
        getRootNode().setEngine(engine);
    }
}
