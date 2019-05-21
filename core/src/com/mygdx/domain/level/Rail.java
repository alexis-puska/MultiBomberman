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

	private SpriteEnum drawSprite;
	private int drawIndex;

	public void init(MultiBombermanGame mbGame, final Level level) {
		this.drawSprite = SpriteEnum.RAIL;
		this.drawIndex = 0;
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
					|| (r.getY() == Constante.GRID_SIZE_Y && this.y == 0)) {
				// UP
				this.up = r;
				nearest += 0b0000000000000010;
			} else if (r.getY() == this.y && (r.getX() == this.x + 1)
					|| (r.getX() == 0 && this.x == Constante.GRID_SIZE_X)) {
				// RIGHT
				this.right = r;
				nearest += 0b0000000000000100;
			} else if (r.getX() == this.x && (r.getY() == this.y - 1)
					|| (r.getY() == 0 && this.y == Constante.GRID_SIZE_Y)) {
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

	public int getIndex() {
		return this.x + (this.y * Constante.GRID_SIZE_X);
	}

	public int getGridIndex() {
		return (y * Constante.GRID_SIZE_X) + x;
	}

	@Override
	public void drawIt() {
		this.drawIndex = this.type.getTextureIndex();
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
		switch (this.type) {
		case START_LEFT:
			if (previous == null) {
				return PovDirection.east;
			} else {
				return PovDirection.center;
			}
		case START_RIGHT:
			if (previous == null) {
				return PovDirection.west;
			} else {
				return PovDirection.center;
			}
		case HORIZONTAL:
			if (previous.equals(left)) {
				return PovDirection.east;
			} else if (previous.equals(right)) {
				return PovDirection.west;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.east;
				} else {
					return PovDirection.west;
				}
			}
		case VERTICAL:
			if (previous.equals(up)) {
				return PovDirection.south;
			} else if (previous.equals(down)) {
				return PovDirection.north;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.south;
				} else {
					return PovDirection.north;
				}
			}
		case DOWN_TO_RIGHT:
			if (previous.equals(right)) {
				return PovDirection.south;
			} else if (previous.equals(down)) {
				return PovDirection.east;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.south;
				} else {
					return PovDirection.east;
				}
			}
		case LEFT_TO_DOWN:
			if (previous.equals(left)) {
				return PovDirection.south;
			} else if (previous.equals(down)) {
				return PovDirection.west;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.south;
				} else {
					return PovDirection.west;
				}
			}
		case LEFT_TO_UP:
			if (previous.equals(up)) {
				return PovDirection.west;
			} else if (previous.equals(left)) {
				return PovDirection.north;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.west;
				} else {
					return PovDirection.north;
				}
			}
		case UP_TO_RIGHT:
			if (previous.equals(up)) {
				return PovDirection.east;
			} else if (previous.equals(right)) {
				return PovDirection.north;
			} else {
				if (ThreadLocalRandom.current().nextInt(2) == 0) {
					return PovDirection.east;
				} else {
					return PovDirection.north;
				}
			}
		default:
		}
		return PovDirection.center;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (canSwitch ? 1231 : 1237);
		result = prime * result + ((down == null) ? 0 : down.hashCode());
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + nearest;
		result = prime * result + ((possibility == null) ? 0 : possibility.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((up == null) ? 0 : up.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rail other = (Rail) obj;
		if (canSwitch != other.canSwitch)
			return false;
		if (down == null) {
			if (other.down != null)
				return false;
		} else if (!down.equals(other.down)) {
			return false;
		}
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (nearest != other.nearest)
			return false;
		if (possibility == null) {
			if (other.possibility != null)
				return false;
		} else if (!possibility.equals(other.possibility)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (up == null) {
			if (other.up != null) {
				return false;
			}
		} else if (!up.equals(other.up)) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		return y != other.y;
	}

}
