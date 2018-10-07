package com.mygdx.game.editor.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.mygdx.game.editor.constant.Constante;
import com.mygdx.game.editor.domain.level.Decor;
import com.mygdx.game.editor.domain.level.Door;
import com.mygdx.game.editor.domain.level.Ennemie;
import com.mygdx.game.editor.domain.level.Item;
import com.mygdx.game.editor.domain.level.Lock;
import com.mygdx.game.editor.domain.level.Pick;
import com.mygdx.game.editor.domain.level.Platform;
import com.mygdx.game.editor.domain.level.Position;
import com.mygdx.game.editor.domain.level.Rayon;
import com.mygdx.game.editor.domain.level.Teleporter;
import com.mygdx.game.editor.domain.level.Vortex;
import com.mygdx.game.editor.domain.level.event.Event;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.service.SpriteService;
import com.mygdx.game.editor.utils.CoordinateUtils;

public class DrawPanel extends Canvas {

	private static final long serialVersionUID = -617780220828076518L;
	private static final int OFFSET = 10;

	private LevelService levelService;
	private SpriteService spriteService;

	public DrawPanel(SpriteService spriteService, LevelService levelService) {
		super();
		this.spriteService = spriteService;
		this.levelService = levelService;
	}

	/**
	 * Draw the level if exists, draw message if level doesn't exists
	 * 
	 * @param g2 graphics
	 */
	public void paint(Graphics g) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			bs = getBufferStrategy();
		}
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		if (levelService.getCurrentLevel() != null) {
			drawBackground(g2);
			drawDecor(g2, true);
			drawTeleporter(g2);
			drawPlatform(g2);
			drawRayon(g2);
			drawVortex(g2);
			drawEnnemies(g2);
			drawStartPlayer(g2);
			drawEffectObject(g2);
			drawPontObject(g2);
			drawItem(g2);
			drawEvent(g2);
			drawGrid(g2);
			drawLock(g2);
			drawDoors(g2);
			drawPick(g2);
			drawDecor(g2, false);
			g2.clearRect(421, 0, 200, 800);
			g2.clearRect(0, 501, 700, 300);

		} else {
			Font font = new Font("Serif", Font.PLAIN, 20);
			g2.setFont(font);
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, Constante.SCREEN_SIZE_X + 20, Constante.SCREEN_SIZE_Y);
			g2.setColor(Color.RED);
			g2.drawString("NO LEVEL FOUND", 130, 250);
		}
		g2.dispose();
		bs.show();
	}

	private void drawStartPlayer(Graphics2D g2) {
		Position sp = levelService.getCurrentLevel().getStartPlayers();
		if (sp != null) {
			Stroke savedStrock = g2.getStroke();
			g2.setColor(Color.GREEN);
			Font font = new Font("Arial", Font.PLAIN, 20);
			g2.setFont(font);
			g2.setStroke(new BasicStroke(2));

			g2.drawString("S", (sp.getX() * Constante.GRID_SIZE + 2) + OFFSET,
					(CoordinateUtils.invGridY(sp.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE);

			g2.setStroke(savedStrock);
		}
	}

	private void drawEffectObject(Graphics2D g2) {
		Position sp = levelService.getCurrentLevel().getStartEffectObjets();
		if (sp != null) {
			Stroke savedStrock = g2.getStroke();
			g2.setColor(Color.CYAN);
			Font font = new Font("Arial", Font.PLAIN, 20);
			g2.setFont(font);
			g2.setStroke(new BasicStroke(2));
			g2.drawString("E", (sp.getX() * Constante.GRID_SIZE + 2) + OFFSET,
					(CoordinateUtils.invGridY(sp.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE);
			g2.setStroke(savedStrock);
		}
	}

	private void drawPontObject(Graphics2D g2) {
		Position sp = levelService.getCurrentLevel().getStartPointObjets();
		if (sp != null) {
			Stroke savedStrock = g2.getStroke();
			g2.setColor(Color.YELLOW);
			Font font = new Font("Arial", Font.PLAIN, 20);
			g2.setFont(font);
			g2.setStroke(new BasicStroke(2));
			g2.drawString("P", (sp.getX() * Constante.GRID_SIZE + 2) + OFFSET,
					(CoordinateUtils.invGridY(sp.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE);
			g2.setStroke(savedStrock);
		}
	}

	/**
	 * Draw the level
	 * 
	 * @param g2 graphics2D
	 */
	private void drawGrid(Graphics2D g2) {

		float alpha = 0.7f;
		Composite saved = g2.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
		int x, y;
		g2.setColor(Color.RED);
		x = 0;
		while (x < Constante.SCREEN_SIZE_X) {
			g2.drawLine(OFFSET + x, 0, x + OFFSET, Constante.SCREEN_SIZE_Y);
			x += Constante.GRID_SIZE;
		}
		y = 0;
		while (y < Constante.SCREEN_SIZE_Y) {
			y += Constante.GRID_SIZE;
			g2.drawLine(OFFSET, y, OFFSET + Constante.SCREEN_SIZE_X, y);
		}
		g2.drawLine(0, 0, Constante.SCREEN_SIZE_X + 20, 0);
		g2.drawLine(0, 0, 0, Constante.SCREEN_SIZE_Y);
		g2.drawLine(OFFSET, 0, OFFSET, Constante.SCREEN_SIZE_Y);
		g2.drawLine(20 + Constante.SCREEN_SIZE_X, 0, 20 + Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
		g2.drawLine(OFFSET + Constante.SCREEN_SIZE_X, 0, OFFSET + Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
		g2.drawLine(0, Constante.SCREEN_SIZE_Y, 20 + Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
		g2.setComposite(saved);
	}

	/**
	 * Draw the background of the level
	 * 
	 * @param g2
	 */
	private void drawBackground(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite("level_background", levelService.getCurrentLevel().getBackground());
		int x = 0;
		int y = 0;
		while (x < Constante.SCREEN_SIZE_X) {
			while (y < Constante.SCREEN_SIZE_Y) {
				g2.drawImage(bf, null, x, y);
				y += bf.getHeight();
			}
			x += bf.getWidth();
			y = 0;
		}
	}

	private void drawItem(Graphics2D g2) {
		for (Item item : levelService.getCurrentLevel().getItems()) {
			BufferedImage bf = spriteService.getSprite("objects", item.getItemId());
			g2.drawImage(bf, null, item.getX() * Constante.GRID_SIZE - (bf.getWidth() / 2) + 10 + OFFSET,
					((CoordinateUtils.invGridY(item.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE)
							- bf.getHeight());
		}
	}

	private void drawLock(Graphics2D g2) {
		for (Lock lock : levelService.getCurrentLevel().getLock()) {
			BufferedImage bf = spriteService.getSprite("serrure", 1);
			g2.drawImage(bf, null, lock.getX() * Constante.GRID_SIZE - (bf.getWidth() / 2) + 10 + OFFSET,
					((CoordinateUtils.invGridY(lock.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE)
							- bf.getHeight());
		}
	}

	private void drawDecor(Graphics2D g2, boolean onBackground) {
		for (Decor decor : levelService.getCurrentLevel().getDecor()) {
			if (decor.isBack() != onBackground) {
				BufferedImage bf = spriteService.getDecor(decor.getIndexAnim());
				g2.drawImage(bf, null, decor.getX(), CoordinateUtils.drawY(decor.getY(), bf.getHeight()));
				g2.setColor(Color.BLUE);
				g2.fillRect(decor.getX() - 5, CoordinateUtils.drawY(decor.getY(), 0) - 5, 10, 10);
			} else {
				float alpha = 0.2f;
				Composite saved = g2.getComposite();
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g2.setComposite(ac);
				BufferedImage bf = spriteService.getDecor(decor.getIndexAnim());
				g2.drawImage(bf, null, decor.getX(), CoordinateUtils.drawY(decor.getY(), bf.getHeight()));
				g2.setColor(Color.BLUE);
				g2.fillRect(decor.getX() - 5, CoordinateUtils.drawY(decor.getY(), 0) - 5, 10, 10);

				g2.setComposite(saved);
			}
		}
	}

	private void drawDoors(Graphics2D g2) {
		for (Door door : levelService.getCurrentLevel().getDoor()) {
			int index = 0;
			if (door.isLocked()) {
				index = door.getType() * 2;
			} else {
				index = (door.getType() * 2) + 1;
			}
			BufferedImage bf = spriteService.getSprite("doors", index);
			g2.drawImage(bf, null, door.getX() * Constante.GRID_SIZE - (bf.getWidth() / 2) + 10 + OFFSET,
					((CoordinateUtils.invGridY(door.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE)
							- bf.getHeight());
			if (!door.isLocked()) {
				drawDisabledFlag(g2, door.getX(), CoordinateUtils.invGridY(door.getY()));
			}
		}
	}

	private void drawPick(Graphics2D g2) {
		for (Pick pick : levelService.getCurrentLevel().getPick()) {
			BufferedImage bf = spriteService.getSprite("pick", 1);
			AffineTransform backup = g2.getTransform();
			AffineTransform trans = new AffineTransform();
			if (pick.getDirection() == 1) {
				trans.rotate((Math.PI / 2), (pick.getX() * Constante.GRID_SIZE) + OFFSET,
						CoordinateUtils.invGridY(pick.getY()) * Constante.GRID_SIZE);
				trans.translate(0, -Constante.GRID_SIZE);
			} else if (pick.getDirection() == 2) {
				trans.rotate((Math.PI), (pick.getX() * Constante.GRID_SIZE) + OFFSET,
						CoordinateUtils.invGridY(pick.getY()) * Constante.GRID_SIZE);
				trans.translate(-Constante.GRID_SIZE, -Constante.GRID_SIZE);
			} else if (pick.getDirection() == 3) {
				trans.rotate(3 * (Math.PI / 2), (pick.getX() * Constante.GRID_SIZE) + OFFSET,
						CoordinateUtils.invGridY(pick.getY()) * Constante.GRID_SIZE);
				trans.translate(-Constante.GRID_SIZE, 0);
			}
			g2.transform(trans);
			g2.drawImage(bf, (pick.getX() * Constante.GRID_SIZE) + OFFSET,
					CoordinateUtils.invGridY(pick.getY()) * Constante.GRID_SIZE, null);
			g2.setTransform(backup); // restore previous transform
			if (!pick.isEnable()) {
				drawDisabledFlag(g2, pick.getX(), CoordinateUtils.invGridY(pick.getY()));
			}
		}
	}

	private void drawEvent(Graphics2D g2) {
		Stroke savedStrock = g2.getStroke();
		g2.setColor(Color.PINK);
		Font font = new Font("Arial", Font.PLAIN, 20);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		for (Event event : levelService.getCurrentLevel().getEvent()) {
			g2.drawString("Ev", (int) (event.getX() * (double) Constante.GRID_SIZE) + OFFSET,
					(int) (CoordinateUtils.invGridY(event.getY()) * (double) Constante.GRID_SIZE)
							+ Constante.GRID_SIZE);
		}
		g2.setStroke(savedStrock);
	}

	private void drawVortex(Graphics2D g2) {
		for (Vortex vortex : levelService.getCurrentLevel().getVortex()) {
			BufferedImage bf = spriteService.getSprite("vortex", 0);
			g2.drawImage(bf,
					(vortex.getX() * Constante.GRID_SIZE - ((int) (bf.getWidth() * vortex.getZoomX()) / 2) + 10)
							+ OFFSET,
					((CoordinateUtils.invGridY(vortex.getY()) * Constante.GRID_SIZE) + Constante.GRID_SIZE)
							- bf.getHeight(),
					(int) (bf.getWidth() * vortex.getZoomX()), (int) (bf.getHeight() * vortex.getZoomY()), null);
			if (!vortex.isEnable()) {
				drawDisabledFlag(g2, vortex.getX(), CoordinateUtils.invGridY(vortex.getY()));
			}
		}
	}

	/**
	 * draw platform of the level
	 * 
	 * @param g2
	 */
	private void drawPlatform(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite("platform", levelService.getCurrentLevel().getHorizontalPlateform());
		BufferedImage bfv = spriteService.getSprite("platform", levelService.getCurrentLevel().getVerticalPlateform());
		for (Platform platform : levelService.getCurrentLevel().getPlatform()) {

			if (platform.isVertical()) {
				AffineTransform backup = g2.getTransform();
				AffineTransform trans = new AffineTransform();
				trans.rotate((3 * Math.PI / 2), platform.getX() * Constante.GRID_SIZE,
						CoordinateUtils.invGridY(platform.getY()) * Constante.GRID_SIZE); // the points to
				trans.translate(-Constante.GRID_SIZE, Constante.GRID_SIZE - OFFSET);
				g2.transform(trans);
				g2.drawImage(bfv.getSubimage(0, 0, platform.getLength() * Constante.GRID_SIZE, Constante.GRID_SIZE),
						(platform.getX() * Constante.GRID_SIZE),
						CoordinateUtils.invGridY(platform.getY()) * Constante.GRID_SIZE, null);
				g2.setTransform(backup);

			} else {
				g2.drawImage(bf.getSubimage(0, 0, platform.getLength() * Constante.GRID_SIZE, Constante.GRID_SIZE),
						(platform.getX() * Constante.GRID_SIZE) + OFFSET,
						CoordinateUtils.invGridY(platform.getY()) * Constante.GRID_SIZE, null);

			}
			if (!platform.isDisplayed()) {
				drawDisabledFlag(g2, platform.getX(), CoordinateUtils.invGridY(platform.getY()));
			}
		}
	}

	/**
	 * draw rayon of the level
	 * 
	 * @param g2
	 */
	private void drawRayon(Graphics2D g2) {
		for (Rayon rayon : levelService.getCurrentLevel().getRayon()) {
			BufferedImage bf = spriteService.getSprite("rayon", rayon.getType().getIndex());
			if (rayon.isVertical()) {
				for (int i = rayon.getY(); i < rayon.getY() + rayon.getLength(); i++) {
					g2.drawImage(bf, (rayon.getX() * Constante.GRID_SIZE) + OFFSET,
							CoordinateUtils.invGridY(i) * Constante.GRID_SIZE, null);
				}
			} else {
				for (int i = 0; i < rayon.getLength(); i++) {
					AffineTransform backup = g2.getTransform();
					AffineTransform trans = new AffineTransform();
					trans.rotate((Math.PI / 2), (rayon.getX() + i) * Constante.GRID_SIZE,
							CoordinateUtils.invGridY(rayon.getY()) * Constante.GRID_SIZE);
					trans.translate(0, -Constante.GRID_SIZE - OFFSET);
					g2.transform(trans);
					g2.drawImage(bf, ((rayon.getX() + i) * Constante.GRID_SIZE),
							CoordinateUtils.invGridY(rayon.getY()) * Constante.GRID_SIZE, null);
					g2.setTransform(backup); // restore previous transform
				}
			}
		}
	}

	/**
	 * draw rayon of the level
	 * 
	 * @param g2
	 */
	private void drawTeleporter(Graphics2D g2) {
		for (Teleporter teleporter : levelService.getCurrentLevel().getTeleporter()) {
			BufferedImage bf = spriteService.getSprite("teleporter", 0);
			if (teleporter.isVertical()) {
				for (int i = teleporter.getY(); i < teleporter.getY() + teleporter.getLength(); i++) {
					g2.drawImage(bf.getSubimage(0, 0, Constante.GRID_SIZE, Constante.GRID_SIZE),
							(teleporter.getX() * Constante.GRID_SIZE) + OFFSET,
							CoordinateUtils.invGridY(i) * Constante.GRID_SIZE, null);
				}
			} else {
				for (int i = 0; i < teleporter.getLength(); i++) {
					AffineTransform backup = g2.getTransform();
					AffineTransform trans = new AffineTransform();
					trans.rotate((Math.PI / 2), (teleporter.getX() + i) * Constante.GRID_SIZE,
							CoordinateUtils.invGridY(teleporter.getY()) * Constante.GRID_SIZE);
					trans.translate(0, -Constante.GRID_SIZE - OFFSET);
					g2.transform(trans);
					g2.drawImage(bf.getSubimage(0, 0, Constante.GRID_SIZE, Constante.GRID_SIZE),
							((teleporter.getX() + i) * Constante.GRID_SIZE),
							CoordinateUtils.invGridY(teleporter.getY()) * Constante.GRID_SIZE, null);
					g2.setTransform(backup); // restore previous transform
				}
			}
		}
	}

	/**
	 * draw Ennemies of the level
	 * 
	 * @param g2
	 */
	private void drawEnnemies(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite("cerise", 0);
		for (Ennemie ennemie : levelService.getCurrentLevel().getEnnemies()) {
			switch (ennemie.getType()) {
			case CERISE:
				bf = spriteService.getSprite("cerise", 0);
				break;
			case ORANGE:
				bf = spriteService.getSprite("orange", 0);
				break;
			case POMME:
				bf = spriteService.getSprite("pomme", 0);
				break;
			case BANANE:
				bf = spriteService.getSprite("banane", 0);
				break;
			case CITRON:
				bf = spriteService.getSprite("citron", 0);
				break;
			case PRUNE:
				bf = spriteService.getSprite("bombinos", 0);
				break;
			case POIRE:
				bf = spriteService.getSprite("poire", 0);
				break;
			case ABRICOT:
				bf = spriteService.getSprite("abricot", 0);
				break;
			case LITCHI:
				bf = spriteService.getSprite("litchi", 0);
				break;
			case FRAISE:
				bf = spriteService.getSprite("fraise", 0);
				break;
			case KIWI:
				bf = spriteService.getSprite("kiwi", 0);
				break;
			case PASTEQUE:
				bf = spriteService.getSprite("pasteque", 0);
				break;
			case ANNANAS:
				bf = spriteService.getSprite("ananas", 0);
				break;
			case BLOB:
				bf = spriteService.getSprite("blob", 0);
				break;
			case FRAMBOISE:
				bf = spriteService.getSprite("framboise", 0);
				break;
			case ABRICOT_NAIN:
				bf = spriteService.getSprite("nainbricot", 0);
				break;
			case SCIE:
				bf = spriteService.getSprite("scie", 0);
				break;
			}
			int x = ((ennemie.getX() * Constante.GRID_SIZE) + (Constante.GRID_SIZE / 2) - (bf.getWidth() / 2)) + OFFSET;
			int y = ((CoordinateUtils.invGridY(ennemie.getY()) + 1) * Constante.GRID_SIZE) - bf.getHeight();
			g2.drawImage(bf, x, y, null);
		}
	}

	private void drawDisabledFlag(Graphics2D g2, int x, int y) {
		Stroke savedStrock = g2.getStroke();
		g2.setColor(Color.RED);
		Font font = new Font("Arial", Font.BOLD, 14);
		g2.setFont(font);
		g2.setStroke(new BasicStroke(2));
		g2.drawString(" ! !", (x * Constante.GRID_SIZE) + OFFSET, (y * Constante.GRID_SIZE) + Constante.GRID_SIZE);
		g2.setStroke(savedStrock);
	}
}