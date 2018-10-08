package com.mygdx.game.editor.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygdx.game.editor.domain.sprite.Sprite;
import com.mygdx.game.editor.domain.sprite.SpriteFile;
import com.mygdx.game.editor.domain.sprite.SpriteFileContent;

public class SpriteService {

	private final static Logger LOG = LogManager.getLogger(SpriteService.class);

	private final FileService fileService;
	private Map<String, BufferedImage[]> sprites;
	private Map<Integer, BufferedImage[]> spritesDecor;

	public SpriteService(FileService fileService) {
		this.fileService = fileService;
		sprites = new HashMap<>();
		spritesDecor = new HashMap<>();
		LOG.info("Load Sprites : START");
		initSprite();
		LOG.info("Load Sprites : DONE");
	}

	/**
	 * return a specific sprite
	 * 
	 * @param animation
	 *            name of animation
	 * @param index
	 *            index of animation
	 * @return Buffered Image
	 */
	public BufferedImage getSprite(String animation, int index) {
		BufferedImage[] spritesAnimation = sprites.get(animation);
		return spritesAnimation[index];
	}

	/**
	 * return a specific sprite
	 * 
	 * @param animation
	 *            name of animation
	 * @param index
	 *            index of animation
	 * @return Buffered Image
	 */
	public BufferedImage getDecor(int index) {
		BufferedImage[] spritesAnimation = spritesDecor.get(index);
		return spritesAnimation[0];
	}

	public int getSpriteAnimationSize(String animation) {
		return sprites.get(animation).length;
	}

	/**
	 * parse json file and create buffer sprite in memory
	 */
	private void initSprite() {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("json/sprite.json");
		
//		SpriteFileContent spriteFile = fileService.readJsonSpriteFile(in);
//		try {
//			BufferedImage temp = null;
//			for (SpriteFile file : spriteFile.getSpriteFile()) {
//				switch (file.getFile()) {
//				case "sprite_rayon_teleporter":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_rayon_teleporter.png"));
//					break;
//				case "sprite_animation":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_animation.png"));
//					break;
//				case "sprite_light":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_light.png"));
//					break;
//				case "sprite_level":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_level.png"));
//					break;
//				case "sprite_objets":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_objets.png"));
//					break;
//				case "sprite_ennemies":
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_ennemies.png"));
//					break;
//				default:
//					temp = ImageIO.read(this.getClass().getResourceAsStream("/sprite/sprite_ennemies.png"));
//					break;
//				}
//				for (Sprite area : file.getArea()) {
//					BufferedImage[] sprite = new BufferedImage[area.getN()];
//					int n = 0;
//					for (int y = 0; y < area.getNy(); y++) {
//						for (int x = 0; x < area.getNx(); x++) {
//
//							if (n >= area.getN()) {
//								break;
//							}
//							int xCalc = area.getX() + (x * area.getSx());
//							int yCalc = area.getY() + (y * area.getSy());
//							sprite[n] = temp.getSubimage(xCalc, yCalc, area.getSx(), area.getSy());
//							n++;
//						}
//					}
//					if (area.getGrp().equals("")) {
//						if (sprites.containsKey(area.getAnimation())) {
//							BufferedImage merge[] = mergeBufferedImage(sprites.get(area.getAnimation()), sprite);
//							sprites.put(area.getAnimation(), merge);
//						} else {
//							sprites.put(area.getAnimation(), sprite);
//						}
//					} else if (area.getGrp().equals("decor")) {
//						spritesDecor.put(spritesDecor.size(), sprite);
//					}
//				}
//			}
//		} catch (IOException e) {
//			LOG.info("IOException : " + e.getMessage());
//		}
	}

	/**
	 * Merge 2 table of buffered Image
	 * 
	 * @param bufferedImages
	 *            Table of image
	 * @param sprite
	 *            Table of image
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
