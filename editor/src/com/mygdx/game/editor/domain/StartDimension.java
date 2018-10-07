package com.mygdx.game.editor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StartDimension {
	public final static int DOOR = 0;
	public final static int VORTEX = 1;

	int type;
	int originLevel;
	int destination;
}
