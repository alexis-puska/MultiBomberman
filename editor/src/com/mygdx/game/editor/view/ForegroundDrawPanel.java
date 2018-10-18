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

	private SpriteService spriteService;

	public ForegroundDrawPanel(SpriteService spriteService) {
		super();
		this.spriteService = spriteService;
	}

	@Override
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
			g2.drawImage(pf.getSubimage(0, 0, EditorConstante.LARGE_GRID_SIZE_X, EditorConstante.LARGE_GRID_SIZE_Y),
					idxColumn * EditorConstante.LARGE_GRID_SIZE_X, idxRow * EditorConstante.LARGE_GRID_SIZE_Y, null);
			idxColumn++;
			idx++;
			if (idx != 0 && idx % EditorConstante.NB_COLUMN_DRAW_FOREGROUND == 0) {
				idxRow++;
				idxColumn = 0;
			}
		}
		g2.dispose();
		bs.show();
	}
}