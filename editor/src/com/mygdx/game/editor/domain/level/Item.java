package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item extends Identifiable implements Serializable {

	private static final long serialVersionUID = -3148349064427411770L;
	private boolean enable;
	private int x;
	private int y;
	private int itemId;

	public Item(int id, boolean enable, int x, int y, int itemId) {
		super(id);
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.itemId = itemId;
	}

}
