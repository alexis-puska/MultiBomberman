package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import com.mygdx.game.editor.constant.EnnemieTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ennemie extends Identifiable implements Serializable {

	private static final long serialVersionUID = -3148349064427411770L;
	private boolean enable;
	private int x;
	private int y;
	private EnnemieTypeEnum type;

	public Ennemie(int id, boolean enable, int x, int y, EnnemieTypeEnum type) {
		super(id);
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.type = type;
	}

}
