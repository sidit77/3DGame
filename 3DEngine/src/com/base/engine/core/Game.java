package com.base.engine.core;

import com.base.engine.rendering.RenderingEngine;

public abstract class Game{

	private GameObject root = new GameObject();

	public void init() {

    }

	public void input(float delta){
		getRootObject().input(delta);
	}

	public void update(float delta){
		getRootObject().update(delta);
	}

	public void render(RenderingEngine renderingEngine){
        renderingEngine.render(getRootObject());
	}

	public GameObject getRootObject(){
		return root;
	}

	public void setEngine(CoreEngine engine) {
        getRootObject().setEngine(engine);
    }
}
