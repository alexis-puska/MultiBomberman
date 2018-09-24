package com.mygdx.dto.level;

import java.io.Serializable;

import com.mygdx.dto.level.common.PositionableDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WallDTO extends PositionableDTO implements Serializable {

	private static final long serialVersionUID = -6820274884896875480L;

	private boolean draw;
	private CustomTextureDTO texture;

}
