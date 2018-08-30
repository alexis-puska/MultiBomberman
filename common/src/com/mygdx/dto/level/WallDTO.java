package com.mygdx.dto.level;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WallDTO implements Serializable {

	private static final long serialVersionUID = -6820274884896875480L;

	private int x;
	private int y;
	private boolean draw;
	private String textureName;
	private int textureIndex;

}
