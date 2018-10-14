package com.mygdx.game.editor.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.mygdx.constante.EditorConstante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.editor.service.SpriteService;

public class ForegroundDrawPanel extends Canvas {

	private static final long serialVersionUID = -617780220828076518L;
	private static final int HEIGHT = 48;
	private static final int WIDTH = 54;
	private static final int NB_COLUMN = 5;

	private SpriteService spriteService;

	public ForegroundDrawPanel(SpriteService spriteService) {
		super();
		this.spriteService = spriteService;
	}

	public void paint(Graphics g) {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			bs = getBufferStrategy();
		}
		Graphics2D g2 = (Graphics2D) bs.getDrawGraphics();
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		int idx = 0;
		int nb = this.spriteService.getSpriteAnimationSize(SpriteEnum.SKY);
		int idxColumn = 0;
		int idxRow = 0;
		while (idx < nb) {
			BufferedImage pf = this.spriteService.getSprite(SpriteEnum.SKY, idx);
			g2.drawImage(pf.getSubimage(0, 0, WIDTH, HEIGHT), idxColumn * WIDTH, idxRow * HEIGHT, null);
			idxColumn++;
			idx++;
			if (idx != 0 && idx % NB_COLUMN == 0) {
				idxRow++;
				idxColumn = 0;
			}
		}
		g2.dispose();
		bs.show();
	}
}