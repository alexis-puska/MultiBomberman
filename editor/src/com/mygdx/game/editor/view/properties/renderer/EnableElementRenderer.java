package com.mygdx.game.editor.view.properties.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.mygdx.game.editor.domain.level.event.EnableElement;

public class EnableElementRenderer extends JLabel implements ListCellRenderer<EnableElement> {

	private static final long serialVersionUID = -7756260545095706601L;

	public EnableElementRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends EnableElement> list, EnableElement enableElement,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());

			if (enableElement.getElementType() == null || enableElement.getId() < 0) {
				setIcon(new ImageIcon(getClass().getResource("/icon/warn.png")));
				setText("NOT CONFIGURED");
			} else {
				setIcon(new ImageIcon(getClass().getResource("/icon/ok.png")));
				setText((enableElement.isNewState() ? "ENABLE - "
						: "DISABLE - ") + enableElement.getElementType().name() + " " + enableElement.getId());
			}
		} else {
			setForeground(list.getForeground());
			if (enableElement.getElementType() == null || enableElement.getId() < 0) {
				setIcon(new ImageIcon(getClass().getResource("/icon/warn.png")));
				setText("NOT CONFIGURED");
				setBackground(new Color(255, 210, 210));
			} else {
				setIcon(new ImageIcon(getClass().getResource("/icon/ok.png")));
				setText((enableElement.isNewState() ? "ENABLE - "
						: "DISABLE - ") + enableElement.getElementType().name() + " " + enableElement.getId());
				setBackground(new Color(210, 255, 210));
			}
		}
		return this;
	}
}