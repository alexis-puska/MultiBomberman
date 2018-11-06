package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TextureDTO implements Serializable {

	private static final long serialVersionUID = 3132951962050727916L;

	private SpriteEnum animation;
	private int index;

	public TextureDTO(TextureDTO orignial) {
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