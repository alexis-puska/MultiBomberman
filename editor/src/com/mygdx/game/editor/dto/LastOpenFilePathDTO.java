package com.mygdx.game.editor.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastOpenFilePathDTO implements Serializable {

	private static final long serialVersionUID = -4940700800638801870L;
	
	private String path;

}
