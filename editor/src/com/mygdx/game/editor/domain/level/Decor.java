package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Decor extends Identifiable implements Serializable {

	private static final long serialVersionUID = -3148349064427411770L;
	private int x;
	private int y;
	private boolean enable;
	private boolean back;
	private int indexAnim;

	public Decor(int id, boolean enable, int x, int y, boolean back, int indexAnim) {
		super(id);
		this.x = x;
		this.y = y;
		this.enable = enable;
		this.back = back;
		this.indexAnim = indexAnim;
	}
}
