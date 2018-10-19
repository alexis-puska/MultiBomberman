package com.mygdx.dto.level;

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
public class TextureDTO implements Serializable {

	private static final long serialVersionUID = 3132951962050727916L;

	private SpriteEnum animation;
	private int index;

	public TextureDTO(TextureDTO orignial) {
		this.animation = orignial.animation;
		this.index = orignial.index;
	}
}