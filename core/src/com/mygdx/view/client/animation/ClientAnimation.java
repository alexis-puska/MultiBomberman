package com.mygdx.view.client.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.constante.Constante;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class ClientAnimation {

	private MultiBombermanGame mbGame;
	private SpriteEnum spriteEnum;
	private float animationTime;
	private Animation<TextureRegion> animation;
	private int x;
	private int y;

	public ClientAnimation(MultiBombermanGame mbGame, SpriteEnum spriteEnum, int gridIndex) {
		this.mbGame = mbGame;
		this.x = gridIndex % Constante.GRID_SIZE_X;
		this.y = (gridIndex - x) / Constante.GRID_SIZE_X;
		this.spriteEnum = spriteEnum;
		this.animationTime = 0f;
		TextureRegion[] sprite = SpriteService.getInstance().getSpriteForAnimation(spriteEnum);
		if (spriteEnum == SpriteEnum.LEVEL1 || spriteEnum == SpriteEnum.LEVEL2 || spriteEnum == SpriteEnum.LEVEL3
				|| spriteEnum == SpriteEnum.LEVEL4 || spriteEnum == SpriteEnum.LEVEL5 || spriteEnum == SpriteEnum.LEVEL6
				|| spriteEnum == SpriteEnum.LEVEL7 || spriteEnum == SpriteEnum.LEVEL8 || spriteEnum == SpriteEnum.LEVEL9
				|| spriteEnum == SpriteEnum.LEVEL10) {
			TextureRegion[] spriteAnimation = new TextureRegion[sprite.length - 1];
			for (int i = 1; i < sprite.length; i++) {
				spriteAnimation[i - 1] = sprite[i];
			}
			animation = new Animation<>(this.spriteEnum.getFrameAnimationTime(), spriteAnimation);
		} else {
			animation = new Animation<>(this.spriteEnum.getFrameAnimationTime(), sprite);
		}
	}

	public boolean canBeRemove() {
		return this.animationTime > this.animation.getAnimationDuration();
	}

	public void drawIt() {
		this.animationTime += Gdx.graphics.getDeltaTime();
		TextureRegion t = animation.getKeyFrame(animationTime, false);
		if (t.getRegionWidth() != Constante.GRID_PIXELS_SIZE_X) {
			mbGame.getBatch().draw(t,
					this.x * Constante.GRID_PIXELS_SIZE_X
							- ((t.getRegionWidth() - Constante.GRID_PIXELS_SIZE_X) / 2.0F),
					this.y * Constante.GRID_PIXELS_SIZE_Y);
		} else {
			mbGame.getBatch().draw(t, this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
		}
	}
}
