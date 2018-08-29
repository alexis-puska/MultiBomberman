package com.mygdx.service.dto.level;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrolleyDTO implements Serializable {

	private static final long serialVersionUID = -2962754568139006753L;
	private int x;
	private int y;

}
