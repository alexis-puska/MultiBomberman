package com.mygdx.game.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygdx.constante.EditorConstante;
import com.mygdx.enumeration.LocaleEnum;
import com.mygdx.game.editor.enumeration.ActionEnum;
import com.mygdx.game.editor.service.LevelService2;
import com.mygdx.game.editor.service.SpriteService;
import com.mygdx.game.editor.utils.CoordinateUtils;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.BackgroundDrawPanel;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.ForegroundDrawPanel;

public class EditorLauncher extends JFrame {

	private static final Logger LOG = LogManager.getLogger(EditorLauncher.class);
	private static final long serialVersionUID = -8256444946608935363L;

	// services
	private final SpriteService spriteService;
	private final LevelService2 levelService2;

	// traduction
	private final Locale currentLocale;
	private final transient ResourceBundle message;

	private String absolutePathFile;
	private ActionEnum action;

	/****************
	 * WEST PANEL
	 ***************/
	private JPanel westPanel;
	private GridLayout westLayout;

	/****************
	 * EAST PANEL
	 ***************/
	private JPanel eastPanel;
	private transient Border eastPanelBorder;
	private GridLayout eastPanelLayout;

	private JPanel propertiesPanel;
	private BorderLayout propertiesPanelLayout;
	private transient Border propertiesPanelBorder;
	private JPanel levelPropertiesPanel;
	private transient SpringLayout levelPropertiesPanelLayout;
	private TitledBorder levelPropertiesPanelBorder;
	private JPanel bonusPanel;
	private transient SpringLayout bonusLayout;
	private TitledBorder bonusBorder;

	private JPanel texturePanel;
	private transient Border texturePanelBorder;
	private GridLayout texturePanelLayout;
	private JPanel foregroundPanel;
	private transient Border foregroundPanelBorder;
	private ForegroundDrawPanel foregroundDrawPanel;
	private JPanel backgroundPanel;
	private transient Border backgroundPanelBorder;
	private BackgroundDrawPanel backgroundDrawPanel;
	/********************
	 * LEVEL PROPERTIES
	 *******************/
	private JLabel levelNameFrLabel;
	private JTextField levelNameFrTextField;
	private JLabel levelNameEnLabel;
	private JTextField levelNameEnTextField;
	private JLabel varianteNameFrLabel;
	private JTextField varianteNameFrTextField;
	private JLabel varianteNameEnLabel;
	private JTextField varianteNameEnTextField;
	private JLabel descriptionFrLabel;
	private JTextField descriptionFrTextField;
	private JLabel descriptionEnLabel;
	private JTextField descriptionEnTextField;
	private JLabel shadowLabel;
	private SpinnerNumberModel shadowSpinnerModel;
	private JSpinner shadowSpinner;
	private JLabel bombeLabel;
	private SpinnerNumberModel bombeSpinnerModel;
	private JSpinner bombeSpinner;
	private JLabel strenghtLabel;
	private SpinnerNumberModel strenghtSpinnerModel;
	private JSpinner strenghtSpinner;
	private JLabel fillWithBrickLabel;
	private JCheckBox fillWithBrickCheckbox;
	private JLabel defaultBackgroundLabel;
	private JButton defaultBackGround;
	private JLabel defaultWallLabel;
	private JButton defaultWall;
	private JLabel defaultBrickAnimationLabel;
	private JComboBox<String> defaultBrickAnimationComboBox;

	/*********************************
	 * --- BONUS ---
	 *********************************/
	private JTextField bonus1TextField;
	private JLabel bonus1Label;
	private JTextField bonus2TextField;
	private JLabel bonus2Label;
	private JTextField bonus3TextField;
	private JLabel bonus3Label;
	private JTextField bonus4TextField;
	private JLabel bonus4Label;
	private JTextField bonus5TextField;
	private JLabel bonus5Label;
	private JTextField bonus6TextField;
	private JLabel bonus6Label;
	private JTextField bonus7TextField;
	private JLabel bonus7Label;
	private JTextField bonus8TextField;
	private JLabel bonus8Label;
	private JTextField bonus9TextField;
	private JLabel bonus9Label;
	private JTextField bonus10TextField;
	private JLabel bonus10Label;
	private JTextField bonus11TextField;
	private JLabel bonus11Label;
	private JTextField bonus12TextField;
	private JLabel bonus12Label;
	private JTextField bonus13TextField;
	private JLabel bonus13Label;
	private JTextField bonus14TextField;
	private JLabel bonus14Label;
	private JTextField bonus15TextField;
	private JLabel bonus15Label;

	/****************
	 * DRAW
	 ***************/
	private JPanel centerPanel;
	private BorderLayout drawLayout;
	private DrawPanel drawPanel;

