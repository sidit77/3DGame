package com.base.engine.components.terrain;

import com.base.engine.core.Util;
import com.base.engine.core.math.Vector2f;
import com.base.engine.core.math.Vector3f;
import com.base.engine.rendering.Vertex;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;

public class TerrainUtil {

    public static Vertex[] createVertices(float x, float z, int size, float res, TerrainGenerator generator, ColorGenerator generator2){

        Logger.debug(">VERTICES: Started to create the vertices");

        ArrayList<Vertex> verticesList = new ArrayList<Vertex>();

        for (float sx = 0; sx < size; sx += res) {
            for (float sz = 0; sz < size; sz += res) {

                float y = generator.getHeight(x + sx, z + sz, res);

                verticesList.add(new Vertex(new Vector3f(x + sx, y, z + sz), new Vector2f(sx, sz), new Vector3f(0,0,0), new Vector3f(0,0,0), generator2.getTextureSplatting(x + sx, y, z + sz)));
            }
        }

        Vertex[] vertices = new Vertex[verticesList.size()];
        verticesList.toArray(vertices);

        Logger.debug(">VERTICES: Finished to create the vertices");
        Logger.debug(">VERTICES: Total amount: " + vertices.length + " vertices");

        return vertices;
    }

    public static int[] createIndices(int size, float res){

        Logger.debug(">INDICES: Started to create the indices");

        ArrayList<Integer> indicesList = new ArrayList<Integer>();
        int verticesRes = Math.round((float)size/res)-1;

        for (int i = 0; i <= verticesRes-1; i++){
            for (int j = 0; j <= verticesRes-1; j++){
                int t = j + (i * (verticesRes+1));

                indicesList.add(t);
                indicesList.add(t+1);
                indicesList.add(t + verticesRes + 1);

                indicesList.add(t + verticesRes + 1);
                indicesList.add(t + 1);
                indicesList.add((t + verticesRes) + 1 + 1);
            }
        }

        Integer[] indices = new Integer[indicesList.size()];
        indicesList.toArray(indices);

        Logger.debug(">INDICES: Finished to create Indices");
        Logger.debug(">INDICES: Total amount: " + indices.length/3 + " polygons");

        return Util.toIntArray(indices);
    }

}
