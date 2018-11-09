package com.mygdx.domain.level;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.main.MultiBombermanGame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trolley extends BodyAble implements Initiable {

	protected int x;
	protected int y;

	@Override
	public void createBody() {
		// make
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		createBody();
	}

	@Override
	public void drawIt() {
		// make
	}

	public boolean isDestroyed() {
		return false;
	}

	public boolean isMoving() {
		return false;
	}

	public void trolleyCollision() {
		// destroy the trolley, two trolley hurt each other and the two trolley move !
	}
}
