package com.mygdx.game.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygdx.constante.Constante;

public class EditorLauncher extends JFrame {

	private static final long serialVersionUID = -3272449442566277297L;

	private static final Logger log = LogManager.getLogger(EditorLauncher.class);

	// services

	// traduction
	private Locale localeLanguage;
	private transient ResourceBundle message;

	// components
	private JPanel navigationPanel;
	private GridLayout navigationLayout;
	private Border navigationBorder;

	public static void main(String[] args) {
		String lang = "fr";
		EditorLauncher editorLauncher;
		if (args != null && args.length > 0 && (args[0].equals("fr") || args[0].equals("en"))) {
			lang = args[0];
		}
		editorLauncher = new EditorLauncher(lang);
		editorLauncher.launch();
	}

	public EditorLauncher(String lang) {
		this.localeLanguage = Locale.forLanguageTag(lang);
		this.message = ResourceBundle.getBundle("i18n/Message", localeLanguage);
		log.info("message {} : {}", lang, message.getString("editor.border.bonus"));
	}

	public void launch() {
		this.getContentPane().setLayout(new BorderLayout());

		initComponent();
		initListeners();
		buildNavigationPanel();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(Constante.EDITOR_SIZE_X, Constante.EDITOR_SIZE_Y);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initComponent() {
		navigationPanel = new JPanel();
		navigationLayout = new GridLayout(0, 1);
		navigationBorder = BorderFactory.createTitledBorder(message.getString("editor.border.navigation"));

	}

	private void initListeners() {
	}

	private void buildNavigationPanel() {
		navigationPanel.setLayout(navigationLayout);
		navigationPanel.setBorder(navigationBorder);
		this.getContentPane().add(navigationPanel, BorderLayout.NORTH);
	}

}
