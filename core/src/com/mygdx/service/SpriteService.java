package com.mygdx.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.sprite.CharacterColorFile;
import com.mygdx.dto.sprite.CharacterFile;
import com.mygdx.dto.sprite.CharacterSpriteFile;
import com.mygdx.dto.sprite.ColorMapFile;
import com.mygdx.dto.sprite.LouisColorFile;
import com.mygdx.dto.sprite.LouisSpriteFile;
import com.mygdx.dto.sprite.Sprite;
import com.mygdx.dto.sprite.SpriteCharacter;
import com.mygdx.dto.sprite.SpriteFile;
import com.mygdx.dto.sprite.SpriteFileContent;
import com.mygdx.dto.sprite.SpriteLouis;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.CharacterSpriteEnum;
import com.mygdx.enumeration.LouisColorEnum;
import com.mygdx.enumeration.LouisSpriteEnum;
import com.mygdx.enumeration.SpriteEnum;

/**
 * Read sprite file and build structure
 * 
 * @author alexispuskarczyk
 */
public class SpriteService {

	private static final String SPRITE_SERVICE = "Sprite Service";
	private static final String SPRITE_JSON_FILE = "json/sprite.json";

	private static SpriteService instance = new SpriteService();

	private Map<SpriteEnum, TextureRegion[]> sprites;
	private Map<CharacterEnum, Map<CharacterColorEnum, Map<CharacterSpriteEnum, TextureRegion[]>>> spritesPlayer;
	private Map<LouisColorEnum, Map<LouisSpriteEnum, TextureRegion[]>> spritesLouis;
	private FileHandle spriteJsonFile;
	private final ObjectMapper objectMapper;

	private List<CharacterColorFile> characterColors;
	private List<LouisColorFile> louisColors;

	/************************
	 * --- Texture ---
	 ************************/

	public static SpriteService getInstance() {
		return instance;
	}

