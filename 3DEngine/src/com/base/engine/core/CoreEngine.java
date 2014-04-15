package com.base.engine.core;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.pmw.tinylog.Logger;

public class CoreEngine{

    private String WINDOW_TITLE;
    private boolean isRunning;
	private Game game;
	private RenderingEngine renderingEngine;
	private int width;
	private int height;
    private boolean vsync;
    private boolean resizeable;
    private int framerate;
    private int samples;

    private long lastFrame;
    private long lastFPS;
    private int fps;

    private boolean wireframe;
    private boolean fullscreen;
	
	public CoreEngine(int width, int height, int framerate, int samples, boolean vsync, boolean resizeable, Game game){

		this.isRunning = false;
		this.game = game;
		this.width = width;
		this.height = height;
        this.framerate = framerate;
        this.resizeable = resizeable;
        this.vsync = vsync;
        this.samples = samples;
		game.setEngine(this);
	}

	public void createWindow(String title){
        WINDOW_TITLE = title;
		Window.createWindow(width, height, samples, title);
        Window.setResizeable(resizeable);
        Window.setVSync(vsync);
		this.renderingEngine = new RenderingEngine();
        Logger.info("Your OpenGL-version is OpenGL " + renderingEngine.getOpenGLVersion() + " (minimum required is 3.3)");
        Logger.info("");
        Logger.info("Creating window with properties:");
        Logger.info("  -width: " + width);
        Logger.info("  -height: " + height);
        Logger.info("  -resizeable: " + resizeable);
        Logger.info("  -vsync enabled: " + vsync);
        Logger.info("  -max. framerate: " + framerate);
        Logger.info("");
	}

	public void start(){
		if(isRunning)
			return;
		
		run();
	}
	
	public void stop(){
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	private void run(){
		isRunning = true;
        Input.init();
		game.init();

		while(isRunning && !Window.isCloseRequested()){
            updateFPS();
            int delta = getDelta();

            if(Input.getKeyDown(Input.KEY_F1)){
                renderingEngine.setWireframe(!wireframe);
                Logger.info("Wireframe mode was set to " + !wireframe);
                wireframe = !wireframe;
            }
            if(Input.getKeyDown(Input.KEY_F2)){
                Window.setFullscreen(!fullscreen);
                Logger.info("Fullscreen mode was set to " + !fullscreen);
                GL11.glViewport(0,0,Window.getWidth(), Window.getHeight());
                renderingEngine.getMainCamera().setAspect((float) Window.getWidth() / (float) Window.getHeight());
                fullscreen = !fullscreen;
            }
			game.input((float)delta/1000);
			Input.update();
			game.update((float)delta/1000);
			game.render(renderingEngine);
            Window.sync(framerate);
			Window.render();

		}
		
		cleanUp();
	}

	private void cleanUp(){
		Window.dispose();
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}

    private long getTime(){
        return (Sys.getTime() * 1000)/Sys.getTimerResolution();
    }

    private void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Window.setTitle(WINDOW_TITLE + "  (FPS: " + fps +")");
            fps = 0;
            lastFPS = getTime();
        }
        fps++;
    }

    private int getDelta(){
        long currentTime = getTime();
        int delta = (int)(currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }
}
