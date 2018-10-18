package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WallDTO extends PositionableDTO implements Serializable {

	private static final long serialVersionUID = -6820274884896875480L;

	private boolean draw;
	private TextureDTO texture;

	public WallDTO(int x, int y, boolean draw, TextureDTO texture) {
		super(x, y);
		this.draw = draw;
		this.texture = texture;
	}

	public WallDTO(int x, int y) {
		super(x, y);
		this.draw = true;
		this.texture = null;
	}
}
