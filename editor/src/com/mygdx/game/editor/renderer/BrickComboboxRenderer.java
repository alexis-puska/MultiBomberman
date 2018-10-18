package com.mygdx.game.editor.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import com.mygdx.enumeration.SpriteEnum;
import com.mygdx.game.editor.enumeration.BrickBurnEnum;
import com.mygdx.game.editor.service.SpriteService;

public class BrickComboboxRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 2877683820303602466L;

	private final SpriteService spriteService;

	public BrickComboboxRenderer(SpriteService spriteService) {
		this.spriteService = spriteService;
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		BrickBurnEnum emp = (BrickBurnEnum) value;
		if (emp != null) {
			for (SpriteEnum val : SpriteEnum.values()) {
				if (val.toString().equals(emp.toString())) {
					this.setIcon(new ImageIcon(spriteService.getSprite(val, 0)));
					break;
				}
			}
		}
		return this;
	}
}
