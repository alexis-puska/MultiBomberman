package com.mygdx.service.dto.level;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RailSwitchDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;

	private int x;
	private int y;
	
	private int next;
	private int previous;
	private int nextAlt;

}
