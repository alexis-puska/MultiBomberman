package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import com.mygdx.game.editor.constant.GameKeyEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lock extends Identifiable implements Serializable {

	private static final long serialVersionUID = -3148349064427411770L;
	private boolean enable;
	private int x;
	private int y;
	private GameKeyEnum key;

	public Lock(int id, boolean enable, int x, int y, GameKeyEnum key) {
		super(id);
		this.enable = enable;
		this.x = x;
		this.y = y;
		this.key = key;
	}

}
