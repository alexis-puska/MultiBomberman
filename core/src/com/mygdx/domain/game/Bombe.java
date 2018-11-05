package com.mygdx.domain.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.domain.enumeration.BombeTypeEnum;
import com.mygdx.domain.level.Level;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

public class Bombe extends BodyAble {

	private static final int NB_FRAME = 4;

	protected int x;
	protected int y;
	private int strenght;
	private BombeTypeEnum type;
	private Player player;
	private int countDown;
	private boolean exploded;

	private int frameCounter;
	private int offsetSprite;
	private int nbFrameForAnimation;
	private int offsetSpriteAnimation;

	public Bombe(Level level, World world, MultiBombermanGame mbGame, int strenght, int x, int y, BombeTypeEnum type,
			Player player, int countDown) {
		this.world = world;
		this.mbGame = mbGame;
		this.level = level;
		this.strenght = strenght;
		this.exploded = false;
		this.x = x;
		this.y = y;
		this.type = type;
		this.player = player;
		this.countDown = countDown;
		this.frameCounter = 0;
		this.offsetSprite = 0;
		this.nbFrameForAnimation = 4;
		createBody();
	}

	@Override
	protected void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		CircleShape groundBox = new CircleShape();
		groundBox.setRadius(0.5f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_FIRE;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(type.getSpriteEnum(), offsetSpriteAnimation),
				(float) this.x * 18, (float) this.y * 16);
	}

	@Override
	public void update() {
		if (frameCounter > NB_FRAME) {
			frameCounter = 0;
			offsetSprite++;
			if (offsetSprite >= nbFrameForAnimation) {
				offsetSprite = 0;
			}
		}
		frameCounter++;
		switch (offsetSprite) {
		case 0:
			offsetSpriteAnimation = 1;
			break;
		case 1:
			offsetSpriteAnimation = 0;
			break;
		case 2:
			offsetSpriteAnimation = 1;
			break;
		case 3:
		default:
			offsetSpriteAnimation = 2;
			break;
		}
		switch (type) {
		case BOMBE:
		case BOMBE_MAX:
		case BOMBE_RUBBER:
			countDown--;
			break;
		case BOMBE_P:
			if (countDown == 1) {
				countDown--;
			}
			break;
		default:
			break;
		}

		if (countDown == 0) {
			explode();
		}
	}

	private void explode() {
		for (int i = 0; i < 35; i++) {
			for (int j = 0; j < 21; j++) {
				if (this.x + i < 35 && this.y + j < 19
						&& this.level.getOccupedWallBrick()[this.x + i][this.y + j] != null) {
					this.level.getOccupedWallBrick()[this.x + i][this.y + j].action();
				}
			}
		}
		for (int i = 0; i < this.strenght; i++) {

		}
		for (int i = 0; i < this.strenght; i++) {

		}
		for (int i = 0; i < this.strenght; i++) {

		}
		exploded = true;
		this.player.bombeExploded();
	}

	public boolean isExploded() {
		return exploded;
	}
}
