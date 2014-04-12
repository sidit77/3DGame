package com.base.engine.components.terrain.generators;

import com.base.engine.components.terrain.TerrainGenerator;
import org.pmw.tinylog.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class HeightMapGenerator implements TerrainGenerator{

    private BufferedImage image;
    private float sen;
    private int size = 0;

    public HeightMapGenerator(String fileName, float sen){
        this.sen = sen;

        try {
            this.image = ImageIO.read(new File("res/maps/" + fileName));

            if(image.getHeight() != image.getWidth()){
                Logger.error("Error, only quadratic heighmaps are allowed");
                System.exit(1);
            }else{
                size = image.getHeight();
            }
        }catch (Exception e){
            Logger.error(e);
            System.exit(1);
        }

    }

    @Override
    public float getHeight(float x, float z, float res) {
        return (new Color(image.getRGB(Math.round(x), Math.round(z))).getRed() / sen);
    }

    @Override
    public String getName() {
        return "Heightmap-Generator";
    }

    public int getSize() {
        return size;
    }
}
