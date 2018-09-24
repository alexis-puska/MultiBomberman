package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomTextureDTO extends PositionableDTO implements Serializable {

	private static final long serialVersionUID = 3132951962050727916L;

	private String animation;
	private int index;
}
