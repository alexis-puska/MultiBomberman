package com.mygdx.service.network.dto;

import java.io.Serializable;

import com.mygdx.enumeration.TimeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RuleScreenDTO implements Serializable {

	private static final long serialVersionUID = -890858808687751648L;

	private boolean suddenDeath;
	private boolean badBomber;
	private TimeEnum time;
	private int iaLevel;
}
