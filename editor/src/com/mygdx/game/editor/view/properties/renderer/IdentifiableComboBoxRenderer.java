package com.mygdx.game.editor.view.properties.renderer;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.mygdx.game.editor.domain.level.Decor;
import com.mygdx.game.editor.domain.level.Door;
import com.mygdx.game.editor.domain.level.Ennemie;
import com.mygdx.game.editor.domain.level.Identifiable;
import com.mygdx.game.editor.domain.level.Item;
import com.mygdx.game.editor.domain.level.Lock;
import com.mygdx.game.editor.domain.level.Pick;
import com.mygdx.game.editor.domain.level.Platform;
import com.mygdx.game.editor.domain.level.Rayon;
import com.mygdx.game.editor.domain.level.Teleporter;
import com.mygdx.game.editor.domain.level.Vortex;
import com.mygdx.game.editor.domain.level.event.Event;

public class IdentifiableComboBoxRenderer extends JLabel implements ListCellRenderer<Identifiable> {

	private static final long serialVersionUID = 6026263763623162494L;
	private ResourceBundle message;

	public IdentifiableComboBoxRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	public IdentifiableComboBoxRenderer(ResourceBundle message) {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		this.message = message;
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Identifiable> list, Identifiable value, int index,
			boolean isSelected, boolean cellHasFocus) {
		String cellText = "";
		if (value.getClass().equals(Item.class)) {
			cellText += message.getString("identifiable.item") + value.getId();
		} else if (value.getClass().equals(Decor.class)) {
			cellText += message.getString("identifiable.decor") + value.getId();
		} else if (value.getClass().equals(Door.class)) {
			cellText += message.getString("identifiable.door") + value.getId();
		} else if (value.getClass().equals(Ennemie.class)) {
			cellText += message.getString("identifiable.ennemie") + value.getId();
		} else if (value.getClass().equals(Event.class)) {
			cellText += message.getString("identifiable.event") + value.getId();
		} else if (value.getClass().equals(Lock.class)) {
			cellText += message.getString("identifiable.lock") + value.getId();
		} else if (value.getClass().equals(Pick.class)) {
			cellText += message.getString("identifiable.pick") + value.getId();
		} else if (value.getClass().equals(Platform.class)) {
			cellText += message.getString("identifiable.platform") + value.getId();
		} else if (value.getClass().equals(Rayon.class)) {
			cellText += message.getString("identifiable.rayon") + value.getId();
		} else if (value.getClass().equals(Teleporter.class)) {
			cellText += message.getString("identifiable.teleporter") + value.getId();
		} else if (value.getClass().equals(Vortex.class)) {
			cellText += message.getString("identifiable.vortex") + value.getId();
		}
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		this.setText(cellText);
		return this;
	}

}