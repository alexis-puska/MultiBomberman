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
import com.mygdx.game.editor.enumeration.BrickBurnEnum;
import com.mygdx.game.editor.renderer.BrickComboboxRenderer;
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

	// utils
	private int posX;
	private int posY;
	private int lastBackgroundIndexClicked;
	private int lastForegroundIndexClicked;

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
	private JSpinner shadowSpinner;
	private JLabel bombeLabel;
	private JSpinner bombeSpinner;
	private JLabel strenghtLabel;
	private JSpinner strenghtSpinner;
	private JLabel fillWithBrickLabel;
	private JCheckBox fillWithBrickCheckbox;
	private JLabel defaultBackgroundLabel;
	private JButton defaultBackGround;
	private JLabel defaultWallLabel;
	private JButton defaultWall;
	private JLabel defaultBrickAnimationLabel;
	private JComboBox<BrickBurnEnum> defaultBrickAnimationComboBox;

	/*********************************
	 * --- BONUS ---
	 *********************************/
	private JSpinner bonus0Spinner;
	private JLabel bonus0Label;
	private JSpinner bonus1Spinner;
	private JLabel bonus1Label;
	private JSpinner bonus2Spinner;
	private JLabel bonus2Label;
	private JSpinner bonus3Spinner;
	private JLabel bonus3Label;
	private JSpinner bonus4Spinner;
	private JLabel bonus4Label;
	private JSpinner bonus5Spinner;
	private JLabel bonus5Label;
	private JSpinner bonus6Spinner;
	private JLabel bonus6Label;
	private JSpinner bonus7Spinner;
	private JLabel bonus7Label;
	private JSpinner bonus8Spinner;
	private JLabel bonus8Label;
	private JSpinner bonus9Spinner;
	private JLabel bonus9Label;
	private JSpinner bonus10Spinner;
	private JLabel bonus10Label;
	private JSpinner bonus11Spinner;
	private JLabel bonus11Label;
	private JSpinner bonus12Spinner;
	private JLabel bonus12Label;
	private JSpinner bonus13Spinner;
	private JLabel bonus13Label;
	private JSpinner bonus14Spinner;
	private JLabel bonus14Label;

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
	private JButton copyLevel;
	private JButton delLevel;
	private JButton nextLevel;
	private JButton previousLevel;
	// currentLevelIndex
	private JPanel currentVariantePanel;
	private GridLayout currentVarianteLayout;
	private transient Border currentVarianteBorder;
	private JButton addVariante;
	private JButton copyVariante;
	private JButton delVariante;
	private JButton nextVariante;
	private JButton previousVariante;

	// file
	private JPanel filePanel;
	private GridLayout fileLayout;
	private transient Border fileBorder;
	private transient JFileChooser loadFileChooser;
	private JButton openSaveFileChooser;
	private transient JFileChooser saveFileChooser;
	private JButton openLoadFileChooser;

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
	private JButton addWallCustomisationButton;
	private JButton removeWallCustomisationButton;
	private JButton setWallTransparent;
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
		this.posX = 1;
		this.posY = 1;
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
		buttonPanel.add(addWallCustomisationButton);
		buttonPanel.add(removeWallCustomisationButton);
		buttonPanel.add(setWallTransparent);
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
		currentLevelPanel.add(copyLevel);
		currentLevelPanel.add(delLevel);
		currentVariantePanel.setLayout(currentVarianteLayout);
		currentVariantePanel.setBorder(currentVarianteBorder);
		currentVariantePanel.add(previousVariante);
		currentVariantePanel.add(nextVariante);
		currentVariantePanel.add(addVariante);
		currentVariantePanel.add(copyVariante);
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
		bonus0Label.setLabelFor(bonus0Spinner);
		bonus1Label.setLabelFor(bonus1Spinner);
		bonus2Label.setLabelFor(bonus2Spinner);
		bonus3Label.setLabelFor(bonus3Spinner);
		bonus4Label.setLabelFor(bonus4Spinner);
		bonus5Label.setLabelFor(bonus5Spinner);
		bonus6Label.setLabelFor(bonus6Spinner);
		bonus7Label.setLabelFor(bonus7Spinner);
		bonus8Label.setLabelFor(bonus8Spinner);
		bonus9Label.setLabelFor(bonus9Spinner);
		bonus10Label.setLabelFor(bonus10Spinner);
		bonus11Label.setLabelFor(bonus11Spinner);
		bonus12Label.setLabelFor(bonus12Spinner);
		bonus13Label.setLabelFor(bonus13Spinner);
		bonus14Label.setLabelFor(bonus14Spinner);
		bonusPanel.setLayout(bonusLayout);
		bonusPanel.setBorder(bonusBorder);
		bonusPanel.add(bonus0Label);
		bonusPanel.add(bonus0Spinner);
		bonusPanel.add(bonus1Label);
		bonusPanel.add(bonus1Spinner);
		bonusPanel.add(bonus2Label);
		bonusPanel.add(bonus2Spinner);
		bonusPanel.add(bonus3Label);
		bonusPanel.add(bonus3Spinner);
		bonusPanel.add(bonus4Label);
		bonusPanel.add(bonus4Spinner);
		bonusPanel.add(bonus5Label);
		bonusPanel.add(bonus5Spinner);
		bonusPanel.add(bonus6Label);
		bonusPanel.add(bonus6Spinner);
		bonusPanel.add(bonus7Label);
		bonusPanel.add(bonus7Spinner);
		bonusPanel.add(bonus8Label);
		bonusPanel.add(bonus8Spinner);
		bonusPanel.add(bonus9Label);
		bonusPanel.add(bonus9Spinner);
		bonusPanel.add(bonus10Label);
		bonusPanel.add(bonus10Spinner);
		bonusPanel.add(bonus11Label);
		bonusPanel.add(bonus11Spinner);
		bonusPanel.add(bonus12Label);
		bonusPanel.add(bonus12Spinner);
		bonusPanel.add(bonus13Label);
		bonusPanel.add(bonus13Spinner);
		bonusPanel.add(bonus14Label);
		bonusPanel.add(bonus14Spinner);
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
		borderNavigation = BorderFactory.createTitledBorder(message.getString("editor.border.navigation"));
		layoutNavigation = new GridLayout();
		layoutNavigation.setColumns(EditorConstante.NB_COLUMN_NAVIGATION);
		layoutNavigation.setRows(EditorConstante.NB_ROW_NAVIGATION);
		currentLevelPanel = new JPanel();
		currentLevelLayout = new GridLayout();
		currentLevelBorder = BorderFactory.createTitledBorder(message.getString("editor.border.currentLevel"));
		addLevel = new JButton(message.getString("editor.button.currentLevel.add"));
		copyLevel = new JButton(message.getString("editor.button.currentLevel.copy"));
		delLevel = new JButton(message.getString("editor.button.currentLevel.delete"));
		nextLevel = new JButton(message.getString("editor.button.currentLevel.next"));
		previousLevel = new JButton(message.getString("editor.button.currentLevel.previous"));
		currentVariantePanel = new JPanel();
		currentVarianteLayout = new GridLayout();
		currentVarianteBorder = BorderFactory.createTitledBorder(message.getString("editor.border.currentVariante"));
		addVariante = new JButton(message.getString("editor.button.currentVariante.add"));
		copyVariante = new JButton(message.getString("editor.button.currentVariante.copy"));
		delVariante = new JButton(message.getString("editor.button.currentVariante.delete"));
		nextVariante = new JButton(message.getString("editor.button.currentVariante.next"));
		previousVariante = new JButton(message.getString("editor.button.currentVariante.previous"));

		/*********************
		 * --- FILE : NORTH ---
		 *********************/
		filePanel = new JPanel();
		fileLayout = new GridLayout();
		fileBorder = BorderFactory.createTitledBorder(message.getString("editor.file.border"));
		openLoadFileChooser = new JButton(message.getString("editor.file.open"));
		openSaveFileChooser = new JButton(message.getString("editor.file.save"));
		loadFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

		FileNameExtensionFilter loadFileChooserFilter = new FileNameExtensionFilter(
				message.getString("editor.file.description"), "json");
		loadFileChooser.setFileFilter(loadFileChooserFilter);
		saveFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		FileNameExtensionFilter saveFileChooserFilter = new FileNameExtensionFilter(
				message.getString("editor.file.description"), "json");
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
		addWallCustomisationButton = new JButton(message.getString("editor.button.wall.addCustomisation"));
		removeWallCustomisationButton = new JButton(message.getString("editor.button.wall.removeCustomisation"));
		setWallTransparent = new JButton(message.getString("editor.button.wall.setTransparent"));
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
		eastPanelBorder = BorderFactory.createTitledBorder(this.message.getString("editor.border.tools"));
		levelPropertiesPanel = new JPanel();
		levelPropertiesPanelLayout = new SpringLayout();
		levelPropertiesPanelBorder = BorderFactory.createTitledBorder(this.message.getString("editor.border.level"));
		bonusPanel = new JPanel();
		bonusLayout = new SpringLayout();
		bonusBorder = BorderFactory.createTitledBorder(this.message.getString("editor.border.bonus"));
		propertiesPanel = new JPanel();
		propertiesPanelLayout = new BorderLayout();
		propertiesPanelBorder = BorderFactory.createTitledBorder(this.message.getString("editor.border.properties"));
		foregroundPanel = new JPanel();
		foregroundPanelBorder = BorderFactory.createTitledBorder(message.getString("editor.border.foreground"));
		foregroundDrawPanel = new ForegroundDrawPanel(spriteService);
		backgroundPanel = new JPanel();
		backgroundPanelBorder = BorderFactory.createTitledBorder(message.getString("editor.border.background"));
		backgroundDrawPanel = new BackgroundDrawPanel(spriteService);
		texturePanel = new JPanel();
		texturePanelLayout = new GridLayout();
		texturePanelBorder = BorderFactory.createTitledBorder(this.message.getString("editor.border.texture"));

		/*********************
		 * --- PROPERTIES ---
		 *********************/
		levelNameFrLabel = new JLabel(this.message.getString("editor.label.level.name.fr"));
		levelNameFrTextField = new JTextField();
		levelNameEnLabel = new JLabel(this.message.getString("editor.label.level.name.en"));
		levelNameEnTextField = new JTextField();
		varianteNameFrLabel = new JLabel(this.message.getString("editor.label.variante.name.fr"));
		varianteNameFrTextField = new JTextField();
		varianteNameEnLabel = new JLabel(this.message.getString("editor.label.variante.name.en"));
		varianteNameEnTextField = new JTextField();
		descriptionFrLabel = new JLabel(this.message.getString("editor.label.variante.description.fr"));
		descriptionFrTextField = new JTextField();
		descriptionEnLabel = new JLabel(this.message.getString("editor.label.variante.description.en"));
		descriptionEnTextField = new JTextField();
		shadowLabel = new JLabel(this.message.getString("editor.label.variante.shadow"));
		SpinnerNumberModel shadowSpinnerModel = new SpinnerNumberModel(new Float(0.0), new Float(0.0), new Float(1.0),
				new Float(0.05));
		shadowSpinner = new JSpinner(shadowSpinnerModel);
		bombeLabel = new JLabel(this.message.getString("editor.label.variante.bombe"));
		SpinnerNumberModel bombeSpinnerModel = new SpinnerNumberModel(2, 1, 6, 1);
		bombeSpinner = new JSpinner(bombeSpinnerModel);
		strenghtLabel = new JLabel(this.message.getString("editor.label.variante.strength"));
		SpinnerNumberModel strenghtSpinnerModel = new SpinnerNumberModel(2, 1, 20, 1);
		strenghtSpinner = new JSpinner(strenghtSpinnerModel);
		fillWithBrickLabel = new JLabel(this.message.getString("editor.label.variante.fillBrick"));
		fillWithBrickCheckbox = new JCheckBox();
		defaultBackgroundLabel = new JLabel(this.message.getString("editor.label.variante.defaultBackgroundTexture"));
		defaultBackGround = new JButton();
		defaultWallLabel = new JLabel(this.message.getString("editor.label.variante.defaultWallTexture"));
		defaultWall = new JButton();
		defaultBrickAnimationLabel = new JLabel(this.message.getString("editor.label.variante.defaultBrickAnimation"));
		defaultBrickAnimationComboBox = new JComboBox<>(BrickBurnEnum.values());
		defaultBrickAnimationComboBox.setRenderer(new BrickComboboxRenderer(spriteService));

		/*****************
		 * --- BONUS ---
		 *****************/
		SpinnerNumberModel bonus0SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus0Spinner = new JSpinner(bonus0SpinnerModel);
		bonus0Label = new JLabel(this.message.getString("editor.label.variante.bonus.death"));
		SpinnerNumberModel bonus1SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus1Spinner = new JSpinner(bonus1SpinnerModel);
		bonus1Label = new JLabel(this.message.getString("editor.label.variante.bonus.roller"));
		SpinnerNumberModel bonus2SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus2Spinner = new JSpinner(bonus2SpinnerModel);
		bonus2Label = new JLabel(this.message.getString("editor.label.variante.bonus.fire"));
		SpinnerNumberModel bonus3SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus3Spinner = new JSpinner(bonus3SpinnerModel);
		bonus3Label = new JLabel(this.message.getString("editor.label.variante.bonus.fire+"));
		SpinnerNumberModel bonus4SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus4Spinner = new JSpinner(bonus4SpinnerModel);
		bonus4Label = new JLabel(this.message.getString("editor.label.variante.bonus.bombe"));
		SpinnerNumberModel bonus5SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus5Spinner = new JSpinner(bonus5SpinnerModel);
		bonus5Label = new JLabel(this.message.getString("editor.label.variante.bonus.bombep"));
		SpinnerNumberModel bonus6SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus6Spinner = new JSpinner(bonus6SpinnerModel);
		bonus6Label = new JLabel(this.message.getString("editor.label.variante.bonus.kick"));
		SpinnerNumberModel bonus7SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus7Spinner = new JSpinner(bonus7SpinnerModel);
		bonus7Label = new JLabel(this.message.getString("editor.label.variante.bonus.glove"));
		SpinnerNumberModel bonus8SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus8Spinner = new JSpinner(bonus8SpinnerModel);
		bonus8Label = new JLabel(this.message.getString("editor.label.variante.bonus.rubber"));
		SpinnerNumberModel bonus9SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus9Spinner = new JSpinner(bonus9SpinnerModel);
		bonus9Label = new JLabel(this.message.getString("editor.label.variante.bonus.superBombe"));
		SpinnerNumberModel bonus10SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus10Spinner = new JSpinner(bonus10SpinnerModel);
		bonus10Label = new JLabel(this.message.getString("editor.label.variante.bonus.shoes"));
		SpinnerNumberModel bonus11SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus11Spinner = new JSpinner(bonus11SpinnerModel);
		bonus11Label = new JLabel(this.message.getString("editor.label.variante.bonus.wall"));
		SpinnerNumberModel bonus12SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus12Spinner = new JSpinner(bonus12SpinnerModel);
		bonus12Label = new JLabel(this.message.getString("editor.label.variante.bonus.egg"));
		SpinnerNumberModel bonus13SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus13Spinner = new JSpinner(bonus13SpinnerModel);
		bonus13Label = new JLabel(this.message.getString("editor.label.variante.bonus.shield"));
		SpinnerNumberModel bonus14SpinnerModel = new SpinnerNumberModel(10, 0, 150, 1);
		bonus14Spinner = new JSpinner(bonus14SpinnerModel);
		bonus14Label = new JLabel(this.message.getString("editor.label.variante.bonus.bombeLine"));

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
				calCoordinate(e.getX(), e.getY());
				addElement();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// unused method
			}
		});

		drawPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// unused method
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// unused method
			}

			@Override
			public void keyPressed(KeyEvent e) {
				LOG.info("pressed : " + e.getKeyCode());
				switch (e.getKeyCode()) {
				case 32:
				case 10:
					addElement();
					break;
				case 37: // left
					if (posX > 0) {
						posX--;
					}
					drawPanel.updatePoint(posX, posY);
					drawPanel.repaint();
					break;
				case 38: // top
					if (posY < 20) {
						posY++;
					}
					drawPanel.updatePoint(posX, posY);
					drawPanel.repaint();
					break;
				case 39: // right
					if (posX < 34) {
						posX++;
					}
					drawPanel.updatePoint(posX, posY);
					drawPanel.repaint();
					break;
				case 40: // bottom
					if (posY > 0) {
						posY--;
					}
					drawPanel.updatePoint(posX, posY);
					drawPanel.repaint();
					break;
				default:
					break;
				}
			}
		});

		foregroundDrawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int caseX = e.getX() / EditorConstante.LARGE_GRID_SIZE_X;
				int caseY = e.getY() / EditorConstante.LARGE_GRID_SIZE_Y;
				lastForegroundIndexClicked = (caseY * EditorConstante.NB_COLUMN_DRAW_FOREGROUND) + caseX;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// unused method
			}
		});

		backgroundDrawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int caseX = e.getX() / EditorConstante.GRID_SIZE_X;
				int caseY = e.getY() / EditorConstante.GRID_SIZE_Y;
				lastBackgroundIndexClicked = (caseY * EditorConstante.NB_COLUMN_DRAW_BACKGROUND) + caseX;
				if (action == ActionEnum.SELECT_DEFAULT_WALL_TEXTURE) {
					levelService2.setDefaultWallTexture((caseY * EditorConstante.NB_COLUMN_DRAW_BACKGROUND) + caseX);
					repaint();
				} else if (action == ActionEnum.SELECT_DEFAULT_BACKGROUND_TEXTURE) {
					levelService2
							.setDefaultBackgroungTexture((caseY * EditorConstante.NB_COLUMN_DRAW_BACKGROUND) + caseX);
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// unused method
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// unused method
			}
		});

		/***********************
		 * --- NAVIGATION ---
		 ***********************/
		nextLevel.addActionListener(e -> {
			levelService2.nextLevel();
			loadPropertiesLevel();
			repaint();
		});
		previousLevel.addActionListener(e -> {
			levelService2.previousLevel();
			loadPropertiesLevel();
			repaint();
		});
		addLevel.addActionListener(e -> {
			levelService2.addLevel();
			loadPropertiesLevel();
			repaint();
		});
		copyLevel.addActionListener(e -> {
			levelService2.copyLevelAndSelectIt();
			loadPropertiesLevel();
			repaint();
		});
		delLevel.addActionListener(e -> {
			levelService2.deleteLevel();
			loadPropertiesLevel();
			repaint();
		});
		nextVariante.addActionListener(e -> {
			levelService2.nextVariante();
			loadPropertiesLevel();
			repaint();
		});
		previousVariante.addActionListener(e -> {
			levelService2.previousVariante();
			loadPropertiesLevel();
			repaint();
		});
		addVariante.addActionListener(e -> {
			levelService2.addVariante();
			loadPropertiesLevel();
			repaint();
		});
		copyVariante.addActionListener(e -> {
			levelService2.copyVarianteAndSelectIt();
			loadPropertiesLevel();
			repaint();
		});
		delVariante.addActionListener(e -> {
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
			levelService2.setDefaultBrickAnimtion(
					BrickBurnEnum.getSpriteFromBrick((BrickBurnEnum) defaultBrickAnimationComboBox.getSelectedItem()));
			drawPanel.repaint();
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
		addWallCustomisationButton.addActionListener(listener -> action = ActionEnum.CUSTOMIZE_WALL);
		removeWallCustomisationButton.addActionListener(listener -> action = ActionEnum.REMOVE_CUSTOMIZATION_WALL);
		setWallTransparent.addActionListener(listener -> action = ActionEnum.SET_WALL_TRANSPARENT);
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

		/********************
		 * --- BONUS ---
		 ********************/
		bonus0Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(0, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus1Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(1, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus2Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(2, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus3Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(3, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus4Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(4, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus5Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(5, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus6Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(6, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus7Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(7, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus8Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(8, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus9Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(9, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus10Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(10, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus11Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(11, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus12Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(12, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus13Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(13, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});
		bonus14Spinner.addChangeListener(e -> {
			JSpinner text = (JSpinner) e.getSource();
			if (text.getValue() != null) {
				Integer f = (Integer) text.getValue();
				levelService2.setBonus(14, f.intValue());
				loadPropertiesLevel();
				drawPanel.repaint();
			}
		});

	}

	/*************************************************************************************
	 * 
	 * --- DRAW EVENT ---
	 * 
	 *************************************************************************************/
	private void addElement() {
		switch (action) {
		case ADD_CUSTOM_BACKGROUND:
			this.levelService2.addCustomBackgroundTexture(posX, posY, lastBackgroundIndexClicked);
			break;
		case ADD_CUSTOM_FOREGROUND:
			this.levelService2.addCustomForegroundTexture(posX, posY, lastForegroundIndexClicked);
			break;
		case ADD_HOLE:
			this.levelService2.addHole(posX, posY);
			break;
		case ADD_INTERRUPTEUR:
			this.levelService2.addInterrupter(posX, posY);
			break;
		case ADD_MINE:
			this.levelService2.addMine(posX, posY);
			break;
		case ADD_RAIL:
			this.levelService2.addRail(posX, posY);
			break;
		case ADD_START_PLAYER:
			this.levelService2.addStartPlayer(posX, posY);
			break;
		case ADD_TELEPORTER:
			this.levelService2.addTeleporter(posX, posY);
			break;
		case ADD_TROLLEY:
			this.levelService2.addTrolley(posX, posY);
			break;
		case ADD_WALL:
			this.levelService2.addWall(posX, posY);
			break;
		case CUSTOMIZE_WALL:
			this.levelService2.customizeWall(posX, posY, lastBackgroundIndexClicked);
			break;
		case REMOVE_CUSTOM_BACKGROUND:
			this.levelService2.removeCustomBackgroundTexture(posX, posY);
			break;
		case REMOVE_CUSTOM_FOREGROUND:
			this.levelService2.removeCustomForegroundTexture(posX, posY);
			break;
		case REMOVE_HOLE:
			this.levelService2.removeHole(posX, posY);
			break;
		case REMOVE_INTERRUPTEUR:
			this.levelService2.removeInterrupter(posX, posY);
			break;
		case REMOVE_MINE:
			this.levelService2.removeMine(posX, posY);
			break;
		case REMOVE_RAIL:
			this.levelService2.removeRail(posX, posY);
			break;
		case REMOVE_START_PLAYER:
			this.levelService2.removeStartPlayer(posX, posY);
			break;
		case REMOVE_TELEPORTER:
			this.levelService2.removeTeleporter(posX, posY);
			break;
		case REMOVE_TROLLEY:
			this.levelService2.removeTrolley(posX, posY);
			break;
		case REMOVE_WALL:
			this.levelService2.removeWall(posX, posY);
			break;
		case REMOVE_CUSTOMIZATION_WALL:
			this.levelService2.removeCustomizationWall(posX, posY);
			break;
		case SELECT_DEFAULT_BACKGROUND_TEXTURE:
			break;
		case SELECT_DEFAULT_WALL_TEXTURE:
			break;
		case SET_WALL_TRANSPARENT:
			this.levelService2.setWallTransparent(posX, posY);
			break;
		case NONE:
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

	private void calCoordinate(int x, int y) {
		posX = x / EditorConstante.GRID_SIZE_X;
		int tmpY = y / EditorConstante.GRID_SIZE_Y;
		posY = CoordinateUtils.invGridY(tmpY);
		drawPanel.updatePoint(posX, posY);
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
		defaultBrickAnimationComboBox
				.setSelectedItem(BrickBurnEnum.getBrickFromSprite(levelService2.getDefaultBrickAnimation()));
		strenghtSpinner.setValue(Integer.valueOf(levelService2.getStrenght()));
		fillWithBrickCheckbox.setSelected(levelService2.isFillWithBrick());
		levelPropertiesPanelBorder.setTitle(
				"Level : " + levelService2.getLevelPosition() + ", variante : " + levelService2.getVariantePosition());
		levelPropertiesPanel.repaint();
		bonus0Spinner.setValue(Integer.valueOf(levelService2.getBonus(0)));
		bonus1Spinner.setValue(Integer.valueOf(levelService2.getBonus(1)));
		bonus2Spinner.setValue(Integer.valueOf(levelService2.getBonus(2)));
		bonus3Spinner.setValue(Integer.valueOf(levelService2.getBonus(3)));
		bonus4Spinner.setValue(Integer.valueOf(levelService2.getBonus(4)));
		bonus5Spinner.setValue(Integer.valueOf(levelService2.getBonus(5)));
		bonus6Spinner.setValue(Integer.valueOf(levelService2.getBonus(6)));
		bonus7Spinner.setValue(Integer.valueOf(levelService2.getBonus(7)));
		bonus8Spinner.setValue(Integer.valueOf(levelService2.getBonus(8)));
		bonus9Spinner.setValue(Integer.valueOf(levelService2.getBonus(9)));
		bonus10Spinner.setValue(Integer.valueOf(levelService2.getBonus(10)));
		bonus11Spinner.setValue(Integer.valueOf(levelService2.getBonus(11)));
		bonus12Spinner.setValue(Integer.valueOf(levelService2.getBonus(12)));
		bonus13Spinner.setValue(Integer.valueOf(levelService2.getBonus(13)));
		bonus14Spinner.setValue(Integer.valueOf(levelService2.getBonus(14)));
		bonusPanel.repaint();
	}
}
