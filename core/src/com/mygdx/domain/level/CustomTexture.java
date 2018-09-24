package com.mygdx.domain.level;

import com.mygdx.domain.common.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomTexture extends Identifiable {

	protected int x;
	protected int y;
	private String animation;
	private int index;
}
