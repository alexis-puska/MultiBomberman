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
public class ControllerMappingFileDTO implements Serializable {

	private static final long serialVersionUID = -4024181076628502522L;

	private List<ControllerMappingDTO> controllers;

}
