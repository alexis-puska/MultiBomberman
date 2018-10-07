package com.mygdx.game.editor.view.properties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mygdx.game.editor.domain.level.Item;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class ItemPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Item item;

	private JLabel enableLabel;
	private JCheckBox enableCheckBox;
	private JLabel typeLabel;
	private SpinnerNumberModel typeModel;
	private JSpinner typeSpinner;

	public ItemPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService, String name,
			Item item) {
		super(message, parent, drawPanel, levelService, name);
		this.item = item;
		idField.setText(Integer.toString(item.getId()));

		enableLabel = new JLabel(message.getString("properties.item.enable"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);
		typeLabel = new JLabel(message.getString("properties.item.type"), JLabel.TRAILING);
		typeModel = new SpinnerNumberModel();
		typeSpinner = new JSpinner();
		typeModel.setMinimum(0);
		typeModel.setMaximum(352);
		typeSpinner.setModel(typeModel);
		typeLabel.setLabelFor(typeSpinner);
		typeSpinner.setValue(Integer.valueOf(item.getItemId()));
		enableCheckBox.setSelected(item.isEnable());
		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(typeLabel);
		this.add(typeSpinner);

		SpringUtilities.makeCompactGrid(this, 3, 2, 6, 6, 6, 6);
		addListeners();
		this.parent.updateUI();
	}

	public void addListeners() {
		typeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					item.setItemId((Integer) text.getValue());
					levelService.updateItem(item);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				item.setEnable(enableCheckBox.isSelected());
				levelService.updateItem(item);
				drawPanel.repaint();
			}
		});
	}

}
