package com.mygdx.game.editor.view.properties;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = 3320448807138087320L;

	private Border border;
	private BorderLayout layout;
	private JScrollPane scrollPane;
	private JTextArea map;

	public MapPanel(ResourceBundle message, String textMap) {
		this.layout = new BorderLayout();
		this.border = BorderFactory.createTitledBorder(message.getString("properties.mapPanel.title"));
		this.setLayout(layout);
		this.setBorder(border);
		this.map = new JTextArea();
		this.map.setEditable(false);
		scrollPane = new JScrollPane(map);
		this.add(scrollPane, BorderLayout.CENTER);
		map.setText(textMap);
	}

}
