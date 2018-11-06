package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;

import lombok.NoArgsConstructor;

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

	public WallDTO(WallDTO orignial) {
		super(orignial.getX(), orignial.getY());
		this.draw = orignial.draw;
		if (orignial.texture != null) {
			this.texture = new TextureDTO(orignial.texture);
		}
	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public TextureDTO getTexture() {
		return texture;
	}

	public void setTexture(TextureDTO texture) {
		this.texture = texture;
	}
}
