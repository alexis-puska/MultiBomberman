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
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.List;

import com.mygdx.constante.EditorConstante;
import com.mygdx.dto.level.CustomTextureDTO;
import com.mygdx.dto.level.WallDTO;
import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.editor.service.LevelService2;
import com.mygdx.game.editor.service.SpriteService;
import com.mygdx.game.editor.utils.CoordinateUtils;

public class DrawPanel extends Canvas {

	private static final long serialVersionUID = -617780220828076518L;

	private static final int FONT_SIZE = 14;
	private static final int SMALL_FONT_SIZE = 8;

	private LevelService2 levelService;
	private SpriteService spriteService;

	private int posX;
	private int posY;

	public DrawPanel(SpriteService spriteService, LevelService2 levelService) {
		super();
		this.spriteService = spriteService;
		this.levelService = levelService;
		this.posX = -1;
		this.posY = -1;
	}

	public void updatePoint(int posX, int posY) {
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * Draw the level if exists, draw message if level doesn't exists
	 * 
	 * @param g2
	 *            graphics
	 */
	@Override
	public void paint(Graphics g) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			bs = getBufferStrategy();
		}
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		if (levelService.getCurrentLevel() != null) {
			drawBackground(g2);
			drawCustomBackground(g2);
			drawWall(g2);
			drawHole(g2);
			drawInterrupter(g2);
			drawMine(g2);
			drawRail(g2);
			drawTrolley(g2);
			drawTeleporter(g2);

			drawCustomForeground(g2);
			drawStartPlayer(g2);
			drawGrid(g2);
			drawPoint(g2);
			g2.clearRect(630, 0, 200, 800);
			g2.clearRect(0, 336, 700, 300);

		} else {
			Font font = new Font("Serif", Font.PLAIN, FONT_SIZE);
			g2.setFont(font);
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
			g2.setColor(Color.RED);
			g2.drawString("NO LEVEL FOUND", 230, 158);
			drawGrid(g2);
		}
		g2.dispose();
		bs.show();
	}

	private void drawPoint(Graphics2D g2) {
		if (posX != -1 && posY != -1) {
			g2.setColor(Color.RED);
			g2.fillOval(posX * EditorConstante.GRID_SIZE_X + (EditorConstante.GRID_SIZE_X / 2),
					(20 - (posY)) * EditorConstante.GRID_SIZE_Y + (EditorConstante.GRID_SIZE_Y / 2), 5, 5);
		}
	}

	/**
	 * Draw the level
	 * 
	 * @param g2
	 *            graphics2D
	 */
	private void drawGrid(Graphics2D g2) {

		float alpha = 0.7f;
		Composite saved = g2.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g2.setComposite(ac);
		int x;
		int y;
		g2.setColor(Color.RED);
		x = 0;
		while (x < EditorConstante.SCREEN_SIZE_X) {
			g2.drawLine(x, 0, x, EditorConstante.SCREEN_SIZE_Y);
			x += EditorConstante.GRID_SIZE_X;
		}
		y = 0;
		while (y < EditorConstante.SCREEN_SIZE_Y) {
			g2.drawLine(0, y, EditorConstante.SCREEN_SIZE_X, y);
			y += EditorConstante.GRID_SIZE_Y;
		}
		g2.drawLine(0, 0, EditorConstante.SCREEN_SIZE_X, 0);
		g2.drawLine(0, 0, 0, EditorConstante.SCREEN_SIZE_Y);
		g2.drawLine(EditorConstante.SCREEN_SIZE_X, 0, EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		g2.drawLine(0, EditorConstante.SCREEN_SIZE_Y, EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		g2.setComposite(saved);
	}

	/**
	 * Draw the background of the level
	 * 
	 * @param g2
	 */
	private void drawBackground(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.LEVEL,
				levelService.getCurrentVariante().getDefaultBackground().getIndex());
		int x = 0;
		int y = 0;
		while (x < EditorConstante.SCREEN_SIZE_X) {
			while (y < EditorConstante.SCREEN_SIZE_Y) {
				g2.drawImage(bf, null, x, y);
				y += bf.getHeight();
			}
			x += bf.getWidth();
			y = 0;
		}
	}

	private void drawHole(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.HOLE, 0);
		List<PositionableDTO> hole = levelService.getCurrentVariante().getHole();
		if (hole != null) {
			hole.stream().forEach(w -> g2.drawImage(bf, null, w.getX() * bf.getWidth(),
					CoordinateUtils.invGridY(w.getY()) * bf.getHeight()));
		}
	}

	private void drawRail(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.RAIL, 0);
		List<PositionableDTO> rail = levelService.getCurrentVariante().getRail();
		if (rail != null) {
			rail.stream().forEach(w -> g2.drawImage(bf, null, w.getX() * bf.getWidth(),
					CoordinateUtils.invGridY(w.getY()) * bf.getHeight()));
		}
	}

	private void drawInterrupter(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.BUTTON, 0);
		List<PositionableDTO> interrupter = levelService.getCurrentVariante().getInterrupter();
		if (interrupter != null) {
			interrupter.stream().forEach(w -> g2.drawImage(bf, null, w.getX() * bf.getWidth(),
					CoordinateUtils.invGridY(w.getY()) * bf.getHeight()));
		}
	}

	private void drawMine(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.MINE, 0);
		List<PositionableDTO> mine = levelService.getCurrentVariante().getMine();
		if (mine != null) {
			mine.stream().forEach(w -> g2.drawImage(bf, null, w.getX() * bf.getWidth(),
					CoordinateUtils.invGridY(w.getY()) * bf.getHeight()));
		}
	}

	private void drawTrolley(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.TROLLEY, 0);
		List<PositionableDTO> trolley = levelService.getCurrentVariante().getTrolley();
		if (trolley != null) {
			trolley.stream().forEach(w -> g2.drawImage(bf, null, (w.getX() * EditorConstante.GRID_SIZE_X) - 6,
					(CoordinateUtils.invGridY(w.getY()) * EditorConstante.GRID_SIZE_Y) - 26));
		}
	}

	private void drawTeleporter(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.TELEPORTER, 0);
		List<PositionableDTO> teleporter = levelService.getCurrentVariante().getTeleporter();
		if (teleporter != null) {
			teleporter.stream().forEach(w -> g2.drawImage(bf, null, w.getX() * bf.getWidth(),
					CoordinateUtils.invGridY(w.getY()) * bf.getHeight()));
		}
	}

	private void drawWall(Graphics2D g2) {
		BufferedImage bf = spriteService.getSprite(SpriteEnum.LEVEL,
				levelService.getCurrentVariante().getDefaultWall().getIndex());
		List<WallDTO> wall = levelService.getCurrentVariante().getWall();
		if (wall != null) {
			wall.stream().forEach(w -> {
				if (w.isDraw()) {
					if (w.getTexture() != null) {
						BufferedImage bf1 = spriteService.getSprite(w.getTexture().getAnimation(),
								w.getTexture().getIndex());
						g2.drawImage(bf1, null, w.getX() * bf.getWidth(),
								CoordinateUtils.invGridY(w.getY()) * bf.getHeight());
					} else {
						g2.drawImage(bf, null, w.getX() * bf.getWidth(),
								CoordinateUtils.invGridY(w.getY()) * bf.getHeight());
					}
				}
				Stroke savedStrock = g2.getStroke();
				g2.setColor(Color.RED);
				Font font = new Font("Arial", Font.PLAIN, SMALL_FONT_SIZE);
				g2.setFont(font);
				g2.setStroke(new BasicStroke(2));
				g2.drawString("w", (w.getX() * EditorConstante.GRID_SIZE_X + 2),
						(CoordinateUtils.invGridY(w.getY()) * EditorConstante.GRID_SIZE_Y)
								+ EditorConstante.GRID_SIZE_Y);
				g2.setStroke(savedStrock);
			});
		}
	}

	private void drawCustomBackground(Graphics2D g2) {
		List<CustomTextureDTO> customBackgroundTexture = levelService.getCurrentVariante().getCustomBackgroundTexture();
		if (customBackgroundTexture != null) {
			customBackgroundTexture.stream().forEach(w -> {
				BufferedImage bf = spriteService.getSprite(w.getAnimation(), w.getIndex());
				g2.drawImage(bf, null, w.getX() * bf.getWidth(), CoordinateUtils.invGridY(w.getY()) * bf.getHeight());

			});
		}
	}

	private void drawCustomForeground(Graphics2D g2) {
		List<CustomTextureDTO> customForegroundTexture = levelService.getCurrentVariante().getCustomForegroundTexture();
		if (customForegroundTexture != null) {
			customForegroundTexture.stream().forEach(w -> {
				BufferedImage bf = spriteService.getSprite(w.getAnimation(), w.getIndex());
				g2.drawImage(bf, null, (w.getX() * EditorConstante.GRID_SIZE_X) - 18,
						(CoordinateUtils.invGridY(w.getY()) * EditorConstante.GRID_SIZE_Y) - 16);
			});
		}
	}

	private void drawStartPlayer(Graphics2D g2) {
		List<PositionableDTO> sps = levelService.getCurrentVariante().getStartPlayer();
		if (sps != null) {
			sps.stream().forEach(sp -> {
				Stroke savedStrock = g2.getStroke();
				g2.setColor(Color.RED);
				Font font = new Font("Arial", Font.PLAIN, FONT_SIZE);
				g2.setFont(font);
				g2.setStroke(new BasicStroke(2));

				g2.drawString("S", (sp.getX() * EditorConstante.GRID_SIZE_X + 2),
						(CoordinateUtils.invGridY(sp.getY()) * EditorConstante.GRID_SIZE_Y)
								+ EditorConstante.GRID_SIZE_Y);
				g2.setStroke(savedStrock);
			});
		}
	}

}