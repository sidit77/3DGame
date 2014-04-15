package com.base.engine.components.GameObjects;

import com.base.engine.core.szenegraph.GameObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.core.RenderingEngine;
import com.base.engine.rendering.Shader;

public class MeshRenderer extends GameObject{

	private Mesh mesh;
	private Material material;

	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine, float transparencyLevel)
	{
        if(material.getFloat("transparency") == transparencyLevel) {
            shader.bind();
            shader.updateUniforms(getTransform(), material, renderingEngine);
            mesh.draw();
        }
	}

}
