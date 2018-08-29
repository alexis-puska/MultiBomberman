package com.mygdx.game.raspberry;

import com.badlogic.gdx.backends.jogamp.JoglNewtApplication;
import com.badlogic.gdx.backends.jogamp.JoglNewtApplicationConfiguration;
import com.mygdx.constante.Constante;
import com.mygdx.game.MultiBombermanGame;

public class RaspberryLauncher {
	public static void main(String[] arg) {
		JoglNewtApplicationConfiguration config = new JoglNewtApplicationConfiguration();
		config.title = "In The Well";
		config.width = Constante.SCREEN_SIZE_X;
		config.height = Constante.SCREEN_SIZE_Y;
		config.foregroundFPS = 60;
		config.vSyncEnabled = false;
		config.fullscreen = true;
		config.resizable = false;
		new JoglNewtApplication(new MultiBombermanGame(), config);
	}
}
