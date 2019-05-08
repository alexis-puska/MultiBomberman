package com.mygdx.service.network.dto;

import java.io.Serializable;
import java.util.Map;

import com.mygdx.domain.PlayerDefinition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleScreenDTO implements Serializable {

	private static final long serialVersionUID = -890858808687751648L;
	private Map<Integer, PlayerDefinition> definitions;

}
