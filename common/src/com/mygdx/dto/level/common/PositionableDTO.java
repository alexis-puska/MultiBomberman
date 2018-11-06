package com.mygdx.dto.level.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PositionableDTO {
	private int x;
	private int y;

	public PositionableDTO(PositionableDTO original) {
		this.x = original.x;
		this.y = original.y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
