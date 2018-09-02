package com.mygdx.utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.service.SpriteService;

public class DrawUtils {

	public static void fillBackground(SpriteBatch batch, SpriteEnum spriteEnum) {
		TextureRegion textureRegionBackground = SpriteService.getInstance().getTexture(spriteEnum, 0);
		int idx = 0;
		int idy = 0;
		while (idy < Constante.SCREEN_SIZE_Y) {
			while (idx < Constante.SCREEN_SIZE_X) {
				batch.draw(textureRegionBackground, idx, idy);
				idx += textureRegionBackground.getRegionWidth();
			}
			idy += textureRegionBackground.getRegionHeight();
			idx = 0;
		}
	}

	private DrawUtils() {
		//empty private constructor
	}
}
