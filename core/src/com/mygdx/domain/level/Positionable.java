package com.mygdx.domain.level;

import com.mygdx.domain.common.Identifiable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Positionable extends Identifiable {

	protected int x;
	protected int y;

}
