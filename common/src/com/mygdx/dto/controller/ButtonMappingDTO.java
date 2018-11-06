package com.mygdx.dto.controller;

import java.io.Serializable;

import com.mygdx.enumeration.ControllerButtonEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ButtonMappingDTO implements Serializable {

	private static final long serialVersionUID = 7449893437224785568L;

	private int button;
	private ControllerButtonEnum mapped;

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public ControllerButtonEnum getMapped() {
		return mapped;
	}

	public void setMapped(ControllerButtonEnum mapped) {
		this.mapped = mapped;
	}
}
