package com.mygdx.game.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygdx.constante.EditorConstante;
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
	private static final int OFFSET = 10;

	// services
	private final SpriteService spriteService;
	private final LevelService2 levelService2;

	// traduction
	private final Locale locale;
	private final ResourceBundle message;

	private String absolutePathFile;
	private int xFirst;
	private int yFirst;
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
	private GridLayout eastLayout;
	private JPanel texturePanel;
	private Border textureBorder;
	private GridLayout textureLayout;
	private JPanel foregroundPanel;
	private Border foregroundBorder;
	private ForegroundDrawPanel foregroundDrawPanel;
	private JPanel backgroundPanel;
	private Border backgroundBorder;
	private BackgroundDrawPanel backgroundDrawPanel;
	/********************
	 * LEVEL PROPERTIES
	 *******************/
	private JPanel panelParameters;
	private SpringLayout panelParametersLayout;
	private Border panelParametersBorder;
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
	private JComboBox defaultBrickAnimationComboBox;

	private JPanel bonusPanel;
	private SpringLayout bonusLayout;
	private Border bonusBorder;
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
	private JFrame eventJFrame;
	private JFrame mapViewJFrame;

	/****************
	 * NAVIGATION
	 ***************/
	private JPanel panelNavigation;
	private Border borderNavigation;
	private GridLayout layoutNavigation;
	// currentLevelIndex
	private JPanel currentLevelPanel;
	private GridLayout currentLevelLayout;
	private Border currentLevelBorder;
	private JButton addLevel;
	private JButton delLevel;
	private JButton nextLevel;
	private JButton previousLevel;
	// currentLevelIndex
	private JPanel currentVariantePanel;
	private GridLayout currentVarianteLayout;
	private Border currentVarianteBorder;
	private JButton addVariante;
	private JButton delVariante;
	private JButton nextVariante;
	private JButton previousVariante;

	// file
	private JPanel filePanel;
	private GridLayout fileLayout;
	private Border fileBorder;
	private JButton openSaveFileChooser;
	private JFileChooser saveFileChooser;
	private JButton openLoadFileChooser;
	private JFileChooser loadFileChooser;
	private FileNameExtensionFilter loadFileChooserFilter;
	private FileNameExtensionFilter saveFileChooserFilter;

	/****************
	 * ACTIONS
	 ***************/
	private JPanel buttonPanel;
	private Border buttonPanelBorder;
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
		if (args != null && args.length > 0) {
			if (args[0].equals("fr") || args[0].equals("en")) {
				lang = args[0];
			}
		}
		app = new EditorLauncher(lang);
		app.Launch();
	}

	public EditorLauncher(String lang) {
		this.locale = Locale.forLanguageTag(lang);
		this.message = ResourceBundle.getBundle("i18n/Message", locale);
		LOG.info("message {} : {}", lang, this.message.getString("editor.border.bonus"));
		LOG.info("Welcome in lr-inthewell-editor App !");
		this.spriteService = new SpriteService();

		this.levelService2 = new LevelService2();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("json/levels.json");
		levelService2.load(in);
		this.action = ActionEnum.NONE;
	}

	private void Launch() {
		this.getContentPane().setLayout(new BorderLayout());
		initComponent();
		initListeners();
		buildParameterPanelButton();
		buildEastPanel();
		buildDrawElement();
		buildNavigationPanelButton();
		buildActionPanelButton();
		buildWestPanel();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(EditorConstante.APP_SIZE_X, EditorConstante.APP_SIZE_Y);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/*************************************************************************************
	 * 
	 * --- BUILD FUNCTION ---
	 * 
	 *************************************************************************************/

	private void buildDrawElement() {
		drawPanel.setSize(EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		drawPanel.setVisible(true);
		centerPanel.setBackground(Color.LIGHT_GRAY);
		centerPanel.setLayout(drawLayout);
		centerPanel.add(drawPanel, BorderLayout.CENTER);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
	}

	private void buildEastPanel() {
		foregroundDrawPanel.setSize(EditorConstante.PANEL_PLATFORM_BACKGROUD_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		foregroundDrawPanel.setVisible(true);
		foregroundPanel.setBorder(foregroundBorder);
		foregroundPanel.add(foregroundDrawPanel);
		backgroundDrawPanel.setSize(EditorConstante.PANEL_PLATFORM_BACKGROUD_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		backgroundDrawPanel.setVisible(true);
		backgroundPanel.setBorder(backgroundBorder);
		backgroundPanel.add(backgroundDrawPanel);
		textureLayout.setRows(2);
		texturePanel.setLayout(textureLayout);
		texturePanel.setBorder(textureBorder);
		texturePanel.add(foregroundPanel);
		texturePanel.add(backgroundPanel);
		eastLayout.setColumns(2);
		eastPanel.setLayout(eastLayout);

		JPanel tmp = new JPanel();
		BorderLayout layout = new BorderLayout();
		tmp.setLayout(layout);
		tmp.add(panelParameters, BorderLayout.NORTH);
		tmp.add(bonusPanel, BorderLayout.CENTER);
		eastPanel.add(tmp);

//		eastPanel.add(panelParameters);
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

		panelParameters.setBorder(panelParametersBorder);
		panelParameters.setLayout(panelParametersLayout);

		shadowSpinnerModel.setMinimum(0);
		shadowSpinnerModel.setMaximum(1);
		shadowSpinnerModel.setValue(0);
		shadowSpinnerModel.setStepSize(0.1f);
		shadowSpinner.setModel(shadowSpinnerModel);

		bombeSpinnerModel.setMinimum(1);
		bombeSpinnerModel.setMaximum(6);
		bombeSpinnerModel.setValue(2);
		bombeSpinnerModel.setStepSize(0.1f);
		bombeSpinner.setModel(bombeSpinnerModel);

		strenghtSpinnerModel.setMinimum(1);
		strenghtSpinnerModel.setMaximum(20);
		strenghtSpinnerModel.setValue(2);
		strenghtSpinnerModel.setStepSize(0.1f);
		strenghtSpinner.setModel(strenghtSpinnerModel);

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

		panelParameters.add(levelNameFrLabel);
		panelParameters.add(levelNameFrTextField);
		panelParameters.add(levelNameEnLabel);
		panelParameters.add(levelNameEnTextField);
		panelParameters.add(varianteNameFrLabel);
		panelParameters.add(varianteNameFrTextField);
		panelParameters.add(varianteNameEnLabel);
		panelParameters.add(varianteNameEnTextField);
		panelParameters.add(descriptionFrLabel);
		panelParameters.add(descriptionFrTextField);
		panelParameters.add(descriptionEnLabel);
		panelParameters.add(descriptionEnTextField);
		panelParameters.add(shadowLabel);
		panelParameters.add(shadowSpinner);
		panelParameters.add(bombeLabel);
		panelParameters.add(bombeSpinner);
		panelParameters.add(strenghtLabel);
		panelParameters.add(strenghtSpinner);
		panelParameters.add(fillWithBrickLabel);
		panelParameters.add(fillWithBrickCheckbox);
		panelParameters.add(defaultBackgroundLabel);
		panelParameters.add(defaultBackGround);
		panelParameters.add(defaultWallLabel);
		panelParameters.add(defaultWall);
		panelParameters.add(defaultBrickAnimationLabel);
		panelParameters.add(defaultBrickAnimationComboBox);
		SpringUtilities.makeGrid(panelParameters, 13, 2, 2, 2, 2, 2);

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

		/*
		 * boolean shouldFill = true; boolean shouldWeightX = true; JButton button;
		 * panelParameters.setLayout(new GridBagLayout()); GridBagConstraints c = new
		 * GridBagConstraints(); if (shouldFill) { //natural height, maximum width
		 * c.fill = GridBagConstraints.HORIZONTAL; }
		 * 
		 * button = new JButton("Button 1"); if (shouldWeightX) { c.weightx = 0.5; }
		 * c.fill = GridBagConstraints.HORIZONTAL; c.gridx = 0; c.gridy = 0;
		 * panelParameters.add(button, c);
		 * 
		 * button = new JButton("Button 2"); c.fill = GridBagConstraints.HORIZONTAL;
		 * c.weightx = 0.5; c.gridx = 1; c.gridy = 0; panelParameters.add(button, c);
		 * 
		 * button = new JButton("Button 3"); c.fill = GridBagConstraints.HORIZONTAL;
		 * c.weightx = 0.0; c.gridx = 0; c.gridy = 1; panelParameters.add(button, c);
		 * 
		 * button = new JButton("Long-Named Button 4"); c.fill =
		 * GridBagConstraints.HORIZONTAL; c.ipady = 40; //make this component tall
		 * c.weightx = 0.0; c.gridwidth = 3; c.gridx = 0; c.gridy = 2;
		 * panelParameters.add(button, c);
		 * 
		 * button = new JButton("5"); c.fill = GridBagConstraints.HORIZONTAL; c.ipady =
		 * 0; //reset to default c.weighty = 1.0; //request any extra vertical space
		 * //c.anchor = GridBagConstraints.PAGE_END; //bottom of space c.insets = new
		 * Insets(10,0,0,0); //top padding c.gridx = 1; //aligned with button 2
		 * c.gridwidth = 2; //2 columns wide c.gridy = 3; //third row
		 * panelParameters.add(button, c);
		 */

	}

	/*************************************************************************************
	 *
	 * --- INIT COMPONENT ---
	 * 
	 *************************************************************************************/
	private void initComponent() {

		// westPanel
		westPanel = new JPanel();
		westLayout = new GridLayout();

		// EastPanel
		eastPanel = new JPanel();
		eastLayout = new GridLayout();
		texturePanel = new JPanel();
		textureLayout = new GridLayout();
		textureBorder = BorderFactory.createTitledBorder(message.getString("platform.border"));
		foregroundPanel = new JPanel();
		foregroundBorder = BorderFactory.createTitledBorder(message.getString("platform.border"));
		foregroundDrawPanel = new ForegroundDrawPanel(spriteService);
		backgroundPanel = new JPanel();
		backgroundBorder = BorderFactory.createTitledBorder(message.getString("background.border"));
		backgroundDrawPanel = new BackgroundDrawPanel(spriteService);

		// draw
		centerPanel = new JPanel();
		drawLayout = new BorderLayout();
		drawPanel = new DrawPanel(spriteService, levelService2);

		// navigation
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

		// file
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

		// ennemies
		buttonPanel = new JPanel();
		buttonPanelBorder = BorderFactory.createTitledBorder(message.getString("editor.border.action"));
		buttonPanelLayout = new GridLayout();
		buttonPanelLayout.setColumns(EditorConstante.NB_COLUMN_ENNEMIE);
		buttonPanelLayout.setRows(EditorConstante.NB_ROW_ENNEMIE);
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

		// properties
		panelParameters = new JPanel();
		panelParametersLayout = new SpringLayout();
		// layoutParameters = new GridLayout();
		// layoutParameters.setColumns(EditorConstante.NB_COLUMN_PARAMETER);
		// layoutParameters.setRows(EditorConstante.NB_ROW_PARAMETER);

		panelParametersBorder = BorderFactory.createTitledBorder("Properties");

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
		shadowSpinnerModel = new SpinnerNumberModel();
		shadowSpinner = new JSpinner();
		bombeLabel = new JLabel("nb bombe");
		bombeSpinnerModel = new SpinnerNumberModel();
		bombeSpinner = new JSpinner();
		strenghtLabel = new JLabel("strnght of bombe");
		strenghtSpinnerModel = new SpinnerNumberModel();
		strenghtSpinner = new JSpinner();
		fillWithBrickLabel = new JLabel("fill with bricks");
		fillWithBrickCheckbox = new JCheckBox();
		defaultBackgroundLabel = new JLabel("default background texture");
		defaultBackGround = new JButton();
		defaultWallLabel = new JLabel("default wall texture");
		defaultWall = new JButton();
		defaultBrickAnimationLabel = new JLabel("default bricks animation");
		defaultBrickAnimationComboBox = new JComboBox();

		bonusPanel = new JPanel();
		bonusLayout = new SpringLayout();
		bonusBorder = BorderFactory.createTitledBorder("Bonus");
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

		/*************************************************************************************
		 *
		 * --- DRAW ---
		 * 
		 *************************************************************************************/
		drawPanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				click(e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				pressed(e.getX(), e.getY());
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				release(e.getX(), e.getY());
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
				System.out.println(e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e.getKeyCode());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e.getKeyCode());
			}
		});

		/*************************************************************************************
		 *
		 * --- NAVIGATION ---
		 * 
		 *************************************************************************************/
		// currentLevelIndex.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("ChangeLevel : " + (Integer) text.getValue());
		// levelService.setCurrentLevelIndex((Integer) text.getValue());
		// loadPropertiesLevel();
		// drawPanel.repaint();
		// }
		// }
		// });
		// currentLevelIndex.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("ChangeLevel : " + (Integer) text.getValue());
		// levelService.setCurrentLevelIndex((Integer) text.getValue());
		// loadPropertiesLevel();
		// drawPanel.repaint();
		// }
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		// currentTypeLevelIndex.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("Change Type : " + (Integer) text.getValue());
		// levelService.setCurrentTypeIndex((Integer) text.getValue());
		// currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
		// centerPanel.updateUI();
		// loadPropertiesLevel();
		// drawPanel.repaint();
		// }
		// }
		// });
		// currentTypeLevelIndex.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("Change Type : " + (Integer) text.getValue());
		// levelService.setCurrentTypeIndex((Integer) text.getValue());
		// currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
		// centerPanel.updateUI();
		// loadPropertiesLevel();
		// drawPanel.repaint();
		// }
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		// addLevel.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// levelService.createLevel();
		// loadPropertiesLevel();
		// repaint();
		// }
		// });
		// delLevel.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// levelService.deleteLevel();
		// loadPropertiesLevel();
		// repaint();
		// }
		// });
		openLoadFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = loadFileChooser.showOpenDialog(panelNavigation);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					absolutePathFile = loadFileChooser.getSelectedFile().getAbsolutePath();
					try {
						saveFileChooser.setCurrentDirectory(loadFileChooser.getSelectedFile().getCanonicalFile());
					} catch (IOException e1) {
						System.out.println("Set save path failed !");
					}
					// try {
					// levelService.putLevelFile(
					// fileService.readJsonFile(new FileInputStream(new File(absolutePathFile))));
					// currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
					// currentTypeLevelIndex.setValue((Integer) levelService.getCurrentTypeIndex());
					// centerPanel.updateUI();
					// loadPropertiesLevel();
					// repaint();
					// } catch (FileNotFoundException e) {
					// LOG.error("", e.getMessage());
					// }
					System.out.println(
							"You chose to open this file: " + loadFileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		openSaveFileChooser.addActionListener(arg0 -> {
			int returnVal = saveFileChooser.showSaveDialog(panelNavigation);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				// LevelFile levelFile = levelService.getLevelFile();
				// absolutePathFile = saveFileChooser.getSelectedFile().getAbsolutePath();
				// if (!absolutePathFile.endsWith(".json")) {
				// absolutePathFile += ".json";
				// }
				// fileService.writeJson(levelFile, new File(absolutePathFile));
				// centerPanel.updateUI();
				// loadPropertiesLevel();
				// repaint();
				// System.out.println("You chose to open this file: " + absolutePathFile);
			}
		});
		/*************************************************************************************
		 *
		 * --- PROPERTIES LEVEL ---
		 * 
		 *************************************************************************************/
		//
		// horizontalPlatformIndexSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setHorizontalPlatformId : " + (Integer) text.getValue());
		// levelService.setHorizontalPlatformId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// });
		// horizontalPlatformIndexSpinner.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setHorizontalPlatformId : " + (Integer) text.getValue());
		// levelService.setHorizontalPlatformId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		//
		// verticalPlatformIndexSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setVerticalPlatformId : " + (Integer) text.getValue());
		// levelService.setVerticalPlatformId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// });
		// verticalPlatformIndexSpinner.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setVerticalPlatformId : " + (Integer) text.getValue());
		// levelService.setVerticalPlatformId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		//
		// backgroundIndexSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setBackgroundId : " + (Integer) text.getValue());
		// levelService.setBackgroundId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// });
		// backgroundIndexSpinner.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// LOG.info("setBackgroundId : " + (Integer) text.getValue());
		// levelService.setBackgroundId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		//
		// nextLevelIndexSpinner.addChangeListener(new ChangeListener() {
		// @Override
		// public void stateChanged(ChangeEvent e) {
		// JSpinner text = (JSpinner) e.getSource();
		// if (text.getValue() != null) {
		// levelService.setNextLevelId((Integer) text.getValue());
		// drawPanel.repaint();
		// }
		// LOG.info("NextLevel change : " + text.getValue());
		// }
		// });
		// nextLevelIndexSpinner.addKeyListener(new KeyListener() {
		// @Override
		// public void keyTyped(KeyEvent e) {
		// char vChar = e.getKeyChar();
		// if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar
		// == KeyEvent.VK_DELETE))) {
		// e.consume();
		// }
		// }
		//
		// @Override
		// public void keyPressed(KeyEvent e) {
		// }
		//
		// @Override
		// public void keyReleased(KeyEvent e) {
		// }
		// });
		//
		// showPlatformLevelCheckBox.addItemListener(new ItemListener() {
		// public void itemStateChanged(ItemEvent e) {
		// levelService.setShowPlatform(showPlatformLevelCheckBox.isSelected());
		// }
		// });
		//
		// englishLevelNameIndexTextField.getDocument().addDocumentListener(new
		// DocumentListener() {
		// private void updateData() {
		// levelService.setLevelName("en", englishLevelNameIndexTextField.getText());
		// }
		//
		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// });
		// frenchLevelNameIndexTextField.getDocument().addDocumentListener(new
		// DocumentListener() {
		// private void updateData() {
		// levelService.setLevelName("fr", frenchLevelNameIndexTextField.getText());
		// }
		//
		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// });
		// spanishLevelNameIndexTextField.getDocument().addDocumentListener(new
		// DocumentListener() {
		// private void updateData() {
		// levelService.setLevelName("es", spanishLevelNameIndexTextField.getText());
		// }
		//
		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// updateData();
		// }
		//
		// });
		//
		/*************************************************************************************
		 *
		 * --- ACTION ---
		 *
		 *************************************************************************************/
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

	private void release(int x, int y) {

		if (x > 400) {
			x = 400;
		}
		if (y > 500) {
			y = 500;
		}

		int caseX = (x - OFFSET) / EditorConstante.GRID_SIZE_X;
		int caseY = CoordinateUtils.clicGridY(y);
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
	}

	private void pressed(int x, int y) {
		if (x > 400) {
			x = 400;
		}
		if (y > 500) {
			y = 500;
		}

		int caseX = (x - OFFSET) / EditorConstante.GRID_SIZE_X;
		int caseY = CoordinateUtils.clicGridY(y);
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
	}

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
	}

	/*************************************************************************************
	 * 
	 * --- TREAT FUNCTION ---
	 * 
	 *************************************************************************************/

	private void addRail(int x, int y) {
		this.levelService2.addRail(x, y);
		repaint();
	}

	public void repaint() {
		this.drawPanel.repaint();
	}

	public void loadPropertiesLevel() {
		/*
		 * verticalPlatformIndexSpinner.setValue((Integer)
		 * levelService2.getVerticalPlatformId());
		 * horizontalPlatformIndexSpinner.setValue((Integer)
		 * levelService2.getHorizontalPlatformId());
		 * backgroundIndexSpinner.setValue((Integer) levelService2.getBackgroundId());
		 * nextLevelIndexSpinner.setValue((Integer) levelService2.getNextLevelId());
		 * showPlatformLevelCheckBox.setSelected(levelService2.isShowPlatform());
		 */
//		spanishLevelNameIndexTextField.setText(levelService2.getLevelName("es"));
//		englishLevelNameIndexTextField.setText(levelService2.getLevelName("en"));
//		frenchLevelNameIndexTextField.setText(levelService2.getLevelName("fr"));
	}
}
