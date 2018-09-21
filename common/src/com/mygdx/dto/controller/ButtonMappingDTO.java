package com.mygdx.dto.controller;

import java.io.Serializable;

import com.mygdx.enumeration.ControllerButtonEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ButtonMappingDTO implements Serializable {

	private static final long serialVersionUID = 7449893437224785568L;

	private int button;
	private ControllerButtonEnum mapped;
}
