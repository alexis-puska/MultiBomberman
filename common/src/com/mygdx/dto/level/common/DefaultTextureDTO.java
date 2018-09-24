package com.mygdx.dto.level.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultTextureDTO implements Serializable {

	private static final long serialVersionUID = 4557602529622087772L;

	private String animation;
	private int index;
}
