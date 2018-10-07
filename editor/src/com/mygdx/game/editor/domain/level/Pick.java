package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pick extends Identifiable implements Serializable {

	private static final long serialVersionUID = -3148349064427411770L;
	private int x;
	private int y;
	private boolean enable;
	private int direction;

	public Pick(int id, boolean enable, int x, int y, int direction) {
		super(id);
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
