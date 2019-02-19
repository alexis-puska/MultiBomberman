package com.mygdx.domain.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.common.BodyAble;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SoundService;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teleporter extends BodyAble implements Initiable {

	protected int x;
	protected int y;
	private boolean animate;
	private boolean secondSoundPlayed;
	private float time;
	private Animation<TextureRegion> animation;

	@Override
	public void createBody() {
		BodyDef groundBodyDef = new BodyDef();
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(0.4f, 0.4f);
		groundBodyDef.position.set(new Vector2((float) this.x + 0.5f, (float) this.y + 0.5f));
		this.body = world.createBody(groundBodyDef);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setFriction(0f);
		fixture.setUserData(this);
		Filter filter = new Filter();
		filter.categoryBits = CollisionConstante.CATEGORY_TELEPORTER;
		filter.maskBits = CollisionConstante.CATEGORY_PLAYER_HITBOX;
		fixture.setFilterData(filter);
	}

	@Override
	public void init(World world, MultiBombermanGame mbGame, Level level) {
		this.mbGame = mbGame;
		this.world = world;
		this.level = level;
		this.time = 0.0f;
		this.animation = new Animation<>(1f / 10f,
				SpriteService.getInstance().getSpriteForAnimation(SpriteEnum.TELEPORTER));
		createBody();
	}

	public void animate(boolean playSound) {
		this.animate = true;
		this.secondSoundPlayed = !playSound;
		this.time = 0.0f;
		if (playSound) {
			SoundService.getInstance().playSound(SoundEnum.TELEPORTER_OPEN);
		}
	}

	public void update() {
		time += Gdx.graphics.getDeltaTime();
		if (animate) {
			if (animation.isAnimationFinished(time)) {
				animate = false;
			}
			if (time > 0.4f && !secondSoundPlayed) {
				SoundService.getInstance().playSound(SoundEnum.TELEPORTER_CLOSE);
				secondSoundPlayed = true;
			}
		}
	}

	@Override
	public void drawIt() {
		if (animate) {
			mbGame.getBatch().draw(animation.getKeyFrame(time, false), this.x * 18, this.y * 16);
		} else {
			mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.TELEPORTER, 0), this.x * 18,
					this.y * 16);
		}
	}
}
