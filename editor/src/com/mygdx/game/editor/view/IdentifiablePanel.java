package com.mygdx.game.editor.view;

import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import com.mygdx.game.editor.service.LevelService;


public class IdentifiablePanel extends JPanel {

    private static final long serialVersionUID = 5594850105813021006L;

    protected JPanel parent;
    protected DrawPanel drawPanel;
    protected LevelService levelService;
    private Border border;
    private SpringLayout layout;

    protected JLabel idLabel;
    protected JTextField idField;

    public IdentifiablePanel(ResourceBundle message, JPanel parent, DrawPanel drawPanel, LevelService levelService, String name) {
        this.parent = parent;
        this.drawPanel = drawPanel;
        this.levelService = levelService;
        border = BorderFactory.createTitledBorder(name);
        this.layout = new SpringLayout();
        this.setBorder(border);
        this.setLayout(layout);
        idField = new JTextField();
        idField.setEditable(false);
        idLabel = new JLabel(message.getString("identifiable.id"), JLabel.TRAILING);
        idLabel.setLabelFor(idField);
        this.add(idLabel);
        this.add(idField);
    }

}
