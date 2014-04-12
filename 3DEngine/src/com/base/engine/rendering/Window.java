package com.base.engine.rendering;

import com.base.engine.core.math.Vector2f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.pmw.tinylog.Logger;

import java.awt.*;

public class Window {

    private static int width, height;

	public static void createWindow(int width, int height, int samples, String title){
		Display.setTitle(title);
        Window.width = width;
        Window.height = height;
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withSamples(samples));
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
        }
	}

    public static void setFullscreen(boolean enabled){

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

        if ((Display.getDisplayMode().getWidth() == dimension.width) &&
                (Display.getDisplayMode().getHeight() == dimension.height) &&
                (Display.isFullscreen() == enabled)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (enabled) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == dimension.width) && (current.getHeight() == dimension.height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                Logger.warn("Failed to find value mode: " + width + "x" + height + " fs=" + enabled);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(enabled);

        } catch (LWJGLException e) {
            Logger.error("Unable to setup mode " + width + "x" + height + " fullscreen=" + enabled + e);
        }

    }

	public static void render(){
		Display.update();
	}
	
	public static void dispose(){
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static boolean isCloseRequested(){
		return Display.isCloseRequested();
	}
	
	public static int getWidth(){
		return Display.getDisplayMode().getWidth();
	}
	
	public static int getHeight(){
		return Display.getDisplayMode().getHeight();
	}
	
	public static String getTitle(){
		return Display.getTitle();
	}

	public Vector2f getCenter(){
		return new Vector2f(getWidth()/2, getHeight()/2);
	}

    public static void sync(int fps){
        Display.sync(fps);
    }

    public static void setTitle(String s) {
        Display.setTitle(s);
    }

    public static void setResizeable(boolean enabled){
        Display.setResizable(enabled);
    }

    public static void setVSync(boolean enabled){
        Display.setVSyncEnabled(enabled);
    }
}
