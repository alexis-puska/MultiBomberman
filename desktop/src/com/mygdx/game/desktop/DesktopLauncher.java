package com.mygdx.game.desktop;

import java.util.Base64;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.constante.Constante;
import com.mygdx.main.MultiBombermanGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "MultiBomberman";
		config.width = Constante.SCREEN_SIZE_X;
		config.height = Constante.SCREEN_SIZE_Y;
		config.foregroundFPS = Constante.FPS;
		config.backgroundFPS = Constante.FPS;
		config.vSyncEnabled = false;
		config.fullscreen = false;
		config.resizable = true;
		config.x = 10;
		config.y = 10;
		config.addIcon("icon_128.png", FileType.Internal);
		config.addIcon("icon_32.png", FileType.Internal);
		config.addIcon("icon_16.png", FileType.Internal);
		long s = System.currentTimeMillis();
		String originalInput = "aaaaaaaaaaaa";
		String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		long e = System.currentTimeMillis();
		System.out.println("time : " + (e - s));
		System.out.println("original : " + originalInput);
		System.out.println("size " + originalInput.getBytes().length);
		System.out.println("encoded : " + encodedString);

		new LwjglApplication(new MultiBombermanGame(), config);
	}
}
