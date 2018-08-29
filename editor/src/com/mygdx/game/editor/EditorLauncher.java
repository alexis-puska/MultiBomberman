package com.mygdx.game.editor;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.mygdx.constante.Constante;

public class EditorLauncher extends JFrame {

	private static final long serialVersionUID = -3272449442566277297L;

	public static void main(String[] args) {

		String lang = "fr";
		EditorLauncher editorLauncher;
		if (args != null && args.length > 0) {
			if (args[0].equals("fr") || args[0].equals("en")) {
				lang = args[0];
			}
		}
		editorLauncher = new EditorLauncher(lang);
		editorLauncher.Launch();

	}

	public EditorLauncher(String lang) {
		// this.locale = Locale.forLanguageTag(lang);
		// this.message = ResourceBundle.getBundle("i18n/Message", locale);

	}

	public void Launch() {
		this.getContentPane().setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(Constante.EDITOR_SIZE_X, Constante.EDITOR_SIZE_Y);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
