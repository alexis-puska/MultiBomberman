package com.mygdx.game.editor.view.properties.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.mygdx.game.editor.domain.level.event.Message;

public class MessageRenderer extends JLabel implements ListCellRenderer<Message> {

    private static final long serialVersionUID = -7756260545095706601L;

    public MessageRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index,
            boolean isSelected, boolean cellHasFocus) {
        if (message.getEn() == null || message.getEn().isEmpty() || message.getFr() == null || message.getFr().isEmpty()
                || message.getEs() == null || message.getEs().isEmpty() || message.getTimeout() <40) {
            setIcon(new ImageIcon(getClass().getResource("/icon/warn.png")));
            setText("manque text");
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setForeground(list.getForeground());
                setBackground(new Color(255, 210, 210));
            }
        } else {
            setIcon(new ImageIcon(getClass().getResource("/icon/ok.png")));
            setText("fr : "+message.getFr());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setForeground(list.getForeground());
                setBackground(new Color(210, 255, 210));
            }
        }
        return this;
    }

}