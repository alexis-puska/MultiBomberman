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
import com.mygdx.game.Game;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.MessageService;
import com.mygdx.service.input_processor.MenuListener;

public class GameScreen implements Screen, MenuListener {

	private final MultiBombermanGame mbGame;

	/********************
	 * --- TEXT ---
	 ********************/
	private GlyphLayout layout;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	private Game game;
	private boolean pause;

	public GameScreen(final MultiBombermanGame mbGame) {
		this.pause = false;
		this.mbGame = mbGame;
		this.mbGame.getPlayerService().setMenuListener(this);
		this.layout = new GlyphLayout();
		this.game = new Game(mbGame);
		this.shapeRenderer = new ShapeRenderer();
		initFont();
	}

	/******************************
	 * --- Init font draw text ---
	 ******************************/
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
	public void render(float delta) {
		// clear screen
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		mbGame.getScreenCamera().update();
		if (pause) {
			this.game.render();
			drawPause();
		} else {
			this.game.step();
			this.game.render();
		}
	}

	private void drawPause() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(0, 0, 640, 360);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.levelScreen"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2f) - (layout.width / 2f), 210);
		mbGame.getBatch().end();
	}

	@Override
	public void show() {// unused method
	}

	@Override
	public void resize(int width, int height) {// unused method
	}

	@Override
	public void pause() {// unused method
	}

	@Override
	public void resume() {// unused method
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

	/**************************************************
	 * --- Player 1 control screen --- can pause / resume / exit game
	 **************************************************/
	@Override
	public void pressStart() {
		if (this.pause) {
			this.pause = false;
		} else {
			this.pause = true;
		}
	}

	@Override
	public void pressSelect() {
		this.game.dispose();
		mbGame.getScreen().dispose();
		mbGame.setScreen(new LevelScreen(mbGame));
	}

	@Override
	public void pressA() { // unused method
	}

	@Override
	public void pressUp() { // unused method
	}

	@Override
	public void pressDown() {// unused method
	}

	@Override
	public void pressLeft() {// unused method
	}

	@Override
	public void pressRight() {// unused method
	}

	@Override
	public void pressB() {// unused method
	}

	@Override
	public void pressX() {// unused method
	}

	@Override
	public void pressY() {// unused method
	}

	@Override
	public void pressL() {// unused method
	}

	@Override
	public void pressR() {// unused method
	}
}
