package com.mygdx.game.editor.view.properties;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

import com.mygdx.game.editor.domain.level.Teleporter;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.IdentifiablePanel;

public class TeleporterPanel extends IdentifiablePanel {

	private static final long serialVersionUID = -4090876979915495722L;

	private Teleporter teleporter;

	private JLabel enableLabel;
	private JLabel invXLabel;
	private JLabel invYLabel;
	private JCheckBox enableCheckBox;
	private JCheckBox invXCheckBox;
	private JCheckBox invYCheckBox;

	private JLabel destinationLabel;
	private JTextField destinationTextField;

	public TeleporterPanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService,
			String name, Teleporter teleporter) {
		super(message, parent, drawPanel, levelService, name);
		this.teleporter = teleporter;
		enableLabel = new JLabel(message.getString("properties.teleporter.enable"), JLabel.TRAILING);
		enableCheckBox = new JCheckBox();
		invXLabel = new JLabel(message.getString("properties.teleporter.invX"), JLabel.TRAILING);
		invXCheckBox = new JCheckBox();
		invYLabel = new JLabel(message.getString("properties.teleporter.invY"), JLabel.TRAILING);
		invYCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);
		destinationLabel = new JLabel(message.getString("properties.teleporter.destination"), JLabel.TRAILING);
		destinationTextField = new JTextField();
		destinationLabel.setLabelFor(destinationTextField);
		destinationTextField.setToolTipText(message.getString("properties.teleporter.destination.description"));
		destinationLabel.setToolTipText(message.getString("properties.teleporter.destination.description"));
		this.add(enableLabel);
		this.add(enableCheckBox);
		this.add(invXLabel);
		this.add(invXCheckBox);
		this.add(invYLabel);
		this.add(invYCheckBox);
		this.add(destinationLabel);
		this.add(destinationTextField);
		idField.setText(Integer.toString(teleporter.getId()));
		destinationTextField.setText(teleporter.getDestinations());
		enableCheckBox.setSelected(teleporter.isEnable());
		invXCheckBox.setSelected(teleporter.isInvX());
		invYCheckBox.setSelected(teleporter.isInvY());
		SpringUtilities.makeCompactGrid(this, 5, 2, 6, 6, 6, 6);
		addListeners();
		this.parent.updateUI();
	}

	public void updateTeleporter() {
		levelService.updateTeleporter(teleporter);
	}

	public void addListeners() {
		destinationTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					String tmp = text.getText();
					if (tmp.endsWith(",")) {
						teleporter.setDestinations(tmp.substring(0, tmp.length() - 1));
					} else {
						teleporter.setDestinations(tmp);
					}
					levelService.updateTeleporter(teleporter);
				}
			}
		});
		destinationTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if ((destinationTextField.getText() != null && !destinationTextField.getText().isEmpty())
						&& !(destinationTextField.getText().endsWith(","))) {
					if ((!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_COMMA))
							|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
						e.consume();
					}
				} else {
					if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE)
							|| (vChar == KeyEvent.VK_DELETE))) {
						e.consume();
					}
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
				teleporter.setEnable(enableCheckBox.isSelected());
				levelService.updateTeleporter(teleporter);
				drawPanel.repaint();
			}
		});
		invXCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				teleporter.setInvX(invXCheckBox.isSelected());
				levelService.updateTeleporter(teleporter);
				drawPanel.repaint();
			}
		});
		invYCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				teleporter.setInvY(invYCheckBox.isSelected());
				levelService.updateTeleporter(teleporter);
				drawPanel.repaint();
			}
		});
	}
}
