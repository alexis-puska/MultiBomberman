package com.mygdx.view;

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
import com.mygdx.domain.Cursor;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class MainScreen implements Screen, MenuListener {

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public MainScreen(final MultiBombermanGame mbGame) {
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
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 0), 110, 70);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 1), 252, 70);
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.NETWORK, 2), 394, 70);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.local"));
		font.draw(mbGame.getBatch(), layout, 176 - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.server"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 62);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.network.client"));
		font.draw(mbGame.getBatch(), layout, 460 - (layout.width / 2), 62);
		switch (Context.getGameMode()) {
		case LOCAL:
			cursor.updateCursorPosition(166, 20);
			break;
		case SERVER:
			cursor.updateCursorPosition(310, 20);
			break;
		case CLIENT:
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
		mbGame.getScreen().dispose();
		switch (Context.getGameMode()) {
		case LOCAL:
			mbGame.setScreen(new PlayerTypeScreen(mbGame));
			break;
		case SERVER:
			mbGame.setScreen(new PlayerTypeScreen(mbGame));
			break;
		case CLIENT:
			mbGame.setScreen(new ClientConnexionScreen(mbGame));
			break;
		default:
			mbGame.setScreen(new PlayerTypeScreen(mbGame));
			break;
		}
	}

	@Override
	public void pressSelect() {
		mbGame.getScreen().dispose();
		mbGame.setScreen(new LangueScreen(mbGame));
	}

	@Override
	public void pressA() {
		//unused method

	}

	@Override
	public void pressUp() {
		//unused method

	}

	@Override
	public void pressDown() {
		//unused method

	}

	@Override
	public void pressLeft() {
		Context.decGameMode();
	}

	@Override
	public void pressRight() {
		Context.incGameMode();
	}

	@Override
	public void pressB() {
		// unused method
	}

	@Override
	public void pressX() {
		//unused method
		
	}

	@Override
	public void pressY() {
		//unused method
		
	}

	@Override
	public void pressL() {
		//unused method
		
	}

	@Override
	public void pressR() {
		//unused method
		
	}
}
