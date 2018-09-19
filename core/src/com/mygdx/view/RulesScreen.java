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
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class RulesScreen implements Screen, MenuListener {

	private final MultiBombermanGame game;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private int cursorPosition;
	private BitmapFont font;

	public RulesScreen(final MultiBombermanGame game) {
		this.game = game;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.game.getPlayerService().setMenuListener(this);
		this.cursorPosition = 0;
		initFont();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getScreenCamera().update();
		game.getBatch().begin();
		game.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 1), 0, 0);
		game.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(game.getBatch().getProjectionMatrix());

		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 210);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		game.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.suddenDeath"));
		font.draw(game.getBatch(), layout, 200, 180);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.badBomber"));
		font.draw(game.getBatch(), layout, 200, 160);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.iaLevel"));
		font.draw(game.getBatch(), layout, 200, 140);
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.ruleScreen.time"));
		font.draw(game.getBatch(), layout, 200, 120);

		layout.setText(font, Context.isSuddenDeath() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(game.getBatch(), layout, 350, 180);
		layout.setText(font, Context.isBadBomber() ? MessageService.getInstance().getMessage("game.menu.yes")
				: MessageService.getInstance().getMessage("game.menu.no"));
		font.draw(game.getBatch(), layout, 350, 160);
		layout.setText(font, Integer.toString(Context.getIaLevel()));
		font.draw(game.getBatch(), layout, 350, 140);
		layout.setText(font, MessageService.getInstance().getMessage(Context.getTime().getKey()));
		font.draw(game.getBatch(), layout, 350, 120);

		this.updateCursorPosition();

		cursor.draw(game.getBatch());
		game.getBatch().end();
	}

	private void updateCursorPosition() {
		switch (cursorPosition) {
		case 0:
			this.cursor.updateCursorPosition(180, 170);
			break;
		case 1:
			this.cursor.updateCursorPosition(180, 150);
			break;
		case 2:
			this.cursor.updateCursorPosition(180, 130);
			break;
		case 3:
			this.cursor.updateCursorPosition(180, 110);
			break;
		}
	}

	@Override
	public void show() {
		// unused method
	}

	@Override
	public void resize(int width, int height) {
		game.getViewport().update(width, height, true);
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
		game.getScreen().dispose();
		game.setScreen(new LevelScreen(game));
	}

	@Override
	public void pressSelect() {
		game.getScreen().dispose();
		game.setScreen(new SkinScreen(game));
	}

	@Override
	public void pressValide() {
		// unused method
	}

	@Override
	public void pressUp() {
		cursorPosition--;
		if (cursorPosition < 0) {
			cursorPosition = 0;
		}
	}

	@Override
	public void pressDown() {
		cursorPosition++;
		if (cursorPosition > 3) {
			cursorPosition = 3;
		}
	}

	@Override
	public void pressLeft() {
		switch (cursorPosition) {
		case 0:
			Context.toogleSuddenDeath();
			break;
		case 1:
			Context.toogleBadBomber();
			break;
		case 2:
			Context.decIaLevel();
			break;
		case 3:
			Context.decTime();
			break;
		}
	}

	@Override
	public void pressRight() {
		switch (cursorPosition) {
		case 0:
			Context.toogleSuddenDeath();
			break;
		case 1:
			Context.toogleBadBomber();
			break;
		case 2:
			Context.incIaLevel();
			break;
		case 3:
			Context.incTime();
			break;
		}
	}

}
