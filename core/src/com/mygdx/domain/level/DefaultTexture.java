package com.mygdx.domain.level;

import com.mygdx.enumeration.SpriteEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultTexture {

	private SpriteEnum animation;
	private int index;

}
