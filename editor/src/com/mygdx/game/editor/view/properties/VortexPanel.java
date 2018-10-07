package com.mygdx.game.editor.view.properties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mygdx.game.editor.domain.level.Vortex;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class VortexPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Vortex vortex;
	

	private JLabel destinationLabel;
	private JTextField destinationTextField;
	private JLabel enableLabel;
	private JCheckBox enableCheckBox;

	private JLabel xLabel;
	private SpinnerNumberModel xModel;
	private JSpinner xSpinner;

	private JLabel yLabel;
	private SpinnerNumberModel yModel;
	private JSpinner ySpinner;

	public VortexPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService,
			String name, Vortex vortex) {
		super(message, parent, drawPanel, levelService, name);
		this.vortex = vortex;
		destinationLabel = new JLabel(message.getString("properties.vortex.destination"), JLabel.TRAILING);
		destinationTextField = new JTextField();
		destinationLabel.setLabelFor(destinationTextField);
		
		enableLabel = new JLabel(message.getString("properties.vortex.active"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);
		
		xLabel = new JLabel(message.getString("properties.vortex.width"), JLabel.TRAILING);
		xModel = new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1);
		xSpinner = new JSpinner();
		xSpinner.setModel(xModel);
		xLabel.setLabelFor(xSpinner);
		

		yLabel = new JLabel(message.getString("properties.vortex.height"), JLabel.TRAILING);
		yModel = new SpinnerNumberModel(0.0, -10.0, 10.0, 0.1);
		ySpinner = new JSpinner();
		ySpinner.setModel(yModel);
		yLabel.setLabelFor(ySpinner);

		destinationLabel.setToolTipText(message.getString("properties.vortex.destination.description"));
		destinationTextField.setToolTipText(message.getString("properties.vortex.destination.description"));
		enableLabel.setToolTipText(message.getString("properties.vortex.active.description"));
		enableCheckBox.setToolTipText(message.getString("properties.vortex.active.description"));
		xLabel.setToolTipText(message.getString("properties.vortex.width.description"));
		xSpinner.setToolTipText(message.getString("properties.vortex.width.description"));
		yLabel.setToolTipText(message.getString("properties.vortex.height.description"));
		ySpinner.setToolTipText(message.getString("properties.vortex.height.description"));
		enableCheckBox.setSelected(vortex.isEnable());
		destinationTextField.setText(Integer.toString(vortex.getDestination()));
		xSpinner.setValue((Double)vortex.getZoomX());
		ySpinner.setValue((Double)vortex.getZoomY());

		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(destinationLabel);
		this.add(destinationTextField);
		this.add(xLabel);
		this.add(xSpinner);
		this.add(yLabel);
		this.add(ySpinner);
		idField.setText(Integer.toString(vortex.getId()));
		destinationTextField.setText(Integer.toString(vortex.getDestination()));
		SpringUtilities.makeCompactGrid(this, 5, 2, 6, 6, 6, 6);
		addListeners();
		this.parent.updateUI();
	}

	public void updateRayon() {
		levelService.updateVortex(vortex);
	}

	public void addListeners() {
		destinationTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					vortex.setDestination(Integer.valueOf(text.getText()));
					levelService.updateVortex(vortex);
				}
			}
		});
		destinationTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		enableCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				vortex.setEnable(enableCheckBox.isSelected());
				levelService.updateVortex(vortex);
				parent.repaint();
				drawPanel.repaint();
			}
		});
		
		xSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner text = (JSpinner) e.getSource();
                if (text.getValue() != null) {
                	vortex.setZoomX((Double)text.getValue());
                    levelService.updateVortex(vortex);
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
                    vortex.setZoomY((Double)text.getValue());
                    levelService.updateVortex(vortex);
                    drawPanel.repaint();
                    parent.repaint();
                }
            }
        });
	}
}
