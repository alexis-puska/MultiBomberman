package com.mygdx.dto.controller;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ControllerMappingDTO implements Serializable {

	private static final long serialVersionUID = -6005904594146827849L;

	private String name;
	private List<ButtonMappingDTO> buttons;
	private List<AxisMappingDTO> axis;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ButtonMappingDTO> getButtons() {
		return buttons;
	}

	public void setButtons(List<ButtonMappingDTO> buttons) {
		this.buttons = buttons;
	}

	public List<AxisMappingDTO> getAxis() {
		return axis;
	}

	public void setAxis(List<AxisMappingDTO> axis) {
		this.axis = axis;
	}
}
