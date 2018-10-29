package com.mygdx.domain;

import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeTypeEnum;

public class Bombe extends BodyAble {

	protected int x;
	protected int y;
	private int strenght;
	private BombeTypeEnum type;
	private Player player;
	private int countDown;

	public Bombe(int strenght, int x, int y, BombeTypeEnum type, Player player, int countDown) {
		this.strenght = strenght;
		this.x = x;
		this.y = y;
		this.type = type;
		this.player = player;
		this.countDown = countDown;
	}

	@Override
	public void createBody() {
		
	}

	@Override
	public void drawIt() {
		// TODO Auto-generated method stub

	}

	public void update() {

	}
}