	/****************
	 * NAVIGATION
	 ***************/
	private JPanel panelNavigation;
	private transient Border borderNavigation;
	private GridLayout layoutNavigation;
	// currentLevelIndex
	private JPanel currentLevelPanel;
	private GridLayout currentLevelLayout;
	private transient Border currentLevelBorder;
	private JButton addLevel;
	private JButton delLevel;
	private JButton nextLevel;
	private JButton previousLevel;
	// currentLevelIndex
	private JPanel currentVariantePanel;
	private GridLayout currentVarianteLayout;
	private transient Border currentVarianteBorder;
	private JButton addVariante;
	private JButton delVariante;
	private JButton nextVariante;
	private JButton previousVariante;

	// file
	private JPanel filePanel;
	private GridLayout fileLayout;
	private transient Border fileBorder;
	private JButton openSaveFileChooser;
	private transient JFileChooser saveFileChooser;
	private JButton openLoadFileChooser;
	private transient JFileChooser loadFileChooser;
	private FileNameExtensionFilter loadFileChooserFilter;
	private FileNameExtensionFilter saveFileChooserFilter;

	/****************
	 * ACTIONS
	 ***************/
	private JPanel buttonPanel;
	private transient Border buttonPanelBorder;
	private GridLayout buttonPanelLayout;
	private JButton addHoleButton;
	private JButton removeHoleButton;
	private JButton addRailButton;
	private JButton removeRailButton;
	private JButton addTrolleyButton;
	private JButton removeTrolleyButton;
	private JButton addInterrupteurButton;
	private JButton removeInterrupteurButton;
	private JButton addMineButton;
	private JButton removeMineButton;
	private JButton addTeleporterButton;
	private JButton removeTeleporterButton;
	private JButton addWallButton;
	private JButton removeWallButton;
	private JButton addStartPlayerButton;
	private JButton removeStartPlayerButton;
	private JButton addCustomBackgroundButton;
	private JButton removeCustomBackgroundButton;
	private JButton addCustomForegroundButton;
	private JButton removeCustomForegroundButton;

	public static void main(String[] args) {
		String lang = "fr";
		EditorLauncher app;
		if (args != null && args.length > 0 && (args[0].equals("fr") || args[0].equals("en"))) {
			lang = args[0];
		}
		app = new EditorLauncher(lang);
		app.launch();
	}

	public EditorLauncher(String lang) {
		this.action = ActionEnum.NONE;
		this.currentLocale = Locale.forLanguageTag(lang);
		this.message = ResourceBundle.getBundle("i18n/Message", currentLocale);
		LOG.info("message {} : {}", lang, this.message.getString("editor.border.bonus"));
		LOG.info("Welcome in lr-inthewell-editor App !");
		this.spriteService = new SpriteService();
		this.levelService2 = new LevelService2();
	}

