package com.base.engine.components.GameObjects.terrain;

import com.base.engine.core.math.Vector3f;

public interface ColorGenerator {
    public Vector3f getTextureSplatting(float sx, float sy, float sz);
}
