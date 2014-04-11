package com.base.engine.components.terrain.generators;

import com.base.engine.components.terrain.ColorGenerator;
import com.base.engine.core.math.Vector3f;

public class HeightbasedColorGenerator implements ColorGenerator{
    
    @Override
    public Vector3f getTextureSplatting(float sx, float sy, float sz) {

        float zone1 = 0;
        float zone2 = 0;
        float zone3 = 0;

        if(sy > -20){
            if(sy < -18){
                zone1 = (sy + 20)/2;
            }else{
                zone1 = 1;
            }
        }

        if(sy > 13){
            if(sy < 15){
                zone2 = (sy -13)/2;
            }else{
                zone2 = 1;
            }
        }

        if(sy > 30){
            if(sy < 32){
                zone3 = (sy - 30)/2;
            }else{
                zone3 = 1;
            }
        }

        return new Vector3f(zone1, zone2, zone3);
    }
    
}
