package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;
import com.mygdx.enumeration.SpriteEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
