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

	public boolean suddenDeath;
	public boolean badBomber;
	public TimeEnum time;
	public int iaLevel;
}
