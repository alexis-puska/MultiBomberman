package com.mygdx.domain.level;

import com.mygdx.domain.common.Drawable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomTexture extends Drawable {

	protected int x;
	protected int y;
	protected String animation;
	protected int index;

	@Override
	public void drawIt() {
		// make

	}
}
