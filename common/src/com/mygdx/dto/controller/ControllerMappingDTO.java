package com.mygdx.dto.controller;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMappingDTO implements Serializable {

	private static final long serialVersionUID = -6005904594146827849L;

	private String name;
	private List<ButtonMappingDTO> buttons;
	private List<AxisMappingDTO> axis;

}