	public SpriteService() {
		Gdx.app.log(SPRITE_SERVICE, "Init");
		sprites = new HashMap<>();
		spritesLouis = new HashMap<>();
		spritesPlayer = new HashMap<>();
		objectMapper = new ObjectMapper();
		spriteJsonFile = Gdx.files.internal(SPRITE_JSON_FILE);
		SpriteFileContent spriteFileContent = null;
		try {
			spriteFileContent = objectMapper.readValue(spriteJsonFile.read(), SpriteFileContent.class);
		} catch (JsonParseException e) {
			Gdx.app.error(SPRITE_SERVICE, "JsonParseException : ", e);
		} catch (JsonMappingException e) {
			Gdx.app.error(SPRITE_SERVICE, "JsonMappingException : ", e);
		} catch (IOException e) {
			Gdx.app.error(SPRITE_SERVICE, "IOException : ", e);
		}
		this.characterColors = spriteFileContent.getCharactersColors();
		this.louisColors = spriteFileContent.getLouisColors();

		int nbSprite = 0;
		int nbSpriteFile = 0;

		List<SpriteFile> spriteFileList = spriteFileContent.getSpriteFile();
		for (SpriteFile spriteFile : spriteFileList) {
			List<String> filesName = spriteFile.getFiles();
			for (String filename : filesName) {
				Texture texture = new Texture(Gdx.files.internal(filename));
				List<Sprite> area = spriteFile.getArea();
				nbSpriteFile = 0;
				for (Sprite sprite : area) {
					int idx = 0;
					SpriteEnum animation = sprite.getAnimation();
					TextureRegion[] regions = new TextureRegion[sprite.getN()];
					for (int l = 0; l < sprite.getNy(); l++) {
						for (int k = 0; k < sprite.getNx(); k++) {
							regions[idx] = new TextureRegion(texture, sprite.getX() + (k * sprite.getSx()),
									sprite.getY() + (l * sprite.getSy()), sprite.getSx(), sprite.getSy());
							idx++;
							nbSprite++;
							nbSpriteFile++;
							if (idx >= sprite.getN()) {
								break;
							}
						}
					}
					if (sprites.containsKey(animation)) {
						sprites.put(animation, mergeTextureRegion(sprites.get(animation), regions));
					} else {
						sprites.put(animation, regions);
					}
				}
				Gdx.app.log("SpriteService", "Nb sprites loaded for : " + filename + " " + nbSpriteFile);
			}
		}
		Gdx.app.log("SpriteService", "Nb sprites loaded : " + nbSprite);

		
		nbSprite = 0;
		nbSpriteFile = 0;
		LouisSpriteFile louisSprite = spriteFileContent.getLouis();
		List<String> filesName = louisSprite.getFiles();
		for (String filename : filesName) {

			List<SpriteLouis> area = louisSprite.getArea();
			nbSpriteFile = 0;
			LouisColorEnum[] colors = LouisColorEnum.values();
			int i = 0;
			for (i = 0; i < colors.length; i++) {
				Map<LouisSpriteEnum, TextureRegion[]> LouisMap = new HashMap<>();
				Texture texture;
				if (colors[i] == LouisColorEnum.NONE) {
					texture = new Texture(Gdx.files.internal(filename));
				} else {
					texture = changeLouisColorTexture(new Texture(Gdx.files.internal(filename)), colors[i]);
				}
				for (SpriteLouis sprite : area) {
					int idx = 0;
					LouisSpriteEnum animation = sprite.getAnimation();
					TextureRegion[] regions = new TextureRegion[sprite.getN()];
					for (int l = 0; l < sprite.getNy(); l++) {
						for (int k = 0; k < sprite.getNx(); k++) {
							regions[idx] = new TextureRegion(texture, sprite.getX() + (k * sprite.getSx()),
									sprite.getY() + (l * sprite.getSy()), sprite.getSx(), sprite.getSy());
							idx++;
							nbSprite++;
							nbSpriteFile++;
							if (idx >= sprite.getN()) {
								break;
							}
						}
					}
					if (LouisMap.containsKey(animation)) {
						LouisMap.put(animation, mergeTextureRegion(LouisMap.get(animation), regions));
					} else {
						LouisMap.put(animation, regions);
					}
				}
				spritesLouis.put(colors[i], LouisMap);
			}
			Gdx.app.log("SpriteService", "Nb sprites loaded for : " + filename + " " + nbSpriteFile);
		}
		Gdx.app.log("SpriteService", "Nb sprites loaded : " + nbSprite);

		
		nbSprite = 0;
		nbSpriteFile = 0;
		CharacterSpriteFile characterSprite = spriteFileContent.getCharacter();
		List<CharacterFile> characterFiles = characterSprite.getFiles();
		for (CharacterFile characterFile : characterFiles) {
			CharacterEnum characterEnum = characterFile.getCharacter();
			nbSpriteFile = 0;
			CharacterColorEnum[] colors = CharacterColorEnum.values();
			Map<CharacterColorEnum, Map<CharacterSpriteEnum, TextureRegion[]>> characterColorMap = new HashMap<>();
			int i = 0;
			for (i = 0; i < colors.length; i++) {
				Map<CharacterSpriteEnum, TextureRegion[]> characterMap = new HashMap<>();
				Texture texture;
				if (colors[i] == CharacterColorEnum.NONE) {
					texture = new Texture(Gdx.files.internal(characterFile.getFile()));
				} else {
					texture = changeCharacterColorTexture(new Texture(Gdx.files.internal(characterFile.getFile())), colors[i]);
				}
				for (SpriteCharacter sprite : characterSprite.getArea()) {
					int idx = 0;
					CharacterSpriteEnum animation = sprite.getAnimation();
					TextureRegion[] regions = new TextureRegion[sprite.getN()];
					for (int l = 0; l < sprite.getNy(); l++) {
						for (int k = 0; k < sprite.getNx(); k++) {
							regions[idx] = new TextureRegion(texture, sprite.getX() + (k * sprite.getSx()),
									sprite.getY() + (l * sprite.getSy()), sprite.getSx(), sprite.getSy());
							idx++;
							nbSprite++;
							nbSpriteFile++;
							if (idx >= sprite.getN()) {
								break;
							}
						}
					}
					if (characterMap.containsKey(animation)) {
						characterMap.put(animation, mergeTextureRegion(characterMap.get(animation), regions));
					} else {
						characterMap.put(animation, regions);
					}
				}
				characterColorMap.put(colors[i], characterMap);
			}
			spritesPlayer.put(characterEnum, characterColorMap);
			
		}
		Gdx.app.log("SpriteService", "Nb sprites loaded : " + nbSprite);

		
		
		
		
		
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

	public TextureRegion getTexture(SpriteEnum spriteEnum, int idx) {
		TextureRegion[] t = sprites.get(spriteEnum);
		return t[idx];
	}
	
	public TextureRegion getLouisTexture(LouisSpriteEnum spriteEnum, LouisColorEnum colorEnum, int idx) {
		TextureRegion[] t = spritesLouis.get(colorEnum).get(spriteEnum);
		return t[idx];
	}
	
	public TextureRegion getPlayerTexture(CharacterSpriteEnum spriteEnum, CharacterColorEnum colorEnum, CharacterEnum characterEnum, int idx) {
		TextureRegion[] t = spritesPlayer.get(characterEnum).get(colorEnum).get(spriteEnum);
		return t[idx];
	}

	public int getAnimationSize(SpriteEnum spriteEnum) {
		TextureRegion[] t = sprites.get(spriteEnum);
		return t.length;
	}

	public TextureRegion[] getTexture(SpriteEnum spriteEnum) {
		return sprites.get(spriteEnum);
	}

	public Texture changeCharacterColorTexture(Texture img, CharacterColorEnum color) {
		if (!img.getTextureData().isPrepared()) {
			img.getTextureData().prepare();
		}
		Pixmap pixmap = img.getTextureData().consumePixmap();
		this.characterColors.stream().forEach(characterColor -> {
			if (characterColor.getColor() == color) {
				for (ColorMapFile change : characterColor.getChange()) {
					Long val = Long.decode(change.getOldColor());
					Long newVal = Long.decode(change.getNewColor());
					for (int x = 0; x < pixmap.getWidth(); x++) {
						for (int y = 0; y < pixmap.getHeight(); y++) {
							long pixelColor = pixmap.getPixel(x, y);
							if (pixelColor == val.intValue()) {
								pixmap.setColor(newVal.intValue());
								pixmap.drawPixel(x, y);
							}
						}
					}
				}
			}
		});
		Texture tex = new Texture(pixmap);
		pixmap.dispose();
		return tex;
	}

	public Texture changeLouisColorTexture(Texture img, LouisColorEnum color) {
		if (!img.getTextureData().isPrepared()) {
			img.getTextureData().prepare();
		}
		Pixmap pixmap = img.getTextureData().consumePixmap();
		this.louisColors.stream().forEach(louisColor -> {
			if (louisColor.getColor() == color) {
				for (ColorMapFile change : louisColor.getChange()) {
					Long val = Long.decode(change.getOldColor());
					Long newVal = Long.decode(change.getNewColor());
					for (int x = 0; x < pixmap.getWidth(); x++) {
						for (int y = 0; y < pixmap.getHeight(); y++) {
							long pixelColor = pixmap.getPixel(x, y);
							if (pixelColor == val.intValue()) {
								pixmap.setColor(newVal.intValue());
								pixmap.drawPixel(x, y);
							}
						}
					}
				}
			}
		});
		Texture tex = new Texture(pixmap);
		pixmap.dispose();
		return tex;
	}

	public void printPixelInsideTexture(Pixmap pixmap) {
		Set<Integer> values = new HashSet<>();
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				values.add(pixmap.getPixel(x, y));
			}
		}
		System.out.println("values : ");
		values.stream().forEach(v -> System.out.println(String.format("0x%08X", v)));
	}
	/*
	 * #define nbShadowAreaSprite 4
	 * 
	 * #define nbFrameWater 2 #define nbSpritePlayerX 9 #define nbSpritePlayerY 7
	 * #define spritePlayerSizeWidth 30 #define spritePlayerSizeHeight 42 #define
	 * nbColorPlayer 7 //#define nbPlayer 16 #define nbTypePlayer 8
	 * 
	 * #define nbSpriteLouisX 5 #define nbSpriteLouisY 7 #define
	 * spriteLouisSizeWidth 30 #define spriteLouisSizeHeight 42 #define nbTypeLouis
	 * 5
	 * 
	 * #define nbLevel 11 #define defaultSpriteSize 16
	 * 
	 * #define nbFireSpriteX 4 #define nbFireSpriteY 9 #define
	 * smallSpriteFireSizeWidth 18 #define smallSpriteFireSizeHeight 16
	 * 
	 * #define nbBombeSpriteX 3 #define nbBombeSpriteY 4 #define nbBonusSpriteX 2
	 * #define nbBonusSpriteY 8
	 * 
	 * #define nbPopBonusSpriteX 4 #define nbPopBonusSpriteY 1 #define
	 * popBonusSpriteWidth 18 #define popBonusSpriteHeight 16
	 * 
	 * #define nbBurnBonusSpriteX 8 #define nbBurnBonusSpriteY 1 #define
	 * burnBonusSpritewidth 30 #define burnBonusSpriteHeight 42
	 * 
	 * #define levelPreviewSizeWidth 128 #define levelPreviewSizeHeight 110 #define
	 * nbLevelPreviewX 3 #define nbLevelPreviewY 4
	 * 
	 * 
	 * #define nbSmallSpriteLevelX 5 #define nbSmallSpriteLevelY 8 #define
	 * nbLargeSpriteLevelX 1 #define nbLargeSpriteLevelY 2 #define
	 * smallSpriteLevelSizeWidth 18 #define smallSpriteLevelSizeHeight 16 #define
	 * largeSpriteLevelSizeWidth 54 #define largeSpriteLevelSizeHeight 48 #define
	 * burnWallStartSprite 22 #define suddenDeathWallSpriteIndex 16 #define
	 * wallSpriteIndex 16 #define skyStartSpriteIndex 40
	 * 
	 * 
	 * #define nbTrolleySpriteX 4 #define nbTrolleySpriteY 1 #define
	 * trolleySpriteSizeWidth 30 #define trolleySpriteSizeHeight 42
	 * 
	 * #define nbSpaceShipSpriteX 2 #define nbSpaceShipSpriteY 4 #define
	 * spaceShipSpriteSizeWidth 30 #define spaceShipSpriteSizeHeight 42
	 * 
	 * #define nbSpriteMineX 13 #define nbSpriteMineY 1 #define nbTeleporterSpriteX
	 * 4 #define nbTeleporterSpriteY 1 #define nbHoleX 2 #define nbHoleY 1 #define
	 * nbWaterOverlayX 2 #define nbWaterOverlayY 1 #define nbRailX 8 #define nbRailY
	 * 1 #define nbButtonX 2 #define nbButtonY 1
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
