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
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.MessageService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;

public class SkinScreen implements Screen, MenuListener {

	private static final int START_X = 70;
	private static final int START_Y = 170;
	private static final int COL_SIZE = 140;
	private static final int ROW_SIZE = 40;

	private final MultiBombermanGame game;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public SkinScreen(final MultiBombermanGame game) {
		this.game = game;
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.game.getPlayerService().setMenuListener(this);
		if (Context.getGameMode() == GameModeEnum.SERVER) {
			game.getNetworkService().sendToClient("draw:test");
		}
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
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.skinScreen"));
		font.draw(game.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 210);
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 4; i++) {
				int pos = i + j * 4;
				layout.setText(font, MessageService.getInstance().getMessage("game.menu.player") + pos + " : ");
				font.draw(game.getBatch(), layout, START_X + (i * COL_SIZE), START_Y - (j * ROW_SIZE));
				game.getBatch()
						.draw(SpriteService.getInstance().getSprite(CharacterSpriteEnum.WALK_DOWN,
								game.getPlayerService().getPlayerColor(pos),
								game.getPlayerService().getPlayerCharacter(pos), 0), START_X + (i * COL_SIZE) + 60,
								START_Y - (j * ROW_SIZE) - 10);
			}
		}
		game.getBatch().end();
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
		game.getScreen().dispose();
		game.setScreen(new RulesScreen(game));
	}

	@Override
	public void pressSelect() {
		if (Context.getGameMode() == GameModeEnum.LOCAL) {
			game.getScreen().dispose();
			game.setScreen(new PlayerTypeScreen(game));
		} else if (Context.getGameMode() == GameModeEnum.SERVER) {
			game.getScreen().dispose();
			game.getNetworkService().acceptConnexion(true);
			game.setScreen(new WaitConnexionScreen(game));
		}
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
		//unused method
	}

	@Override
	public void pressRight() {
		//unused method
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
