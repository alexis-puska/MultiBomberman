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

import com.mygdx.constante.EditorConstante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.editor.domain.level.Position;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.service.SpriteService;
import com.mygdx.game.editor.utils.CoordinateUtils;

public class DrawPanel extends Canvas {

	private static final long serialVersionUID = -617780220828076518L;

	private static final int FONT_SIZE = 14;

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
			drawStartPlayer(g2);
			drawGrid(g2);
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

	private void drawStartPlayer(Graphics2D g2) {
		Position sp = levelService.getCurrentLevel().getStartPlayers();
		if (sp != null) {
			Stroke savedStrock = g2.getStroke();
			g2.setColor(Color.GREEN);
			Font font = new Font("Arial", Font.PLAIN, FONT_SIZE);
			g2.setFont(font);
			g2.setStroke(new BasicStroke(2));

			g2.drawString("S", (sp.getX() * EditorConstante.GRID_SIZE_X + 2),
					(CoordinateUtils.invGridY(sp.getY()) * EditorConstante.GRID_SIZE_Y) + EditorConstante.GRID_SIZE_Y);
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
		BufferedImage bf = spriteService.getSprite(SpriteEnum.LEVEL, levelService.getCurrentLevel().getBackground());
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

}