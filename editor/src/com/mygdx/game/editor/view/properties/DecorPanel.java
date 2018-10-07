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

import com.mygdx.game.editor.domain.level.Decor;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class DecorPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Decor decor;

	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel indexAnimLabel;
	private JLabel enableLabel;
	private JLabel backLabel;

	private SpinnerNumberModel xModel;
	private SpinnerNumberModel yModel;
	private SpinnerNumberModel backgroundIdModel;

	private JSpinner xSpinner;
	private JSpinner ySpinner;
	private JSpinner indexAnimSpinner;
	private JCheckBox enableCheckBox;
	private JCheckBox backCheckBox;

	public DecorPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService,
			String name, Decor decor) {
		super(message, parent, drawPanel, levelService, name);
		this.decor = decor;
		xLabel = new JLabel(message.getString("properties.decor.x"), JLabel.TRAILING);
		xModel = new SpinnerNumberModel();
		xSpinner = new JSpinner();
		xModel.setMinimum(0);
		xModel.setMaximum(420);
		xSpinner.setModel(xModel);

		xLabel.setLabelFor(xSpinner);
		yLabel = new JLabel(message.getString("properties.decor.y"), JLabel.TRAILING);
		yModel = new SpinnerNumberModel();
		ySpinner = new JSpinner();
		yModel.setMinimum(0);
		yModel.setMaximum(500);
		ySpinner.setModel(yModel);
		yLabel.setLabelFor(ySpinner);

		indexAnimLabel = new JLabel(message.getString("properties.decor.decorId"), JLabel.TRAILING);
		backgroundIdModel = new SpinnerNumberModel();
		indexAnimSpinner = new JSpinner();
		backgroundIdModel.setMinimum(0);
		backgroundIdModel.setMaximum(9);
		indexAnimSpinner.setModel(backgroundIdModel);
		indexAnimLabel.setLabelFor(indexAnimSpinner);
		
		enableLabel = new JLabel(message.getString("properties.decor.enable"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);

		backLabel = new JLabel(message.getString("properties.decor.back"), JLabel.TRAILING);
		backCheckBox = new JCheckBox();
		backLabel.setLabelFor(backCheckBox);
		
		this.add(xLabel);
		this.add(xSpinner);
		this.add(yLabel);
		this.add(ySpinner);
		this.add(indexAnimLabel);
		this.add(indexAnimSpinner);
		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(backLabel);
		this.add(backCheckBox);


		idField.setText(Integer.toString(decor.getId()));
		xSpinner.setValue(Integer.valueOf(decor.getX()));
		ySpinner.setValue(Integer.valueOf(decor.getY()));
		indexAnimSpinner.setValue(Integer.valueOf(decor.getIndexAnim()));
		enableCheckBox.setSelected(decor.isEnable());
		backCheckBox.setSelected(decor.isBack());

		SpringUtilities.makeCompactGrid(this, 6, 2, 2, 2, 2, 2);
		addListeners();
		this.parent.updateUI();
	}

	public void updateDecor() {
		levelService.updateDecor(decor);
	}

	public void addListeners() {
		xSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					decor.setX((Integer) text.getValue());
					levelService.updateDecor(decor);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		ySpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					decor.setY((Integer) text.getValue());
					levelService.updateDecor(decor);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		indexAnimSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					decor.setIndexAnim((Integer) text.getValue());
					levelService.updateDecor(decor);
					drawPanel.repaint();
					parent.repaint();
				}
			}
		});
		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				decor.setEnable(enableCheckBox.isSelected());
				levelService.updateDecor(decor);
				drawPanel.repaint();
			}
		});
		backCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				decor.setBack(backCheckBox.isSelected());
				levelService.updateDecor(decor);
				drawPanel.repaint();
			}
		});
	}

}
