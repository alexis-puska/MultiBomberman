package com.mygdx.domain.level;

import com.mygdx.domain.common.Drawable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wall extends Drawable {

	protected int x;
	protected int y;
	private boolean draw;
	private String animation;
	private int index;

	@Override
	public void drawIt() {
		// TODO Auto-generated method stub

	}

}
