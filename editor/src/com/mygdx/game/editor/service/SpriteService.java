package com.mygdx.game.editor.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.dto.sprite.Sprite;
import com.mygdx.dto.sprite.SpriteFile;
import com.mygdx.dto.sprite.SpriteFileContent;
import com.mygdx.enumeration.SpriteEnum;

public class SpriteService {

	private final static Logger LOG = LogManager.getLogger(SpriteService.class);

	private Map<SpriteEnum, BufferedImage[]> sprites;
	private final ObjectMapper objectMapper;

	public SpriteService() {
		this.objectMapper = new ObjectMapper();
		sprites = new HashMap<>();
		LOG.info("Load Sprites : START");
		int n = initSprite();
		LOG.info("Load Sprites : DONE, {} sprites loaded", n);
	}

	public SpriteFileContent readJsonSpriteFile(InputStream in) {
		SpriteFileContent list = null;
		try {
			list = objectMapper.readValue(in, SpriteFileContent.class);
		} catch (JsonParseException e) {
			LOG.error("JsonParseException : " + e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error("JsonMappingException : " + e.getMessage());
		} catch (IOException e) {
			LOG.error("IOException : " + e.getMessage());
		}
		return list;
	}

	/**
	 * return a specific sprite
	 * 
	 * @param animation name of animation
	 * @param index     index of animation
	 * @return Buffered Image
	 */
	public BufferedImage getSprite(SpriteEnum animation, int index) {
		BufferedImage[] spritesAnimation = sprites.get(animation);
		return spritesAnimation[index];
	}

	public int getSpriteAnimationSize(SpriteEnum animation) {
		return sprites.get(animation).length;
	}

	/**
	 * parse json file and create buffer sprite in memory
	 */
	private int initSprite() {
		int nb = 0;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream in = classloader.getResourceAsStream("json/sprite.json");
			SpriteFileContent spriteFileContent = this.readJsonSpriteFile(in);
			BufferedImage temp = null;
			List<SpriteFile> spriteFiles = spriteFileContent.getSpriteFile();
			for (SpriteFile spriteFile : spriteFiles) {
				List<String> spritesFilename = spriteFile.getFiles();
				for (String spriteFilename : spritesFilename) {
					temp = ImageIO.read(classloader.getResourceAsStream(spriteFilename));
					List<Sprite> areas = spriteFile.getArea();
					for (Sprite area : areas) {
						BufferedImage[] sprite = new BufferedImage[area.getN()];
						int n = 0;
						for (int y = 0; y < area.getNy(); y++) {
							for (int x = 0; x < area.getNx(); x++) {
								if (n >= area.getN()) {
									break;
								}
								int xCalc = area.getX() + (x * area.getSx());
								int yCalc = area.getY() + (y * area.getSy());
								sprite[n] = temp.getSubimage(xCalc, yCalc, area.getSx(), area.getSy());
								n++;
								nb++;
							}
						}
						if (sprites.containsKey(area.getAnimation())) {
							BufferedImage merge[] = mergeBufferedImage(sprites.get(area.getAnimation()), sprite);
							sprites.put(area.getAnimation(), merge);
						} else {
							sprites.put(area.getAnimation(), sprite);
						}
					}
				}
			}
		} catch (IOException e) {
			LOG.error("IOException : {}", e.getMessage());
		}
		return nb;
	}

	/**
	 * Merge 2 table of buffered Image
	 * 
	 * @param bufferedImages Table of image
	 * @param sprite         Table of image
	 * @return Table of image merged
	 */
	private BufferedImage[] mergeBufferedImage(BufferedImage[] bufferedImages, BufferedImage[] sprite) {
		BufferedImage[] merged = new BufferedImage[bufferedImages.length + sprite.length];
		int index = 0;
		for (int i = 0; i < bufferedImages.length; i++) {
			merged[i] = bufferedImages[index];
			index++;
		}
		index = 0;
		for (int i = bufferedImages.length; i < bufferedImages.length + sprite.length; i++) {
			merged[i] = sprite[index];
			index++;
		}
		return merged;
	}

}
