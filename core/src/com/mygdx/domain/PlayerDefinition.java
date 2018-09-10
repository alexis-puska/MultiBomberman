package com.mygdx.domain;

import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.PlayerTypeEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDefinition {
	private int position;
	private CharacterColorEnum color;
	private CharacterEnum character;
	private PlayerTypeEnum playerType;

	public PlayerDefinition(int position, PlayerTypeEnum playerType) {
		this.position = position;
		this.playerType = playerType;
		this.color = CharacterColorEnum.random();
		this.character = CharacterEnum.random();
	}

}
