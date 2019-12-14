package com.mygdx.ia;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.CollisionConstante;
import com.mygdx.domain.Player;
import com.mygdx.ia.enumeration.BrainStateEnum;

public class BrainLevel1 extends Brain {

	public BrainLevel1(Player player) {
		super(player, (short) CollisionConstante.CATEGORY_BOMBE,
				(short) (CollisionConstante.CATEGORY_WALL | CollisionConstante.CATEGORY_BRICKS));
		this.state = BrainStateEnum.FIND_BRICK;
	}

	public void think() {

		System.out.println("player " + this.player.getIndex() + " : " + this.state.name());

		if (!player.isDead()) {
			switch (state) {
			case DROP_BOMBE:
				this.player.pressA();
				this.state = BrainStateEnum.FIND_SECURE;
				break;
			case FIND_BOMBE:
				break;
			case FIND_BONUS:
				break;
			case FIND_BRICK:
				if (this.searchBrickToDetroy(true)) {
					this.state = BrainStateEnum.GO_TO_DROP_BOMBE;
				}
				break;
			case FIND_PLAYER:
				break;
			case FIND_SECURE:
				if (this.findSecurePlace(true)) {
					
					Gdx.app.log("BRAIN LEVEL 1 secure place found : ", "player "+player.getIndex()+" : "+this.player.getGridIndex() + " "+ this.scr.getSecuredCell());
					
					this.state = BrainStateEnum.GO_TO_SECURE;
				} else {
					this.state = BrainStateEnum.WAIT_TO_DEAD;
				}
				break;
			case GO_TO_BOMBE:
				break;
			case GO_TO_DROP_BOMBE:
				if (this.moveToObjectif()) {
					this.state = BrainStateEnum.DROP_BOMBE;
				}
				break;
			case GO_TO_NEAR_PLAYER:
				break;
			case GO_TO_SECURE:
				if (this.moveToObjectif()) {
					this.state = BrainStateEnum.WAIT_FOR_EXPLODE;
				}
				break;
			case GO_TO_TAKE_BONUS:
				break;
			case WAIT:
				break;
			case WAIT_AND_CHECK_SECURE:
				break;
			case WAIT_FOR_EXPLODE:
				switch (player.getBombeType()) {
				case BOMBE_P:
					this.player.pressB();
					break;
				case BOMBE:
				case BOMBE_MAX:
				case BOMBE_RUBBER:
				default:
					break;
				}
				if (this.bombeExploded) {
					this.state = BrainStateEnum.FIND_BRICK;
				}
				break;
			case WAIT_TO_DEAD:
				break;
			default:
				break;
			}
		}
	}
}
