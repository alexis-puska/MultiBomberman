package com.mygdx.service;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Read sprite file and build structure
 * 
 * @author alexispuskarczyk
 */
public class SpriteService {

	private static final String SPRITE_SERVICE = "Sprite Service";

	private static SpriteService instance = new SpriteService();

	private Map<String, TextureRegion[]> sprites;
	private Map<Integer, TextureRegion[]> spritesDecor;
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

	public TextureRegion getDecor(int index) {
		return spritesDecor.get(index)[0];
	}

	private Texture textureFromName(String name) {
		switch (name) {
		case "sprite_animation":
			return spriteAnimation;
		default:
			return spriteAnimation;
		}
	}
	
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
