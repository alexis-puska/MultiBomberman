package com.mygdx.service.network.dto;

import java.io.Serializable;

import com.mygdx.constante.Constante;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LevelScreenDTO implements Serializable {

	private static final long serialVersionUID = -890858808687751648L;
	public int bombe;
	public int strenght;
	public String group;
	public String name;
	public String description;
	public int indexPreview;
	public int[] bonus;

	public LevelScreenDTO() {
		this.bonus = new int[Constante.MAX_BONUS];
	}

	public void setBonus(int idx, int val) {
		this.bonus[idx] = val;
	}

}
