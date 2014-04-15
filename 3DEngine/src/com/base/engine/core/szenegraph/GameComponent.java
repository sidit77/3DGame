package com.base.engine.core.szenegraph;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.RenderingEngine;

public abstract class GameComponent
{
	private GameObject parent;

    public void input(float delta) {}
	public void update(float delta) {}

	public void setParent(GameObject parent)
	{
		this.parent = parent;
	}

	public Transform getTransform()
	{
		return parent.getTransform();
	}

	public void addToEngine(CoreEngine engine) {}

    public RenderingEngine getRenderingEngine(){
        return parent.getEngine().getRenderingEngine();
    }

}

