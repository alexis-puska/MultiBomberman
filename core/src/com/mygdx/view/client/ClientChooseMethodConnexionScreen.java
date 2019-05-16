package com.mygdx.view.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.constante.Constante;
import com.mygdx.domain.screen.Cursor;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.view.MainScreen;

public class ClientChooseMethodConnexionScreen implements Screen, MenuListener {

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public ClientChooseMethodConnexionScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.mbGame.getPlayerService().setMenuListener(this);
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK_CLIENT, 0), 110, 70);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK_CLIENT, 1), 252, 70);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK_CLIENT, 2), 394, 70);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.connexion"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.connexion.local"));
		font.draw(mbGame.getBatch(), layout, 176 - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.connexion.internet"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.connexion.ip"));
		font.draw(mbGame.getBatch(), layout, 460 - (layout.width / 2), 62);
		switch (Context.getClientConnexionMethod()) {
		case LOCAL:
			cursor.updateCursorPosition(166, 20);
			break;
		case INTERNET:
			cursor.updateCursorPosition(310, 20);
			break;
		case IP:
			cursor.updateCursorPosition(450, 20);
			break;
		default:
			cursor.updateCursorPosition(166, 20);
			break;
		}
		cursor.draw(mbGame.getBatch());
		mbGame.getBatch().end();
	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {
		// unused method
	}

	@Override
	public void pause() {
		// unused method
	}

	@Override
	public void resume() {
		// unused method
	}

	@Override
	public void hide() {
		// unused method
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
		font.dispose();
	}

	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void pressStart() {
		SoundService.getInstance().playSound(SoundEnum.VALIDE);
		mbGame.getScreen().dispose();
		switch (Context.getClientConnexionMethod()) {
		case IP:
			mbGame.setScreen(new ClientIPConnexionScreen(mbGame));
			break;
		case INTERNET:
			mbGame.setScreen(new ClientInternetConnexionScreen(mbGame));
			break;
		case LOCAL:
		default:
			mbGame.setScreen(new ClientLocalConnexionScreen(mbGame));
			break;
		}
	}

	@Override
	public void pressSelect() {
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
		mbGame.getScreen().dispose();
		mbGame.setScreen(new MainScreen(mbGame));
	}

	@Override
	public void pressA() {
		// unused method

	}

	@Override
	public void pressUp() {
		// unused method

	}

	@Override
	public void pressDown() {
		// unused method

	}

	@Override
	public void pressLeft() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		Context.decConnexionMethode();
	}

	@Override
	public void pressRight() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		Context.incConnexionMethod();
	}

	@Override
	public void pressB() {
		// unused method
	}

	@Override
	public void pressX() {
		// unused method

	}

	@Override
	public void pressY() {
		// unused method

	}

	@Override
	public void pressL() {
		// unused method

	}

	@Override
	public void pressR() {
		// unused method

	}
}
