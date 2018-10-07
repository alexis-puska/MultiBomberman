
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

import com.mygdx.game.editor.domain.level.Pick;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class PickPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Pick pick;
	
	private JLabel enableLabel;
	private JCheckBox enableCheckBox;

	private JLabel directionLabel;
	private SpinnerNumberModel directionModel;
	private JSpinner directionSpinner;

	public PickPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService, String name,
			Pick pick) {
		super(message, parent, drawPanel, levelService, name);
		this.pick = pick;

		enableLabel = new JLabel(message.getString("properties.pick.actif"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableCheckBox.setToolTipText(message.getString("properties.pick.actif.description"));
		enableLabel.setLabelFor(enableCheckBox);

		directionLabel = new JLabel(message.getString("properties.pick.direction"), JLabel.TRAILING);
		directionModel = new SpinnerNumberModel();
		directionSpinner = new JSpinner();
		directionModel.setMinimum(0);
		directionModel.setMaximum(3);
		directionSpinner.setModel(directionModel);
		directionLabel.setLabelFor(directionSpinner);

		idField.setText(Integer.toString(pick.getId()));
		enableCheckBox.setSelected(pick.isEnable());
		directionSpinner.setValue((Integer) pick.getDirection());

		this.add(enableLabel);
		this.add(enableCheckBox);

		this.add(directionLabel);
		this.add(directionSpinner);

		SpringUtilities.makeCompactGrid(this, 3, 2, 6, 6, 6, 6);
		addListeners();
		this.parent.updateUI();
	}

	public void addListeners() {
		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				pick.setEnable(enableCheckBox.isSelected());
				levelService.updatePick(pick);
				drawPanel.repaint();
			}
		});
		directionSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					pick.setDirection((Integer) text.getValue());
					levelService.updatePick(pick);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
	}
}
