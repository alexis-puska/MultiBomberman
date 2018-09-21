package com.mygdx.dto.controller;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AxisMappingDTO implements Serializable {

	private static final long serialVersionUID = 7449893437224785568L;

	private int axis;
	private int mapped;
	private boolean inverted;
}
