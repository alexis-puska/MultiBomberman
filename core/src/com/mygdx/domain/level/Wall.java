package com.mygdx.domain.level;

import com.mygdx.domain.common.BodyAble;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wall extends BodyAble {

	protected int x;
	protected int y;
	private boolean draw;
	private boolean customSkin;
	private String animation;
	private int index;

	@Override
	public void drawIt() {
		// make

	}

	@Override
	public void createBody() {
		// make

	}

}
