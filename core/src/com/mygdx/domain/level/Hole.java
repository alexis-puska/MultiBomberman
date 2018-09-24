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
public class Hole extends BodyAble {

	protected int x;
	protected int y;

	@Override
	public void createBody() {
		// TODO Auto-generated method stub
	}

	@Override
	public void drawIt() {
		// TODO Auto-generated method stub
	}

}
