package com.mygdx.game.editor.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.mygdx.game.editor.constant.Constante;
import com.mygdx.game.editor.service.SpriteService;

public class PlatformDrawPanel extends Canvas {

	private static final long serialVersionUID = -617780220828076518L;

	private SpriteService spriteService;

	public PlatformDrawPanel(SpriteService spriteService) {
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
		g2.fillRect(0, 0, Constante.SCREEN_SIZE_X, Constante.SCREEN_SIZE_Y);
		Font font = new Font("Arial", Font.PLAIN, 20);
		g2.setFont(font);
		g2.setColor(Color.RED);
		int idx = 0;
		int nb = this.spriteService.getSpriteAnimationSize("platform");
		int nbColumn = nb / 25;
		if (nb % 25 != 0) {
			nbColumn++;
		}
		int idxColumn = 0;
		int idxRow = 0;
		int widthColumn = 300 / nbColumn;
		while (idx < nb) {
			BufferedImage pf = this.spriteService.getSprite("platform", idx);
			g2.drawImage(pf.getSubimage(0, 0, widthColumn, Constante.GRID_SIZE), idxColumn * widthColumn,
					idxRow * Constante.GRID_SIZE, null);
			g2.drawString(Integer.toString(idx), idxColumn * widthColumn, (idxRow + 1) * Constante.GRID_SIZE);
			idxRow++;
			idx++;
			if (idx != 0 && idx % 25 == 0) {
				idxColumn++;
				idxRow = 0;
			}
		}
		g2.dispose();
		bs.show();
	}
}