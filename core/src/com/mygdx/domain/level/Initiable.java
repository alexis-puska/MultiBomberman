package com.mygdx.domain.level;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.main.MultiBombermanGame;

public interface Initiable {

	public void init(World world, MultiBombermanGame mbGame, Level level);
	
}
