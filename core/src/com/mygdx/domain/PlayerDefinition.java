package com.mygdx.domain;

import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.PlayerTypeEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

	public void incPlayerType(boolean network) {
		this.playerType = PlayerTypeEnum.next(this.playerType, network);
	}

	public void decPlayerType(boolean network) {
		this.playerType = PlayerTypeEnum.previous(this.playerType, network);
	}

	public void incCharacter() {
		this.character = CharacterEnum.next(this.character);
	}

	public void decCharacter() {
		this.character = CharacterEnum.previous(this.character);
	}

	public void incCharacterColor() {
		this.color = CharacterColorEnum.next(this.color);
	}

	public void decCharacterColor() {
		this.color = CharacterColorEnum.previous(this.color);
	}

}
