package com.base.game;

import com.base.engine.core.CoreEngine;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.LoggingLevel;

public class Main{

	public static void main(String[] args){
        Configurator.defaultConfig().level(LoggingLevel.DEBUG).formatPattern("{level}: {message}").activate();
		CoreEngine engine = new CoreEngine(1280, 720, 60, false, false, new TestGame());
		engine.createWindow("3D Game Engine");
		engine.start();
	}

}
