package com.mygdx.dto.level.common;

import java.io.Serializable;

import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class DefaultTextureDTO implements Serializable {

	private static final long serialVersionUID = 4557602529622087772L;

	private SpriteEnum animation;
	private int index;

	public DefaultTextureDTO(DefaultTextureDTO original) {
		this.animation = original.animation;
		this.index = original.index;
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
