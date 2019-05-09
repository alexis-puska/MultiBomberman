package com.mygdx.service.network.dto;

import java.io.Serializable;

import com.mygdx.constante.Constante;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelScreenDTO implements Serializable {

	private static final long serialVersionUID = -890858808687751648L;
	private int bombe;
	private int strenght;
	private String group;
	private String name;
	private String description;
	private int indexPreview;
	private int[] bonus;

	public LevelScreenDTO() {
		this.bonus = new int[Constante.MAX_BONUS];
	}

	public void setBonus(int idx, int val) {
		this.bonus[idx] = val;
	}

}
