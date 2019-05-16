package com.mygdx.view.client;

import java.util.List;

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
import com.mygdx.service.MessageService;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.client.DiscoveryClient;
import com.mygdx.service.network.dto.DiscoveryServerInfo;

public class ClientLocalConnexionScreen implements Screen, MenuListener {
	private static final int NB_SERVER_DISPLAYED = 12;

	private final MultiBombermanGame mbGame;
	private final Cursor cursor;
	private final GlyphLayout layout;
	private final ShapeRenderer shapeRenderer;
	private final DiscoveryClient discovery;
	private BitmapFont font;
	private BitmapFont largeFont;
	private int cursorPos;
	private int displayListOffset;
	private List<DiscoveryServerInfo> listOfServer;

	public ClientLocalConnexionScreen(final MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		this.cursorPos = 0;
		this.cursor = new Cursor(198, 90);
		this.layout = new GlyphLayout();
		this.shapeRenderer = new ShapeRenderer();
		this.discovery = new DiscoveryClient();
		this.mbGame.getPlayerService().setMenuListener(this);
		initFont();
		refresh();
	}

	private void refresh() {
		discovery.refreshLocalNetworkServerList();
		listOfServer = discovery.getLocalNetworkServer();
		displayListOffset = 0;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		updateCursor();
		mbGame.getScreenCamera().update();
		mbGame.getBatch().begin();
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.BACKGROUND, 0), 0, 0);
		mbGame.getBatch().end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(mbGame.getBatch().getProjectionMatrix());
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0.5f);
		shapeRenderer.rect(10, 10, 620, 340);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		mbGame.getBatch().begin();
		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientLocalConnexionScreen"));
		font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 340);

		layout.setText(font, MessageService.getInstance().getMessage("game.menu.clientLocalConnexionScreen.refresh"));
		font.draw(mbGame.getBatch(), layout, 50, 320);

		layout.setText(font,
				MessageService.getInstance().getMessage("game.menu.clientLocalConnexionScreen.server")
						+ (cursorPos == 0 ? "Nan / " : (cursorPos + displayListOffset) + "/")
						+ (listOfServer == null ? "Nan" : listOfServer.size()));
		font.draw(mbGame.getBatch(), layout, 490, 320);

		if (mbGame.getNetworkService().getLastClientError() != null) {
			layout.setText(font, MessageService.getInstance()
					.getMessage("game.menu.clientConnexionScreen." + mbGame.getNetworkService().getLastClientError()));
			font.draw(mbGame.getBatch(), layout, (Constante.SCREEN_SIZE_X / 2) - (layout.width / 2), 50);
		}

		if (listOfServer != null) {
			int offs = 0;
			for (int i = displayListOffset; i < displayListOffset + NB_SERVER_DISPLAYED; i++) {
				if (i < listOfServer.size()) {
					DiscoveryServerInfo s = listOfServer.get(i);
					layout.setText(font, s.getIp() + " " + s.getPort());
					font.draw(mbGame.getBatch(), layout, 50, 300 - (offs * 20));
					offs++;
				}
			}
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
		largeFont.dispose();
	}

	public void initFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/font_gbboot.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 14;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		font = generator.generateFont(parameter);
		parameter.size = 24;
		parameter.borderWidth = 0f;
		parameter.borderColor = new Color(255, 0, 0, 255);
		parameter.color = new Color(255, 0, 0, 255);
		largeFont = generator.generateFont(parameter);
		generator.dispose();
	}

	private void updateCursor() {
		cursor.updateCursorPosition(20, 310 - (cursorPos * 20));
	}

	@Override
	public void pressStart() {
		if (cursorPos == 0) {
			this.refresh();
		} else {
			SoundService.getInstance().playSound(SoundEnum.VALIDE);
			mbGame.getPlayerService().initControllerMap();
			ClientViewScreen cvs = new ClientViewScreen(mbGame);
			DiscoveryServerInfo dsi = listOfServer.get((cursorPos - 1) + displayListOffset);
			if (mbGame.getNetworkService().connectToServer(dsi.getPort(), dsi.getIp(), cvs)) {
				mbGame.getScreen().dispose();
				mbGame.setScreen(cvs);
			}
		}
	}

	@Override
	public void pressSelect() {
		SoundService.getInstance().playSound(SoundEnum.CANCEL);
		mbGame.getScreen().dispose();
		mbGame.setScreen(new ClientChooseMethodConnexionScreen(mbGame));
	}

	@Override
	public void pressUp() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		if (cursorPos > 0) {
			if (cursorPos == 1 && displayListOffset > 0) {
				displayListOffset--;
			} else {
				cursorPos--;
			}
		}
	}

	@Override
	public void pressDown() {
		SoundService.getInstance().playSound(SoundEnum.BIP);
		if (listOfServer != null && cursorPos < listOfServer.size() + 1) {
			if (cursorPos == NB_SERVER_DISPLAYED) {
				if (cursorPos + displayListOffset < listOfServer.size()) {
					displayListOffset++;
				}
			} else {
				cursorPos++;
			}
		}
	}

	@Override
	public void pressLeft() {
		// unused
	}

	@Override
	public void pressRight() {
		// unused
	}

	@Override
	public void pressA() {
		// unused
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
		int res = displayListOffset;
		res = displayListOffset - NB_SERVER_DISPLAYED;
		if (res < 0) {
			res = 0;
		}
		displayListOffset = res;
	}

	@Override
	public void pressR() {
		int res = displayListOffset;
		res = displayListOffset + NB_SERVER_DISPLAYED;
		if (res > listOfServer.size() - NB_SERVER_DISPLAYED) {
			res = listOfServer.size() - NB_SERVER_DISPLAYED;
		}
		displayListOffset = res;
	}
}
