package com.mygdx.game.editor.view.properties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mygdx.game.editor.constant.GameKeyEnum;
import com.mygdx.game.editor.domain.level.Door;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class DoorPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Door door;

	private JLabel enableLabel;
	private JCheckBox enableCheckBox;

	private JLabel typeLabel;
	private SpinnerNumberModel typeModel;
	private JSpinner typeSpinner;

	private JLabel toLevelLabel;
	private SpinnerNumberModel toLevelModel;
	private JSpinner toLevelSpinner;

	private JLabel requieredKeyLabel;
	private JComboBox<GameKeyEnum> requieredKeyComboBox;

	private JLabel serrureIdLabel;
	private SpinnerNumberModel serrureIdModel;
	private JSpinner serrureIdSpinner;

	private JLabel lockedLabel;
	private JCheckBox lockedCheckBox;

	public DoorPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService, String name,
			Door door) {
		super(message, parent, drawPanel, levelService, name);
		this.door = door;

		enableLabel = new JLabel(message.getString("properties.door.enable"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);

		typeLabel = new JLabel(message.getString("properties.door.type"), JLabel.TRAILING);
		typeModel = new SpinnerNumberModel();
		typeSpinner = new JSpinner();
		typeModel.setMinimum(0);
		typeModel.setMaximum(5);
		typeSpinner.setModel(typeModel);
		typeLabel.setLabelFor(typeSpinner);

		toLevelLabel = new JLabel(message.getString("properties.door.toLevel"), JLabel.TRAILING);
		toLevelModel = new SpinnerNumberModel();
		toLevelSpinner = new JSpinner();
		toLevelSpinner.setModel(toLevelModel);
		toLevelLabel.setLabelFor(toLevelSpinner);

		requieredKeyLabel = new JLabel(message.getString("properties.door.requieredKey"), JLabel.TRAILING);
		requieredKeyComboBox = new JComboBox<>(GameKeyEnum.values());
		requieredKeyComboBox.addItem(null);
		requieredKeyLabel.setLabelFor(requieredKeyComboBox);

		serrureIdLabel = new JLabel(message.getString("properties.lock.serrure"), JLabel.TRAILING);
		serrureIdModel = new SpinnerNumberModel();
		serrureIdSpinner = new JSpinner();
		serrureIdSpinner.setModel(serrureIdModel);
		serrureIdLabel.setLabelFor(serrureIdSpinner);

		lockedLabel = new JLabel(message.getString("properties.door.locked"), JLabel.TRAILING);
		lockedCheckBox = new JCheckBox();
		lockedLabel.setLabelFor(lockedCheckBox);

		idField.setText(Integer.toString(door.getId()));
		typeSpinner.setValue(Integer.valueOf(door.getType()));
		toLevelSpinner.setValue(Integer.valueOf(door.getToLevel()));
		requieredKeyComboBox.setSelectedItem(door.getKey());
		serrureIdSpinner.setValue(Integer.valueOf(door.getLockId()));
		lockedCheckBox.setSelected(door.isLocked());
		enableCheckBox.setSelected(door.isEnable());

		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(typeLabel);
		this.add(typeSpinner);
		this.add(toLevelLabel);
		this.add(toLevelSpinner);
		this.add(requieredKeyLabel);
		this.add(requieredKeyComboBox);
		this.add(serrureIdLabel);
		this.add(serrureIdSpinner);
		this.add(lockedLabel);
		this.add(lockedCheckBox);
		addListeners();
		SpringUtilities.makeCompactGrid(this, 7, 2, 6, 6, 6, 6);
		this.parent.updateUI();
	}

	public void addListeners() {
		typeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					door.setType((Integer) text.getValue());
					levelService.updateDoor(door);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		toLevelSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					door.setToLevel((Integer) text.getValue());
					levelService.updateDoor(door);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		requieredKeyComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				door.setKey((GameKeyEnum) requieredKeyComboBox.getSelectedItem());
				levelService.updateDoor(door);
				drawPanel.repaint();
				parent.repaint();
			}
		});
		serrureIdSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					door.setLockId((Integer) text.getValue());
					levelService.updateDoor(door);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		lockedCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				door.setLocked(lockedCheckBox.isSelected());
				levelService.updateDoor(door);
				drawPanel.repaint();
			}
		});
		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				door.setEnable(enableCheckBox.isSelected());
				levelService.updateDoor(door);
				drawPanel.repaint();
			}
		});
	}

}
