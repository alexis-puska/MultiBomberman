package com.mygdx.dto.level.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionableDTO extends IdentifiableDTO {
	private int x;
	private int y;
}
