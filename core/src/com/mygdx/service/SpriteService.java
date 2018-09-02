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
	private Map<CharacterEnum, Map<CharacterColorEnum, Map<CharacterSpriteEnum, TextureRegion[]>>> playerSprites;
	private Map<LouisColorEnum, Map<LouisSpriteEnum, TextureRegion[]>> louisSprites;
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
		louisSprites = new HashMap<>();
		playerSprites = new HashMap<>();
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

		initSprite(spriteFileContent);
		initLouisSprite(spriteFileContent);
		initPlayerSprite(spriteFileContent);
	}

	/**
	 * Load common Sprite
	 * 
	 * @param spriteFileContent the json file read from application asset
	 */
	private void initSprite(SpriteFileContent spriteFileContent) {
		int nbSprite = 0;
		int nbSpriteFile = 0;
		// Load common sprite
		List<SpriteFile> spriteFiles = spriteFileContent.getSpriteFile();
		for (SpriteFile spriteFile : spriteFiles) {
			List<String> spritesFilename = spriteFile.getFiles();
			for (String spriteFilename : spritesFilename) {
				Texture texture = new Texture(Gdx.files.internal(spriteFilename));
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
				Gdx.app.log("SpriteService", "Nb sprites loaded for : " + spriteFilename + " " + nbSpriteFile);
			}
		}
		Gdx.app.log("SpriteService", "Nb sprites loaded : " + nbSprite);
	}

	/**
	 * Load Louis Sprite
	 * 
	 * @param spriteFileContent the json file read from application asset
	 */
	private void initLouisSprite(SpriteFileContent spriteFileContent) {
		int nbSprite = 0;
		int nbSpriteFile = 0;
		LouisSpriteFile louisSpriteFile = spriteFileContent.getLouis();
		List<String> louisFilenames = louisSpriteFile.getFiles();
		for (String louisFilename : louisFilenames) {
			List<SpriteLouis> area = louisSpriteFile.getArea();
			nbSpriteFile = 0;
			LouisColorEnum[] louisColors = LouisColorEnum.values();
			int i = 0;
			for (i = 0; i < louisColors.length; i++) {
				Map<LouisSpriteEnum, TextureRegion[]> louisSpriteTextureMap = new HashMap<>();
				Texture texture;
				if (louisColors[i] == LouisColorEnum.NONE) {
					texture = new Texture(Gdx.files.internal(louisFilename));
				} else {
					texture = changeLouisColorTexture(new Texture(Gdx.files.internal(louisFilename)), louisColors[i]);
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
					if (louisSpriteTextureMap.containsKey(animation)) {
						louisSpriteTextureMap.put(animation,
								mergeTextureRegion(louisSpriteTextureMap.get(animation), regions));
					} else {
						louisSpriteTextureMap.put(animation, regions);
					}
				}
				louisSprites.put(louisColors[i], louisSpriteTextureMap);
				Gdx.app.log("SpriteService",
						"sprites load for \"" + louisColors[i] + " player\" : " + nbSpriteFile + " sprites");
				nbSpriteFile = 0;
			}
			Gdx.app.log("SpriteService",
					"sprites load for \"Player\" : " + louisFilename + ", " + nbSprite + " sprites");
		}
	}

	/**
	 * Load Player Sprite
	 * 
	 * @param spriteFileContent the json file read from application asset
	 */
	private void initPlayerSprite(SpriteFileContent spriteFileContent) {
		int nbSprite = 0;
		CharacterSpriteFile characterSprite = spriteFileContent.getCharacter();
		List<CharacterFile> characterFiles = characterSprite.getFiles();
		for (CharacterFile characterFile : characterFiles) {
			CharacterEnum characterEnum = characterFile.getCharacter();
			int nbSpriteFile = 0;
			CharacterColorEnum[] characterColors = CharacterColorEnum.values();
			Map<CharacterColorEnum, Map<CharacterSpriteEnum, TextureRegion[]>> characterColorMap = new HashMap<>();
			int i = 0;
			for (i = 0; i < characterColors.length; i++) {
				Map<CharacterSpriteEnum, TextureRegion[]> characterMap = new HashMap<>();
				Texture texture;
				if (characterColors[i] == CharacterColorEnum.NONE) {
					texture = new Texture(Gdx.files.internal(characterFile.getFile()));
				} else {
					texture = changeCharacterColorTexture(new Texture(Gdx.files.internal(characterFile.getFile())),
							characterColors[i]);
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
				characterColorMap.put(characterColors[i], characterMap);
				playerSprites.put(characterEnum, characterColorMap);
				Gdx.app.log("SpriteService",
						"sprites load for \"" + characterColors[i] + " player\" : " + nbSpriteFile + " sprites");
				nbSpriteFile = 0;
			}
			Gdx.app.log("SpriteService",
					"sprites load for \"player\" : " + characterFile.getFile() + ", " + nbSprite + " sprites");
			nbSprite = 0;
		}
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

	public TextureRegion getSprite(SpriteEnum spriteEnum, int idx) {
		TextureRegion[] t = sprites.get(spriteEnum);
		return t[idx];
	}

	public TextureRegion getSprite(LouisSpriteEnum spriteEnum, LouisColorEnum colorEnum, int idx) {
		TextureRegion[] t = louisSprites.get(colorEnum).get(spriteEnum);
		return t[idx];
	}

	public TextureRegion getSprite(CharacterSpriteEnum spriteEnum, CharacterColorEnum colorEnum,
			CharacterEnum characterEnum, int idx) {
		TextureRegion[] t = playerSprites.get(characterEnum).get(colorEnum).get(spriteEnum);
		return t[idx];
	}

	public int getAnimationSize(SpriteEnum spriteEnum) {
		TextureRegion[] t = sprites.get(spriteEnum);
		return t.length;
	}

	public int getAnimationSize(CharacterSpriteEnum characterSpriteEnum) {
		TextureRegion[] t = playerSprites.get(CharacterEnum.BOMBERMAN).get(CharacterColorEnum.BLUE).get(characterSpriteEnum);
		return t.length;
	}
	
	public int getAnimationSize(LouisSpriteEnum louisSpriteEnum) {
		TextureRegion[] t = louisSprites.get(LouisColorEnum.NONE).get(louisSpriteEnum);
		return t.length;
	}

	/**
	 * Change color for player texture
	 * @param img original texture
	 * @param color the desired color
	 * @return modified texture
	 */
	private Texture changeCharacterColorTexture(Texture img, CharacterColorEnum color) {
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

	/**
	 * Change color for Louis texture
	 * @param img original texture
	 * @param color the desired color
	 * @return modified texture
	 */
	private Texture changeLouisColorTexture(Texture img, LouisColorEnum color) {
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

	/**
	 * Print pixel values inside a pixmap texture
	 * @param pixmap the pixmap
	 */
	@SuppressWarnings("unused")
	private void printPixelInsideTexture(Pixmap pixmap) {
		Set<Integer> values = new HashSet<>();
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				values.add(pixmap.getPixel(x, y));
			}
		}
		System.out.println("values : ");
		values.stream().forEach(v -> System.out.println(String.format("0x%08X", v)));
	}
}
