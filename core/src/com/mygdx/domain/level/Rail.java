package com.mygdx.domain.level;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.constante.Constante;
import com.mygdx.domain.common.Drawable;
import com.mygdx.domain.enumeration.RailEnum;
import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.SpriteService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rail extends Drawable {

	protected int x;
	protected int y;

	private RailEnum type;
	private boolean canSwitch;
	private List<RailEnum> possibility;
	private short nearest;

	private Rail left;
	private Rail up;
	private Rail right;
	private Rail down;

	public void init(MultiBombermanGame mbGame, final Level level) {
		this.mbGame = mbGame;
		this.canSwitch = false;
		this.possibility = new ArrayList<>();
		this.nearest = 0b0000000000000000;
		List<Rail> nearestRail = level.getRail().stream().filter(r -> (r.getX() == this.x - 1 && r.getY() == this.y)
				|| (r.getX() == this.x + 1 && r.getY() == this.y) || (r.getY() == this.y - 1 && r.getX() == this.x)
				|| (r.getY() == this.y + 1 && r.getX() == this.x)
				|| (this.x == 0 && r.getX() == Constante.GRID_SIZE_X && r.getY() == this.y)
				|| (this.x == Constante.GRID_SIZE_X && r.getX() == 0 && r.getY() == this.y)
				|| (this.y == 0 && r.getY() == Constante.GRID_SIZE_Y && r.getX() == this.x)
				|| (this.y == Constante.GRID_SIZE_Y && r.getY() == 0 && r.getX() == this.x))
				.collect(Collectors.toList());
		if (nearestRail.size() > 2) {
			canSwitch = true;
		}
		int p = 0;
		nearestRail.stream().forEach(r -> {
			if (r.getY() == this.y && (r.getX() == this.x - 1 || (r.getX() == Constante.GRID_SIZE_X && this.x == 0))) {
				// LEFT
				this.left = r;
				nearest += 0b0000000000000001;
			} else if (r.getX() == this.x && (r.getY() == this.y + 1)
					|| ((r.getY() == Constante.GRID_SIZE_Y && this.y == 0))) {
				// UP
				this.up = r;
				nearest += 0b0000000000000010;
			} else if (r.getY() == this.y && (r.getX() == this.x + 1)
					|| ((r.getX() == 0 && this.x == Constante.GRID_SIZE_X))) {
				// RIGHT
				this.right = r;
				nearest += 0b0000000000000100;
			} else if (r.getX() == this.x && (r.getY() == this.y - 1)
					|| ((r.getY() == 0 && this.y == Constante.GRID_SIZE_Y))) {
				// DOWN
				this.down = r;
				nearest += 0b0000000000001000;
			}
		});
		switch (nearest) {
		case 0b0001:
			this.type = RailEnum.START_RIGHT;
			// left
			break;
		case 0b0010:
			// up : not used
			break;
		case 0b0011:
			this.type = RailEnum.LEFT_TO_UP;
			break;
		case 0b0100:
			this.type = RailEnum.START_LEFT;
			// right
			break;
		case 0b0101:
			this.type = RailEnum.HORIZONTAL;
			break;
		case 0b0110:
			this.type = RailEnum.UP_TO_RIGHT;
			break;
		case 0b0111:
			// LUR
			possibility.add(RailEnum.LEFT_TO_UP);
			possibility.add(RailEnum.UP_TO_RIGHT);
			possibility.add(RailEnum.HORIZONTAL);
			p = ThreadLocalRandom.current().nextInt(0, possibility.size());
			this.type = possibility.get(p);
			break;
		case 0b1000:
			// down : not used
			break;
		case 0b1001:
			this.type = RailEnum.LEFT_TO_DOWN;
			break;
		case 0b1010:
			this.type = RailEnum.VERTICAL;
			break;
		case 0b1011:
			possibility.add(RailEnum.LEFT_TO_UP);
			possibility.add(RailEnum.LEFT_TO_DOWN);
			possibility.add(RailEnum.VERTICAL);
			p = ThreadLocalRandom.current().nextInt(0, possibility.size());
			this.type = possibility.get(p);
			// LUD
			break;
		case 0b1100:
			this.type = RailEnum.DOWN_TO_RIGHT;
			break;
		case 0b1101:
			possibility.add(RailEnum.DOWN_TO_RIGHT);
			possibility.add(RailEnum.LEFT_TO_DOWN);
			possibility.add(RailEnum.HORIZONTAL);
			p = ThreadLocalRandom.current().nextInt(0, possibility.size());
			this.type = possibility.get(p);
			// LRD
			break;
		case 0b1110:
			possibility.add(RailEnum.UP_TO_RIGHT);
			possibility.add(RailEnum.DOWN_TO_RIGHT);
			possibility.add(RailEnum.VERTICAL);
			p = ThreadLocalRandom.current().nextInt(0, possibility.size());
			this.type = possibility.get(p);
			// UDR
			break;
		default:
		case 0b1111:
			possibility.add(RailEnum.HORIZONTAL);
			possibility.add(RailEnum.VERTICAL);
			possibility.add(RailEnum.LEFT_TO_DOWN);
			possibility.add(RailEnum.LEFT_TO_UP);
			possibility.add(RailEnum.DOWN_TO_RIGHT);
			possibility.add(RailEnum.UP_TO_RIGHT);
			p = ThreadLocalRandom.current().nextInt(0, possibility.size());
			this.type = possibility.get(p);
			// CROSS
			break;
		}
	}

	@Override
	public void drawIt() {
		mbGame.getBatch().draw(SpriteService.getInstance().getSprite(SpriteEnum.RAIL, this.type.getTextureIndex()),
				this.x * Constante.GRID_PIXELS_SIZE_X, this.y * Constante.GRID_PIXELS_SIZE_Y);
	}

	public void switchRail() {
		if (canSwitch) {
			int p = ThreadLocalRandom.current().nextInt(1, possibility.size());
			int index = p + this.possibility.indexOf(this.type);
			if (index > possibility.size() - 1) {
				index -= possibility.size();
			}
			this.type = possibility.get(index);
		}
	}

	public PovDirection getNextDirection(Rail previous) {
		// si le rail en paramètre = null, le personnage rentre dans le wagon, retour
		// de la direction à prendre.
		if (previous == null) {
			if (this.getType() == RailEnum.START_LEFT) {
				return PovDirection.east;
			} else if (this.getType() == RailEnum.START_RIGHT) {
				return PovDirection.west;
			}
			return PovDirection.center;
		}
		// convertion rail en position précédente
		int val = 0b0000000000000000;
		if (previous.getY() == this.y
				&& (previous.getX() == this.x - 1 || (previous.getX() == Constante.GRID_SIZE_X && this.x == 0))) {
			val += 0b0000000000000001;
		} else if (previous.getX() == this.x
				&& (previous.getY() == this.y + 1 || (previous.getY() == 0 && this.y == Constante.GRID_SIZE_Y))) {
			val += 0b0000000000000010;
		} else if (previous.getY() == this.y
				&& (previous.getX() == this.x + 1 || (previous.getX() == 0 && this.x == Constante.GRID_SIZE_X))) {
			val += 0b0000000000000100;
		} else if (previous.getX() == this.x
				&& (previous.getY() == this.y - 1 || (previous.getY() == Constante.GRID_SIZE_Y && this.y == 0))) {
			val += 0b0000000000001000;
		}
		// si connecté à 2 rail, on retourne la direction du rail non précédent.
		if (possibility.size() == 2) {
			if ((this.type.getDirection() & 0b0000000000000001) > 0 && (0b0000000000000001 != val)) {
				return PovDirection.west;
			} else if ((this.type.getDirection() & 0b0000000000000010) > 0 && (0b0000000000000010 != val)) {
				return PovDirection.north;
			} else if ((this.type.getDirection() & 0b0000000000000100) > 0 && (0b0000000000000100 != val)) {
				return PovDirection.east;
			} else if ((this.type.getDirection() & 0b0000000000001000) > 0 && (0b0000000000001000 != val)) {
				return PovDirection.south;
			}
			// si possibilité = 1, on est sur une voie de garage
		} else if (possibility.size() == 1) {
			return PovDirection.center;
		} else {
			// sinon on arrive sur un aiguillage, si la direction fait partie de la positoin
			// de l'aiguillage, on retourne la position actuelle de l'aiguille
			List<PovDirection> dirs = new ArrayList<>();
			int dir = this.type.getDirection() - val;
			if (dir != this.type.getDirection()) {
				switch (dir) {
				case 0b0000000000000001:
					return PovDirection.west;
				case 0b0000000000000010:
					return PovDirection.north;
				case 0b0000000000000100:
					return PovDirection.east;
				default:
				case 0b0000000000001000:
					return PovDirection.south;
				}
			}
			// sinon tirage aléatoire de la position de l'aiguille.
			int choice = this.nearest - val;
			if ((choice & 0b0000000000000001) > 0) {
				dirs.add(PovDirection.west);
			}
			if ((choice & 0b0000000000000010) > 0) {
				dirs.add(PovDirection.north);
			}
			if ((choice & 0b0000000000000100) > 0) {
				dirs.add(PovDirection.east);
			}
			if ((choice & 0b0000000000001000) > 0) {
				dirs.add(PovDirection.south);
			}
			int p = ThreadLocalRandom.current().nextInt(0, dirs.size());
			return dirs.get(p);
		}
		return PovDirection.center;
	}

	public Rail getRail(PovDirection dir) {
		if (dir == PovDirection.west) {
			return left;
		} else if (dir == PovDirection.north) {
			return up;
		} else if (dir == PovDirection.east) {
			return right;
		} else if (dir == PovDirection.south) {
			return down;
		}
		return null;
	}
}
