package com.mygdx.game.editor.view.properties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mygdx.game.editor.constant.RayonTypeEnum;
import com.mygdx.game.editor.domain.level.Rayon;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class RayonPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Rayon rayon;

	private JLabel enableLabel;
	private JCheckBox enableCheckBox;

	private JLabel typeLabel;
	private JComboBox<RayonTypeEnum> typeComboBox;
	private JLabel verticalLabel;
	private JCheckBox verticalCheckBox;

	public RayonPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService,
			String name, Rayon rayon) {
		super(message, parent, drawPanel, levelService, name);
		this.rayon = rayon;
		enableLabel = new JLabel(message.getString("properties.rayon.enable"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);
		typeLabel = new JLabel(message.getString("properties.rayon.type"), JLabel.TRAILING);
		typeComboBox = new JComboBox<RayonTypeEnum>(RayonTypeEnum.values());
		typeLabel.setLabelFor(typeComboBox);
		verticalLabel = new JLabel(message.getString("properties.rayon.vertical"), JLabel.TRAILING);
		verticalCheckBox = new JCheckBox();
		verticalLabel.setLabelFor(verticalCheckBox);

		idField.setText(Integer.toString(rayon.getId()));
		typeComboBox.setSelectedItem(rayon.getType());
		verticalCheckBox.setSelected(rayon.isVertical());
		enableCheckBox.setSelected(rayon.isEnable());

		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(typeLabel);
		this.add(typeComboBox);
		this.add(verticalLabel);
		this.add(verticalCheckBox);
		SpringUtilities.makeCompactGrid(this, 4, 2, 2, 2, 2, 2);
		addListeners();
		this.parent.updateUI();
	}

	public void addListeners() {
		typeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rayon.setType((RayonTypeEnum) typeComboBox.getSelectedItem());
				levelService.updateRayon(rayon);
				drawPanel.repaint();
				parent.repaint();
			}
		});
		verticalCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rayon.setVertical(verticalCheckBox.isSelected());
				levelService.updateRayon(rayon);
				drawPanel.repaint();
				parent.repaint();
			}
		});
		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				rayon.setEnable(enableCheckBox.isSelected());
				levelService.updateRayon(rayon);
				drawPanel.repaint();
			}
		});
	}
}
