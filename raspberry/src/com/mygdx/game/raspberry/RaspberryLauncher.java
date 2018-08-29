package com.mygdx.game.raspberry;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.constante.Constante;
import com.mygdx.game.MultiBombermanGame;

public class RaspberryLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MultiBomberman";
		config.width = Constante.SCREEN_SIZE_X;
		config.height = Constante.SCREEN_SIZE_Y;
		config.foregroundFPS = 25;
		config.backgroundFPS = 25;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		config.resizable = true;
		config.addIcon("icon_128.png", FileType.Internal);
		config.addIcon("icon_32.png", FileType.Internal);
		config.addIcon("icon_16.png", FileType.Internal);
		new LwjglApplication(new MultiBombermanGame(), config);
	}
}
