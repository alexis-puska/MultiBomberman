package com.mygdx.dto.controller;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ControllerMappingFileDTO implements Serializable {

	private static final long serialVersionUID = -4024181076628502522L;

	private List<ControllerMappingDTO> controllers;

	public List<ControllerMappingDTO> getControllers() {
		return controllers;
	}

	public void setControllers(List<ControllerMappingDTO> controllers) {
		this.controllers = controllers;
	}
}
