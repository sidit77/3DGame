package com.base.engine.core.szenegraph;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.RenderingEngine;
import com.base.engine.rendering.Shader;

import java.util.ArrayList;

public class GameObject{

	private ArrayList<GameComponent> components;
	private Transform transform;
	private CoreEngine engine;

	public GameObject(){
		components = new ArrayList<GameComponent>();
		transform = new Transform();
		engine = null;
	}

	public GameObject addComponent(GameComponent component){
        components.add(component);
		component.setParent(this);

		return this;
	}

    public GameObject removeComponent(GameComponent component){
        components.remove(component);
        component.setParent(null);

        return this;
    }

    public void input(float delta){
        transform.update();
        for(GameComponent component : components)
            component.input(delta);
    }

    public void update(float delta){
        for(GameComponent component : components)
            component.update(delta);
    }

    public void render(Shader shader, RenderingEngine renderingEngine, float transparencyLevel){ }

	public Transform getTransform(){
		return transform;
	}

    public CoreEngine getEngine() {
        return engine;
    }

    public void setEngine(CoreEngine engine){
		if(this.engine != engine){
			this.engine = engine;
            addToEngine(engine);

			for(GameComponent component : components)
				component.addToEngine(engine);

		}
	}

    public void addToEngine(CoreEngine engine){

    }
}
