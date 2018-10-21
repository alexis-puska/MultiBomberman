package com.mygdx.game.editor.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.editor.service.SpriteService;

public class PreviewComboboxRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 2877683820303602466L;

	private final SpriteService spriteService;

	public PreviewComboboxRenderer(SpriteService spriteService) {
		this.spriteService = spriteService;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			this.setIcon(
					new ImageIcon(spriteService.getSprite(SpriteEnum.PREVIEW, (int) value).getSubimage(0, 0, 120, 16)));
		}
		return this;
	}
}
