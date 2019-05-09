package com.mygdx.service.network.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WaitScreenDTO implements Serializable {

	private static final long serialVersionUID = 2627356807347045805L;
	public int nbHumainPlayer;
	public int nbClient;
}
