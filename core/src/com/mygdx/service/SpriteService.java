package com.mygdx.service;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.LouisColorEnum;
import com.mygdx.enumeration.LouisSpriteEnum;

/**
 * Read sprite file and build structure
 * 
 * @author alexispuskarczyk
 */
public class SpriteService {

	private static final String SPRITE_SERVICE = "Sprite Service";

	private static SpriteService instance = new SpriteService();

	private Map<String, TextureRegion[]> sprites;
	private Map<CharacterEnum,Map<CharacterColorEnum,Map<CharacterSpriteEnum, TextureRegion[]>>> spritesPlayer;
	private Map<LouisColorEnum,Map<LouisSpriteEnum, TextureRegion[]>> spritesLouis;
	private FileHandle spriteJsonFile;
	private final ObjectMapper objectMapper;

	/************************
	 * --- Texture ---
	 ************************/
	private Texture spriteAnimation;

	public static SpriteService getInstance() {
		return instance;
	}

	public SpriteService() {
		Gdx.app.log(SPRITE_SERVICE, "Init");
		objectMapper = new ObjectMapper();
		spriteAnimation = new Texture(Gdx.files.internal("sprite/sprite_animation.png"));

		Gdx.app.log("SpriteService", "Nb sprites loaded : " + 0);
	}

	private TextureRegion[] mergeTextureRegion(TextureRegion[] textureRegions, TextureRegion[] regions) {
		int size = textureRegions.length + regions.length;
		TextureRegion[] merged = new TextureRegion[size];
		int idx = 0;
		for (int i = 0; i < textureRegions.length; i++) {
			merged[idx] = textureRegions[i];
			idx++;
		}
		for (int i = 0; i < regions.length; i++) {
			merged[idx] = regions[i];
			idx++;
		}
		return merged;
	}

	public TextureRegion getTexture(String name, int idx) {
		TextureRegion[] t = sprites.get(name);
		return t[idx];
	}

	public int getAnimationSize(String name) {
		TextureRegion[] t = sprites.get(name);
		return t.length;
	}

	public TextureRegion[] getTexture(String name) {
		return sprites.get(name);
	}

	private Texture textureFromName(String name) {
		switch (name) {
		case "sprite_animation":
			return spriteAnimation;
		default:
			return spriteAnimation;
		}
	}
	
	
	/*
	#define nbShadowAreaSprite		4

	#define nbFrameWater			2
	#define nbSpritePlayerX			9
	#define nbSpritePlayerY			7
	#define spritePlayerSizeWidth		30
	#define spritePlayerSizeHeight	42
	#define nbColorPlayer			7
	//#define nbPlayer				16
	#define nbTypePlayer			8

	#define nbSpriteLouisX			5
	#define nbSpriteLouisY			7
	#define spriteLouisSizeWidth		30
	#define spriteLouisSizeHeight		42
	#define nbTypeLouis 			5

	#define nbLevel				11
	#define defaultSpriteSize		16

	#define nbFireSpriteX			4
	#define nbFireSpriteY			9
	#define smallSpriteFireSizeWidth	18
	#define smallSpriteFireSizeHeight	16

	#define nbBombeSpriteX			3
	#define nbBombeSpriteY			4
	#define nbBonusSpriteX			2
	#define nbBonusSpriteY			8

	#define nbPopBonusSpriteX			4
	#define nbPopBonusSpriteY			1
	#define popBonusSpriteWidth			18
	#define popBonusSpriteHeight		16

	#define nbBurnBonusSpriteX			8
	#define nbBurnBonusSpriteY			1
	#define burnBonusSpritewidth		30
	#define burnBonusSpriteHeight		42

	#define levelPreviewSizeWidth		128
	#define levelPreviewSizeHeight	110
	#define nbLevelPreviewX			3
	#define nbLevelPreviewY			4


	#define nbSmallSpriteLevelX		5
	#define nbSmallSpriteLevelY		8
	#define nbLargeSpriteLevelX		1
	#define nbLargeSpriteLevelY		2
	#define smallSpriteLevelSizeWidth	18
	#define smallSpriteLevelSizeHeight	16
	#define largeSpriteLevelSizeWidth	54
	#define largeSpriteLevelSizeHeight	48
	#define burnWallStartSprite 22
	#define suddenDeathWallSpriteIndex	16
	#define wallSpriteIndex				16
	#define skyStartSpriteIndex			40


	#define nbTrolleySpriteX		4
	#define nbTrolleySpriteY		1
	#define trolleySpriteSizeWidth	30
	#define trolleySpriteSizeHeight	42

	#define nbSpaceShipSpriteX		2
	#define nbSpaceShipSpriteY		4
	#define spaceShipSpriteSizeWidth	30
	#define spaceShipSpriteSizeHeight	42

	#define nbSpriteMineX			13
	#define nbSpriteMineY			1
	#define nbTeleporterSpriteX		4
	#define nbTeleporterSpriteY		1
	#define nbHoleX				2
	#define nbHoleY				1
	#define nbWaterOverlayX			2
	#define nbWaterOverlayY			1
	#define nbRailX				8
	#define nbRailY				1
	#define nbButtonX				2
	#define nbButtonY				1
	*/
//	{
//		"levels": [{
//				"id": 1,
//				"description": "",
//				"values": [{
//					"id": 1,
//					"description1": "",
//					"description2": "",
//					"start": [{
//						"id": 1,
//						"x": 1,
//						"y": 2
//					}],
//					"texture": [{
//						"id": 1,
//						"x": 1,
//						"y": 1
//					}],
//					"wall": [{
//						"id": 1,
//						"x": 1,
//						"y": 1
//					}]
//
//				}]
//			}
//
//		]
//	}

}
