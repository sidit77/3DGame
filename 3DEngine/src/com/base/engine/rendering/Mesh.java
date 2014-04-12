package com.base.engine.rendering;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.meshLoading.IndexedModel;
import com.base.engine.rendering.meshLoading.OBJModel;
import com.base.engine.rendering.resourceManagement.MeshResource;
import org.lwjgl.opengl.GL15;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Mesh{

	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	private MeshResource resource;
	private String fileName;
	
	public Mesh(String fileName){
		this.fileName = fileName;
		MeshResource oldResource = loadedModels.get(fileName);

		if(oldResource != null){
			resource = oldResource;
			resource.addReference();
		}else{
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}
	}
	
	public Mesh(Vertex[] vertices, int[] indices){
		this(vertices, indices, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals){
		fileName = "";
		addVertices(vertices, indices, calcNormals, calcNormals);
	}

	@Override
	protected void finalize(){
		if(resource.removeReference() && !fileName.isEmpty()){
			loadedModels.remove(fileName);
		}
	}
	
	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals, boolean calcTangents){
		if(calcNormals){
			calcNormals(vertices, indices);
		}

        if(calcTangents){
            calcTangents(vertices, indices);
        }

		resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		GL15.glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw(){
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);

        glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
        glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
        glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
        glVertexAttribPointer(4, 3, GL_FLOAT, false, Vertex.SIZE * 4, 44);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
        glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
	}
	
	private void calcNormals(Vertex[] vertices, int[] indices){

        Logger.trace("NORMALS: Started to calculate normals");

		for(int i = 0; i < indices.length; i += 3){
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
			
			Vector3f normal = v1.cross(v2).normalized();
			
			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].setNormal(vertices[i].getNormal().normalized());

        Logger.trace("NORMALS: Finished to calculate normals");
	}

    private void calcTangents(Vertex[] vertices, int[] indices){

        Logger.trace("NORMALS: Started to calculate tangents");

        for(int i = 0; i < indices.length; i += 3){
            Vertex v0 = vertices[indices[i]];
            Vertex v1 = vertices[indices[i+1]];
            Vertex v2 = vertices[indices[i+2]];

            Vector3f edge1 = v1.getPos().sub(v0.getPos());
            Vector3f edge2 = v2.getPos().sub(v0.getPos());

            float deltaU1 = v1.getTexCoord().getX() - v0.getTexCoord().getX();
            float deltaV1 = v1.getTexCoord().getY() - v0.getTexCoord().getY();
            float deltaU2 = v2.getTexCoord().getX() - v0.getTexCoord().getX();
            float deltaV2 = v2.getTexCoord().getY() - v0.getTexCoord().getY();

            float f = 1.0f / (deltaU1 * deltaV2 - deltaU2 * deltaV1);

            Vector3f tangent = new Vector3f(0,0,0);

            tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
            tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
            tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));

            v0.setTangent(v0.getTangent().add(tangent));
            v1.setTangent(v1.getTangent().add(tangent));
            v2.setTangent(v2.getTangent().add(tangent));

        }

        for(int i = 0; i < vertices.length; i++)
            vertices[i].setTangent(vertices[i].getTangent().normalized());

        Logger.trace("NORMALS: Finished to calculate tangents");

    }
	
	private Mesh loadMesh(String fileName){

        Logger.debug("MODEL: Starting to read model from file " + fileName);

		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if(!ext.equals("obj")){
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel("res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for(int i = 0; i < model.getPositions().size(); i++){
			vertices.add(new Vertex(model.getPositions().get(i),
					                model.getTexCoords().get(i),
					                model.getNormals().get(i),
                                    new Vector3f(0,0,0)
            ));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false, true);

        Logger.debug("MODEL: Finished to read model from file");

        return null;
	}
}
