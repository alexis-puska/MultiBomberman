package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Vortex extends Identifiable implements Serializable {

	private static final long serialVersionUID = 3701911571588077040L;
	private int x;
	private int y;
	private double zoomX;
	private double zoomY;
	private boolean enable;
	private int destination;

	public Vortex(int id, boolean enable, int x, int y, double zoomX, double zoomY, int destination) {
		super(id);
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.zoomX = zoomX;
		this.zoomY = zoomY;
		this.destination = destination;
	}
}
