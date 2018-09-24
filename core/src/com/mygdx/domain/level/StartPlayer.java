package com.mygdx.domain.level;

import com.mygdx.domain.common.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StartPlayer extends Identifiable {

	protected int x;
	protected int y;

}
