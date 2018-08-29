package com.mygdx.game;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.service.NetworkService;
import com.mygdx.service.SoundService;
import com.mygdx.service.input_processor.MenuInputProcessor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiBombermanGame extends Game {
	private NetworkService networkService;
	private Texture img;

	private SpriteBatch batch;
	private OrthographicCamera screenCamera;
	private Viewport viewport;
	private MenuInputProcessor menuInputProcessor;

	@Override
	public void create() {
		Gdx.app.setLogLevel(Constante.LIBGDX_LOG_LEVEL);
		networkService = new NetworkService();
		// SpriteService.getInstance();

		/****************************************
		 * Camera and viewport to draw fit image
		 ****************************************/
		screenCamera = new OrthographicCamera();
		screenCamera.position.set(Constante.SCREEN_SIZE_X / 2, Constante.SCREEN_SIZE_Y / 2, 0);
		screenCamera.update();
		viewport = new FitViewport(Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y, screenCamera);
		viewport.apply();

		batch = new SpriteBatch();
		batch.setProjectionMatrix(screenCamera.combined);
		menuInputProcessor = new MenuInputProcessor();
		SoundService.getInstance().playMusic(MusicEnum.MENU);
		Gdx.input.setInputProcessor(menuInputProcessor);

		// this.setScreen(new SplashScreen(this));
		img = new Texture(Gdx.files.internal("sprite/characters/sprite_bomberman.png"));
		networkService = new NetworkService();

	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!img.getTextureData().isPrepared()) {
			img.getTextureData().prepare();
		}
		Pixmap pixmap = img.getTextureData().consumePixmap();
		printPixelInsideTexture(pixmap);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				int pixelColor = pixmap.getPixel(x, y);

				if (pixelColor == 0xf8f8f8ff) {
					pixmap.setColor(0x707070ff);
					pixmap.drawPixel(x, y);
				}
				if (pixelColor == 0xf8a020ff) {
					pixmap.setColor(0x303030ff);
					pixmap.drawPixel(x, y);
				}
				if (pixelColor == 0xf8a020ff) {
					pixmap.setColor(0xf8a020ff);
					pixmap.drawPixel(x, y);
				}
				if (pixelColor == 0x0058e8ff) {

					pixmap.setColor(0x505050ff);
					pixmap.drawPixel(x, y);
				}
				if (pixelColor == 0x00b018ff) {
					pixmap.setColor(0xC0C0C0ff);
					pixmap.drawPixel(x, y);
				}
				if (pixelColor == 0xe82050ff) {
					pixmap.setColor(0xE01898ff);
					pixmap.drawPixel(x, y);
				}
			}
		}
		Texture tex = new Texture(pixmap);
		batch.begin();
		batch.draw(tex, 0, 0);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
	}

	private void printPixelInsideTexture(Pixmap pixmap) {
		Set<Integer> values = new HashSet<>();
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				values.add(pixmap.getPixel(x, y));
			}
		}
		System.out.println("values : ");
		values.stream().forEach(v -> System.out.println(String.format("0x%08X", v)));
	}
}
