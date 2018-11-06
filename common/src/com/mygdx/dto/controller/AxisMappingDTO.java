package com.mygdx.dto.controller;

import java.io.Serializable;

import com.mygdx.enumeration.ControllerAxisEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AxisMappingDTO implements Serializable {

	private static final long serialVersionUID = 7449893437224785568L;

	private ControllerAxisEnum type;
	private int axis;
	private boolean inverted;

	public ControllerAxisEnum getType() {
		return type;
	}

	public void setType(ControllerAxisEnum type) {
		this.type = type;
	}

	public int getAxis() {
		return axis;
	}

	public void setAxis(int axis) {
		this.axis = axis;
	}

	public boolean isInverted() {
		return inverted;
	}

	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}
}
