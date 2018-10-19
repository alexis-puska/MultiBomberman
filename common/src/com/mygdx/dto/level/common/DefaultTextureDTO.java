package com.mygdx.dto.level.common;

import java.io.Serializable;

import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
