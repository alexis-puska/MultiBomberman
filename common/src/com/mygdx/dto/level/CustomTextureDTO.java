package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.enumeration.SpriteEnum;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CustomTextureDTO extends PositionableDTO implements Serializable {

	private static final long serialVersionUID = 3132951962050727916L;

	private SpriteEnum animation;
	private int index;

	public CustomTextureDTO(int x, int y, SpriteEnum animation, int index) {
		super(x, y);
		this.animation = animation;
		this.index = index;
	}

	public CustomTextureDTO(CustomTextureDTO orignial) {
		super(orignial.getX(), orignial.getY());
		this.animation = orignial.animation;
		this.index = orignial.index;
	}

	public SpriteEnum getAnimation() {
		return animation;
	}

	public void setAnimation(SpriteEnum animation) {
		this.animation = animation;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