	private void launch() {
		this.getContentPane().setLayout(new BorderLayout());
		initComponent();
		buildParameterPanelButton();
		buildBonusPanel();
		buildDrawElement();
		buildNavigationPanelButton();
		buildActionPanelButton();
		buildEastPanel();
		buildWestPanel();
		initListeners();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(EditorConstante.APP_SIZE_X, EditorConstante.APP_SIZE_Y);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**************************
	 * --- BUILD FUNCTION ---
	 **************************/
	private void buildDrawElement() {
		drawPanel.setSize(EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		drawPanel.setVisible(true);
		centerPanel.setBackground(Color.LIGHT_GRAY);
		centerPanel.setLayout(drawLayout);
		centerPanel.add(drawPanel, BorderLayout.CENTER);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
	}

	private void buildEastPanel() {
		foregroundDrawPanel.setSize(EditorConstante.EAST_PANEL_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		foregroundDrawPanel.setVisible(true);
		foregroundPanel.setBorder(foregroundPanelBorder);
		foregroundPanel.add(foregroundDrawPanel);
		backgroundDrawPanel.setSize(EditorConstante.EAST_PANEL_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		backgroundDrawPanel.setVisible(true);
		backgroundPanel.setBorder(backgroundPanelBorder);
		backgroundPanel.add(backgroundDrawPanel);
		texturePanelLayout.setRows(2);
		texturePanel.setLayout(texturePanelLayout);
		texturePanel.setBorder(texturePanelBorder);
		texturePanel.add(foregroundPanel);
		texturePanel.add(backgroundPanel);

		levelPropertiesPanel.setLayout(levelPropertiesPanelLayout);
		bonusPanel.setLayout(bonusLayout);
		propertiesPanel.setLayout(propertiesPanelLayout);
		propertiesPanel.setBorder(propertiesPanelBorder);
		propertiesPanel.add(levelPropertiesPanel, BorderLayout.NORTH);
		propertiesPanel.add(bonusPanel, BorderLayout.CENTER);

		eastPanelLayout.setColumns(2);
		eastPanel.setLayout(eastPanelLayout);
		eastPanel.setBorder(eastPanelBorder);
		eastPanel.add(propertiesPanel);
		eastPanel.add(texturePanel);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
	}

	private void buildActionPanelButton() {
		buttonPanel.setBorder(buttonPanelBorder);
		buttonPanel.setLayout(buttonPanelLayout);
		buttonPanel.add(addHoleButton);
		buttonPanel.add(removeHoleButton);
		buttonPanel.add(addRailButton);
		buttonPanel.add(removeRailButton);
		buttonPanel.add(addTrolleyButton);
		buttonPanel.add(removeTrolleyButton);
		buttonPanel.add(addInterrupteurButton);
		buttonPanel.add(removeInterrupteurButton);
		buttonPanel.add(addMineButton);
		buttonPanel.add(removeMineButton);
		buttonPanel.add(addTeleporterButton);
		buttonPanel.add(removeTeleporterButton);
		buttonPanel.add(addWallButton);
		buttonPanel.add(removeWallButton);
		buttonPanel.add(addStartPlayerButton);
		buttonPanel.add(removeStartPlayerButton);
		buttonPanel.add(addCustomBackgroundButton);
		buttonPanel.add(removeCustomBackgroundButton);
		buttonPanel.add(addCustomForegroundButton);
		buttonPanel.add(removeCustomForegroundButton);
	}

	private void buildWestPanel() {
		westLayout.setRows(2);
		westPanel.setLayout(westLayout);
		this.getContentPane().add(buttonPanel, BorderLayout.WEST);
	}

	private void buildNavigationPanelButton() {
		currentLevelPanel.setLayout(currentLevelLayout);
		currentLevelPanel.setBorder(currentLevelBorder);
		currentLevelPanel.add(previousLevel);
		currentLevelPanel.add(nextLevel);
		currentLevelPanel.add(addLevel);
		currentLevelPanel.add(delLevel);
		currentVariantePanel.setLayout(currentVarianteLayout);
		currentVariantePanel.setBorder(currentVarianteBorder);
		currentVariantePanel.add(previousVariante);
		currentVariantePanel.add(nextVariante);
		currentVariantePanel.add(addVariante);
		currentVariantePanel.add(delVariante);
		filePanel.setLayout(fileLayout);
		filePanel.setBorder(fileBorder);
		filePanel.add(openLoadFileChooser);
		filePanel.add(openSaveFileChooser);
		panelNavigation.setBorder(borderNavigation);
		panelNavigation.setLayout(layoutNavigation);
		panelNavigation.add(filePanel);
		panelNavigation.add(currentLevelPanel);
		panelNavigation.add(currentVariantePanel);
		this.getContentPane().add(panelNavigation, BorderLayout.NORTH);
	}

	private void buildParameterPanelButton() {
		levelPropertiesPanel.setBorder(levelPropertiesPanelBorder);
		levelPropertiesPanel.setLayout(levelPropertiesPanelLayout);
		levelNameFrLabel.setLabelFor(levelNameFrTextField);
		levelNameEnLabel.setLabelFor(levelNameEnTextField);
		varianteNameFrLabel.setLabelFor(levelNameFrLabel);
		varianteNameEnLabel.setLabelFor(levelNameFrLabel);
		descriptionFrLabel.setLabelFor(levelNameFrLabel);
		descriptionEnLabel.setLabelFor(levelNameFrLabel);
		shadowLabel.setLabelFor(levelNameFrLabel);
		bombeLabel.setLabelFor(levelNameFrLabel);
		strenghtLabel.setLabelFor(strenghtSpinner);
		fillWithBrickLabel.setLabelFor(fillWithBrickCheckbox);
		defaultBackgroundLabel.setLabelFor(defaultBackGround);
		defaultWallLabel.setLabelFor(defaultWall);
		defaultBrickAnimationLabel.setLabelFor(defaultBrickAnimationComboBox);
		levelPropertiesPanel.add(levelNameFrLabel);
		levelPropertiesPanel.add(levelNameFrTextField);
		levelPropertiesPanel.add(levelNameEnLabel);
		levelPropertiesPanel.add(levelNameEnTextField);
		levelPropertiesPanel.add(varianteNameFrLabel);
		levelPropertiesPanel.add(varianteNameFrTextField);
		levelPropertiesPanel.add(varianteNameEnLabel);
		levelPropertiesPanel.add(varianteNameEnTextField);
		levelPropertiesPanel.add(descriptionFrLabel);
		levelPropertiesPanel.add(descriptionFrTextField);
		levelPropertiesPanel.add(descriptionEnLabel);
		levelPropertiesPanel.add(descriptionEnTextField);
		levelPropertiesPanel.add(shadowLabel);
		levelPropertiesPanel.add(shadowSpinner);
		levelPropertiesPanel.add(bombeLabel);
		levelPropertiesPanel.add(bombeSpinner);
		levelPropertiesPanel.add(strenghtLabel);
		levelPropertiesPanel.add(strenghtSpinner);
		levelPropertiesPanel.add(fillWithBrickLabel);
		levelPropertiesPanel.add(fillWithBrickCheckbox);
		levelPropertiesPanel.add(defaultBackgroundLabel);
		levelPropertiesPanel.add(defaultBackGround);
		levelPropertiesPanel.add(defaultWallLabel);
		levelPropertiesPanel.add(defaultWall);
		levelPropertiesPanel.add(defaultBrickAnimationLabel);
		levelPropertiesPanel.add(defaultBrickAnimationComboBox);
		SpringUtilities.makeGrid(levelPropertiesPanel, 13, 2, 2, 2, 2, 2);
	}

	private void buildBonusPanel() {
		bonus1Label.setLabelFor(bonus1TextField);
		bonus2Label.setLabelFor(bonus2TextField);
		bonus3Label.setLabelFor(bonus3TextField);
		bonus4Label.setLabelFor(bonus4TextField);
		bonus5Label.setLabelFor(bonus5TextField);
		bonus6Label.setLabelFor(bonus6TextField);
		bonus7Label.setLabelFor(bonus7TextField);
		bonus8Label.setLabelFor(bonus8TextField);
		bonus9Label.setLabelFor(bonus9TextField);
		bonus10Label.setLabelFor(bonus10TextField);
		bonus11Label.setLabelFor(bonus11TextField);
		bonus12Label.setLabelFor(bonus12TextField);
		bonus13Label.setLabelFor(bonus13TextField);
		bonus14Label.setLabelFor(bonus14TextField);
		bonus15Label.setLabelFor(bonus15TextField);
		bonusPanel.setLayout(bonusLayout);
		bonusPanel.setBorder(bonusBorder);
		bonusPanel.add(bonus1Label);
		bonusPanel.add(bonus1TextField);
		bonusPanel.add(bonus2Label);
		bonusPanel.add(bonus2TextField);
		bonusPanel.add(bonus3Label);
		bonusPanel.add(bonus3TextField);
		bonusPanel.add(bonus4Label);
		bonusPanel.add(bonus4TextField);
		bonusPanel.add(bonus5Label);
		bonusPanel.add(bonus5TextField);
		bonusPanel.add(bonus6Label);
		bonusPanel.add(bonus6TextField);
		bonusPanel.add(bonus7Label);
		bonusPanel.add(bonus7TextField);
		bonusPanel.add(bonus8Label);
		bonusPanel.add(bonus8TextField);
		bonusPanel.add(bonus9Label);
		bonusPanel.add(bonus9TextField);
		bonusPanel.add(bonus10Label);
		bonusPanel.add(bonus10TextField);
		bonusPanel.add(bonus11Label);
		bonusPanel.add(bonus11TextField);
		bonusPanel.add(bonus12Label);
		bonusPanel.add(bonus12TextField);
		bonusPanel.add(bonus13Label);
		bonusPanel.add(bonus13TextField);
		bonusPanel.add(bonus14Label);
		bonusPanel.add(bonus14TextField);
		bonusPanel.add(bonus15Label);
		bonusPanel.add(bonus15TextField);
		SpringUtilities.makeGrid(bonusPanel, 15, 2, 2, 2, 2, 2);
	}

	/*************************************************************************************
	 *
	 * --- INIT COMPONENT ---
	 * 
	 *************************************************************************************/
	private void initComponent() {

		/***********************
		 * --- DRAW : CENTER---
		 ***********************/
		centerPanel = new JPanel();
		drawLayout = new BorderLayout();
		drawPanel = new DrawPanel(spriteService, levelService2);

		/*********************
		 * --- NAVIGATION ---
		 *********************/
		panelNavigation = new JPanel();
		borderNavigation = BorderFactory.createTitledBorder(message.getString("navigation.border"));
		layoutNavigation = new GridLayout();
		layoutNavigation.setColumns(EditorConstante.NB_COLUMN_NAVIGATION);
		layoutNavigation.setRows(EditorConstante.NB_ROW_NAVIGATION);
		currentLevelPanel = new JPanel();
		currentLevelLayout = new GridLayout();
		currentLevelBorder = BorderFactory.createTitledBorder(message.getString("currentLevel.border"));
		addLevel = new JButton(message.getString("currentLevel.button.add"));
		delLevel = new JButton(message.getString("currentLevel.button.delete"));
		nextLevel = new JButton(message.getString("currentLevel.button.next"));
		previousLevel = new JButton(message.getString("currentLevel.button.previous"));
		currentVariantePanel = new JPanel();
		currentVarianteLayout = new GridLayout();
		currentVarianteBorder = BorderFactory.createTitledBorder(message.getString("currentVariante.border"));
		addVariante = new JButton(message.getString("currentVariante.button.add"));
		delVariante = new JButton(message.getString("currentVariante.button.delete"));
		nextVariante = new JButton(message.getString("currentVariante.button.next"));
		previousVariante = new JButton(message.getString("currentVariante.button.previous"));

		/*********************
		 * --- FILE : NORTH ---
		 *********************/
		filePanel = new JPanel();
		fileLayout = new GridLayout();
		fileBorder = BorderFactory.createTitledBorder(message.getString("file.border"));
		openLoadFileChooser = new JButton(message.getString("file.open"));
		openSaveFileChooser = new JButton(message.getString("file.save"));
		loadFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		loadFileChooserFilter = new FileNameExtensionFilter(message.getString("file.description"), "json");
		loadFileChooser.setFileFilter(loadFileChooserFilter);
		saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		saveFileChooserFilter = new FileNameExtensionFilter(message.getString("file.description"), "json");
		saveFileChooser.setFileFilter(saveFileChooserFilter);

		/***********************
		 * --- BUTTONS : WEST---
		 ***********************/
		westPanel = new JPanel();
		westLayout = new GridLayout();
		buttonPanel = new JPanel();
		buttonPanelBorder = BorderFactory.createTitledBorder(message.getString("editor.border.action"));
		buttonPanelLayout = new GridLayout();
		buttonPanelLayout.setColumns(EditorConstante.NB_COLUMN_ACTION);
		buttonPanelLayout.setRows(EditorConstante.NB_ROW_ACTION);
		addHoleButton = new JButton(message.getString("editor.button.hole.add"));
		addRailButton = new JButton(message.getString("editor.button.rail.add"));
		addTrolleyButton = new JButton(message.getString("editor.button.trolley.add"));
		addInterrupteurButton = new JButton(message.getString("editor.button.interrupteur.add"));
		addMineButton = new JButton(message.getString("editor.button.mine.add"));
		addTeleporterButton = new JButton(message.getString("editor.button.teleporter.add"));
		addWallButton = new JButton(message.getString("editor.button.wall.add"));
		addStartPlayerButton = new JButton(message.getString("editor.button.startPlayer.add"));
		addCustomBackgroundButton = new JButton(message.getString("editor.button.defaultBackground.add"));
		addCustomForegroundButton = new JButton(message.getString("editor.button.defaultForeground.add"));
		removeHoleButton = new JButton(message.getString("editor.button.hole.remove"));
		removeRailButton = new JButton(message.getString("editor.button.rail.remove"));
		removeTrolleyButton = new JButton(message.getString("editor.button.trolley.remove"));
		removeInterrupteurButton = new JButton(message.getString("editor.button.interrupteur.remove"));
		removeMineButton = new JButton(message.getString("editor.button.mine.remove"));
		removeTeleporterButton = new JButton(message.getString("editor.button.teleporter.remove"));
		removeWallButton = new JButton(message.getString("editor.button.wall.remove"));
		removeStartPlayerButton = new JButton(message.getString("editor.button.startPlayer.remove"));
		removeCustomBackgroundButton = new JButton(message.getString("editor.button.defaultBackground.remove"));
		removeCustomForegroundButton = new JButton(message.getString("editor.button.defaultForeground.remove"));

		/*************************************
		 * --- PROPERTIES / TEXTURE : WEST---
		 *************************************/
		eastPanel = new JPanel();
		eastPanelLayout = new GridLayout();
		eastPanelBorder = BorderFactory.createTitledBorder("Outils");
		levelPropertiesPanel = new JPanel();
		levelPropertiesPanelLayout = new SpringLayout();
		levelPropertiesPanelBorder = BorderFactory.createTitledBorder("Level");
		bonusPanel = new JPanel();
		bonusLayout = new SpringLayout();
		bonusBorder = BorderFactory.createTitledBorder("Bonus");
		propertiesPanel = new JPanel();
		propertiesPanelLayout = new BorderLayout();
		propertiesPanelBorder = BorderFactory.createTitledBorder("Properties");
		foregroundPanel = new JPanel();
		foregroundPanelBorder = BorderFactory.createTitledBorder(message.getString("platform.border"));
		foregroundDrawPanel = new ForegroundDrawPanel(spriteService);
		backgroundPanel = new JPanel();
		backgroundPanelBorder = BorderFactory.createTitledBorder(message.getString("background.border"));
		backgroundDrawPanel = new BackgroundDrawPanel(spriteService);
		texturePanel = new JPanel();
		texturePanelLayout = new GridLayout();
		texturePanelBorder = BorderFactory.createTitledBorder("Texture");

		/*********************
		 * --- PROPERTIES ---
		 *********************/
		levelNameFrLabel = new JLabel("level name fr");
		levelNameFrTextField = new JTextField();
		levelNameEnLabel = new JLabel("level name en");
		levelNameEnTextField = new JTextField();
		varianteNameFrLabel = new JLabel("variante name fr");
		varianteNameFrTextField = new JTextField();
		varianteNameEnLabel = new JLabel("varnate name fr");
		varianteNameEnTextField = new JTextField();
		descriptionFrLabel = new JLabel("description fr");
		descriptionFrTextField = new JTextField();
		descriptionEnLabel = new JLabel("description en");
		descriptionEnTextField = new JTextField();
		shadowLabel = new JLabel("shadow value");
		shadowSpinnerModel = new SpinnerNumberModel(new Float(0.0), new Float(0.0), new Float(1.0), new Float(0.05));
		shadowSpinner = new JSpinner(shadowSpinnerModel);
		bombeLabel = new JLabel("nb bombe");
		bombeSpinnerModel = new SpinnerNumberModel(2, 1, 6, 1);
		bombeSpinner = new JSpinner(bombeSpinnerModel);
		strenghtLabel = new JLabel("strnght of bombe");
		strenghtSpinnerModel = new SpinnerNumberModel(2, 1, 20, 1);
		strenghtSpinner = new JSpinner(strenghtSpinnerModel);
		fillWithBrickLabel = new JLabel("fill with bricks");
		fillWithBrickCheckbox = new JCheckBox();
		defaultBackgroundLabel = new JLabel("default background texture");
		defaultBackGround = new JButton();
		defaultWallLabel = new JLabel("default wall texture");
		defaultWall = new JButton();
		defaultBrickAnimationLabel = new JLabel("default bricks animation");
		defaultBrickAnimationComboBox = new JComboBox<>();

		/*****************
		 * --- BONUS ---
		 *****************/
		bonus1TextField = new JTextField();
		bonus1Label = new JLabel("bonus1");
		bonus2TextField = new JTextField();
		bonus2Label = new JLabel("bonus2");
		bonus3TextField = new JTextField();
		bonus3Label = new JLabel("bonus3");
		bonus4TextField = new JTextField();
		bonus4Label = new JLabel("bonus4");
		bonus5TextField = new JTextField();
		bonus5Label = new JLabel("bonus5");
		bonus6TextField = new JTextField();
		bonus6Label = new JLabel("bonus6");
		bonus7TextField = new JTextField();
		bonus7Label = new JLabel("bonus7");
		bonus8TextField = new JTextField();
		bonus8Label = new JLabel("bonus8");
		bonus9TextField = new JTextField();
		bonus9Label = new JLabel("bonus9");
		bonus10TextField = new JTextField();
		bonus10Label = new JLabel("bonus10");
		bonus11TextField = new JTextField();
		bonus11Label = new JLabel("bonus11");
		bonus12TextField = new JTextField();
		bonus12Label = new JLabel("bonus12");
		bonus13TextField = new JTextField();
		bonus13Label = new JLabel("bonus13");
		bonus14TextField = new JTextField();
		bonus14Label = new JLabel("bonus14");
		bonus15TextField = new JTextField();
		bonus15Label = new JLabel("bonus15");

	}

	private void initListeners() {
		/***********************
		 * --- NAVIGATION ---
		 ***********************/
		openLoadFileChooser.addActionListener(actionEvent -> {
			int returnVal = loadFileChooser.showOpenDialog(panelNavigation);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				absolutePathFile = loadFileChooser.getSelectedFile().getAbsolutePath();
				try {
					saveFileChooser.setCurrentDirectory(loadFileChooser.getSelectedFile().getCanonicalFile());

					levelService2.load(new FileInputStream(new File(absolutePathFile)));
					centerPanel.updateUI();
					loadPropertiesLevel();
					repaint();
				} catch (FileNotFoundException e) {
					LOG.error("", e.getMessage());
				} catch (IOException e1) {
					LOG.info("Set save path failed !");
				}
				LOG.info("You chose to open this file: {}", loadFileChooser.getSelectedFile().getAbsolutePath());
			}
		});

		openSaveFileChooser.addActionListener(arg0 -> {
			int returnVal = saveFileChooser.showSaveDialog(panelNavigation);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				absolutePathFile = saveFileChooser.getSelectedFile().getAbsolutePath();
				if (!absolutePathFile.endsWith(".json")) {
					absolutePathFile += ".json";
				}
				levelService2.save(new File(absolutePathFile));
				centerPanel.updateUI();
				loadPropertiesLevel();
				repaint();
				LOG.info("You chose to open this file: ", absolutePathFile);
			}
		});

		/***********************
		 * --- DRAW ---
		 ***********************/
		drawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				click(e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		drawPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				LOG.info(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				LOG.info(e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				LOG.info(e.getKeyCode());
			}
		});

		backgroundDrawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (action == ActionEnum.SELECT_DEFAULT_BACKGROUND_TEXTURE) {
					int caseX = e.getX() / EditorConstante.GRID_SIZE_X;
					int caseY = e.getY() / EditorConstante.GRID_SIZE_Y;
					levelService2
							.setDefaultBackgroungTexture((caseY * EditorConstante.NB_COLUMN_DRAW_BACKGROUND) + caseX);
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		backgroundDrawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (action == ActionEnum.SELECT_DEFAULT_WALL_TEXTURE) {
					int caseX = e.getX() / EditorConstante.GRID_SIZE_X;
					int caseY = e.getY() / EditorConstante.GRID_SIZE_Y;
					levelService2.setDefaultWallTexture((caseY * EditorConstante.NB_COLUMN_DRAW_BACKGROUND) + caseX);
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		/***********************
		 * --- NAVIGATION ---
		 ***********************/
		nextLevel.addActionListener(action -> {
			levelService2.nextLevel();
			loadPropertiesLevel();
			repaint();
		});
		previousLevel.addActionListener(action -> {
			levelService2.previousLevel();
			loadPropertiesLevel();
			repaint();
		});
		addLevel.addActionListener(action -> {
			levelService2.addLevel();
			loadPropertiesLevel();
			repaint();
		});
		delLevel.addActionListener(action -> {
			levelService2.deleteLevel();
			loadPropertiesLevel();
			repaint();
		});
		nextVariante.addActionListener(action -> {
			levelService2.nextVariante();
			loadPropertiesLevel();
			repaint();
		});
		previousVariante.addActionListener(action -> {
			levelService2.previousVariante();
			loadPropertiesLevel();
			repaint();
		});
		addVariante.addActionListener(action -> {
			levelService2.addVariante();
			loadPropertiesLevel();
			repaint();
		});
		delVariante.addActionListener(action -> {
			levelService2.deleteVariante();
			loadPropertiesLevel();
			repaint();
		});

		/***************************
		 * --- PROPERTIES LEVEL ---
		 ***************************/
		levelNameEnTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setLevelName(LocaleEnum.ENGLISH, levelNameEnTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});

		levelNameFrTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setLevelName(LocaleEnum.FRENCH, levelNameFrTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});

		varianteNameEnTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setVarianteName(LocaleEnum.ENGLISH, varianteNameEnTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});
		varianteNameFrTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setVarianteName(LocaleEnum.FRENCH, varianteNameFrTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});

		descriptionEnTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setVarianteDescription(LocaleEnum.ENGLISH, descriptionEnTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});

		descriptionFrTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService2.setVarianteDescription(LocaleEnum.FRENCH, descriptionFrTextField.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateData();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateData();
			}
		});

		shadowSpinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Float f = (Float) text.getValue();
				levelService2.setShadow(f.floatValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bombeSpinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBombe(f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		strenghtSpinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setStrenght(f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		fillWithBrickCheckbox
				.addItemListener(item -> levelService2.setFillWithBrick(fillWithBrickCheckbox.isSelected()));
		defaultBackGround.addActionListener(e -> action = ActionEnum.SELECT_DEFAULT_BACKGROUND_TEXTURE);
		defaultWall.addActionListener(e -> action = ActionEnum.SELECT_DEFAULT_WALL_TEXTURE);
		defaultBrickAnimationComboBox.addItemListener(event -> {
			// door.setKey((GameKeyEnum) requieredKeyComboBox.getSelectedItem());
			// levelService.updateDoor(door);
			// drawPanel.repaint();
			// parent.repaint();
		});

		/********************
		 * --- ACTION ---
		 ********************/
		addHoleButton.addActionListener(listener -> action = ActionEnum.ADD_HOLE);
		addRailButton.addActionListener(listener -> action = ActionEnum.ADD_RAIL);
		addTrolleyButton.addActionListener(listener -> action = ActionEnum.ADD_TROLLEY);
		addInterrupteurButton.addActionListener(listener -> action = ActionEnum.ADD_INTERRUPTEUR);
		addMineButton.addActionListener(listener -> action = ActionEnum.ADD_MINE);
		addTeleporterButton.addActionListener(listener -> action = ActionEnum.ADD_TELEPORTER);
		addWallButton.addActionListener(listener -> action = ActionEnum.ADD_WALL);
		addStartPlayerButton.addActionListener(listener -> action = ActionEnum.ADD_START_PLAYER);
		addCustomBackgroundButton.addActionListener(listener -> action = ActionEnum.ADD_CUSTOM_BACKGROUND);
		addCustomForegroundButton.addActionListener(listener -> action = ActionEnum.ADD_CUSTOM_FOREGROUND);
		removeHoleButton.addActionListener(listener -> action = ActionEnum.REMOVE_HOLE);
		removeRailButton.addActionListener(listener -> action = ActionEnum.REMOVE_RAIL);
		removeTrolleyButton.addActionListener(listener -> action = ActionEnum.REMOVE_TROLLEY);
		removeInterrupteurButton.addActionListener(listener -> action = ActionEnum.REMOVE_INTERRUPTEUR);
		removeMineButton.addActionListener(listener -> action = ActionEnum.REMOVE_MINE);
		removeTeleporterButton.addActionListener(listener -> action = ActionEnum.REMOVE_TELEPORTER);
		removeWallButton.addActionListener(listener -> action = ActionEnum.REMOVE_WALL);
		removeStartPlayerButton.addActionListener(listener -> action = ActionEnum.REMOVE_START_PLAYER);
		removeCustomBackgroundButton.addActionListener(listener -> action = ActionEnum.REMOVE_CUSTOM_BACKGROUND);
		removeCustomForegroundButton.addActionListener(listener -> action = ActionEnum.REMOVE_CUSTOM_FOREGROUND);
	}

	/*************************************************************************************
	 * 
	 * --- DRAW EVENT ---
	 * 
	 *************************************************************************************/
	private void click(int x, int y) {
		int invY = CoordinateUtils.clickY(y);
		switch (action) {
		case ADD_CUSTOM_BACKGROUND:
			break;
		case ADD_CUSTOM_FOREGROUND:
			break;
		case ADD_HOLE:
			break;
		case ADD_INTERRUPTEUR:
			break;
		case ADD_MINE:
			break;
		case ADD_RAIL:
			this.levelService2.addRail(x, invY);
			break;
		case ADD_START_PLAYER:
			break;
		case ADD_TELEPORTER:
			break;
		case ADD_TROLLEY:
			break;
		case ADD_WALL:
			break;
		case NONE:
			break;
		case REMOVE_CUSTOM_BACKGROUND:
			break;
		case REMOVE_CUSTOM_FOREGROUND:
			break;
		case REMOVE_HOLE:
			break;
		case REMOVE_INTERRUPTEUR:
			break;
		case REMOVE_MINE:
			break;
		case REMOVE_RAIL:
			break;
		case REMOVE_START_PLAYER:
			break;
		case REMOVE_TELEPORTER:
			break;
		case REMOVE_TROLLEY:
			break;
		case REMOVE_WALL:
			break;
		case SELECT_DEFAULT_BACKGROUND_TEXTURE:
			break;
		case SELECT_DEFAULT_WALL_TEXTURE:
			break;
		default:
			break;
		}
		repaint();
	}

	/*************************************************************************************
	 * 
	 * --- TREAT FUNCTION ---
	 * 
	 *************************************************************************************/
	@Override
	public void repaint() {
		this.drawPanel.repaint();
	}

	public void loadPropertiesLevel() {
		levelNameEnTextField.setText(levelService2.getLevelName(LocaleEnum.ENGLISH));
		levelNameFrTextField.setText(levelService2.getLevelName(LocaleEnum.FRENCH));
		varianteNameEnTextField.setText(levelService2.getVarianteName(LocaleEnum.ENGLISH));
		varianteNameFrTextField.setText(levelService2.getVarianteName(LocaleEnum.FRENCH));
		descriptionEnTextField.setText(levelService2.getVarianteDescription(LocaleEnum.ENGLISH));
		descriptionFrTextField.setText(levelService2.getVarianteDescription(LocaleEnum.FRENCH));
		shadowSpinner.setValue(Float.valueOf(levelService2.getShadow()));
		bombeSpinner.setValue(Integer.valueOf(levelService2.getBombe()));
		strenghtSpinner.setValue(Integer.valueOf(levelService2.getStrenght()));
		fillWithBrickCheckbox.setSelected(levelService2.isFillWithBrick());
		levelPropertiesPanelBorder.setTitle(
				"Level : " + levelService2.getLevelPosition() + ", variante : " + levelService2.getVariantePosition());
		levelPropertiesPanel.repaint();
	}
}
