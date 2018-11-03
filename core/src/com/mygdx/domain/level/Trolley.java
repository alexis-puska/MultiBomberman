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
public class Trolley extends BodyAble {

	protected int x;
	protected int y;

	@Override
	public void createBody() {
		// make
	}

	@Override
	public void drawIt() {
		// make
	}

	public boolean isDestroyed() {
		return false;
	}

}
