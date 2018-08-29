package com.mygdx.service.dto.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomTextureDTO implements Serializable {

	private static final long serialVersionUID = 3132951962050727916L;

	private boolean front;
	private int x;
	private int y;
	private String textureName;
	private int textureIndex;
}
