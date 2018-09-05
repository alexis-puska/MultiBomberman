package com.mygdx.dto.sprite;

import java.io.Serializable;
import java.util.List;

import com.mygdx.enumeration.CharacterColorEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterColorFile implements Serializable {

	private static final long serialVersionUID = -8772069448741457644L;

	private CharacterColorEnum color;
	private List<ColorMapFile> change;
	private String textColor;
}
