package com.mygdx.dto.sprite;

import java.io.Serializable;
import java.util.List;

import com.mygdx.enumeration.LouisColorEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LouisColorFile implements Serializable {

	private static final long serialVersionUID = -8772069448741457644L;

	private LouisColorEnum color;
	private List<ColorMapFile> change;
}
