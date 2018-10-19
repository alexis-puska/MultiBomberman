package com.mygdx.dto.level.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionableDTO {
	private int x;
	private int y;

	public PositionableDTO(PositionableDTO original) {
		this.x = original.x;
		this.y = original.y;
	}
}
