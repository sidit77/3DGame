package com.base.engine.rendering;

import com.base.engine.core.math.Vector2f;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {
	public static void createWindow(int width, int height, String title){
		Display.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
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
