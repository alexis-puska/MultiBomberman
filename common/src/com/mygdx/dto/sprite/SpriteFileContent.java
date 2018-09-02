package com.mygdx.dto.sprite;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpriteFileContent implements Serializable {

	private static final long serialVersionUID = -5410889236338101940L;

	@JsonProperty("spriteFile")
	private List<SpriteFile> spriteFile;
	private CharacterSpriteFile character;
	private LouisSpriteFile louis;
	private List<CharacterColorFile> charactersColors;
	private List<LouisColorFile> louisColors;
}
