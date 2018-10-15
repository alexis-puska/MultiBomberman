package com.mygdx.game.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.LayoutManager;
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
	private Border borderParameters;
	private JPanel verticalPlatformIndexPanel;
	private GridLayout verticalPlatformLayout;
	private Border verticalPlatformIndexBorder;
	private SpinnerNumberModel verticalPlatformIndexModel;
	private JSpinner verticalPlatformIndexSpinner;
	private JPanel backgroundIndexPanel;
	private GridLayout backgroundLayout;
	private Border backgroundIndexBorder;
	private SpinnerNumberModel backgroundIndexModel;
	private JSpinner backgroundIndexSpinner;
	private JPanel horizontalPlatformIndexPanel;
	private GridLayout horizontalPlatformLayout;
	private Border horizontalPlatformIndexBorder;
	private SpinnerNumberModel horizontalPlatformIndexModel;
	private JSpinner horizontalPlatformIndexSpinner;
	private JPanel nextLevelIndexPanel;
	private GridLayout nextLevelIndexLayout;
	private Border nextLevelIndexBorder;
	private SpinnerNumberModel nextLevelIndexSpinnerModel;
	private JSpinner nextLevelIndexSpinner;
	private JPanel showPlatformLevelPanel;
	private GridLayout showPlatformLevelLayout;
	private Border showPlatformLevelBorder;
	private JCheckBox showPlatformLevelCheckBox;
	private JPanel frenchLevelNameIndexPanel;
	private GridLayout frenchLevelNameIndexLayout;
	private Border frenchLevelNameIndexBorder;
	private JTextField frenchLevelNameIndexTextField;
	private JPanel englishLevelNameIndexPanel;
	private GridLayout englishLevelNameIndexLayout;
	private Border englishLevelNameIndexBorder;
	private JTextField englishLevelNameIndexTextField;
	private JPanel spanishLevelNameIndexPanel;
	private GridLayout spanishLevelNameIndexLayout;
	private Border spanishLevelNameIndexBorder;
	private JTextField spanishLevelNameIndexTextField;

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
	// currentLevelType
	private JPanel currentTypePanel;
	private GridLayout currentTypeLayout;
	private Border currentTypeBorder;
	private JLabel currentTypeLevel;
	private SpinnerNumberModel currentTypeLevelIndexModel;
	private JSpinner currentTypeLevelIndex;
	// currentLevelIndex
	private JPanel currentLevelPanel;
	private GridLayout currentLevelLayout;
	private Border currentLevelBorder;
	private JButton addLevel;
	private JButton delLevel;
	private SpinnerNumberModel currentLevelIndexModel;
	private JSpinner currentLevelIndex;
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
	 * ENNEMIES
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
		buildEnnemiePanelButton();
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
		eastPanel.add(panelParameters);
		eastPanel.add(texturePanel);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
	}

	private void buildEnnemiePanelButton() {
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
	}

	private void buildWestPanel() {
		westLayout.setRows(2);
		westPanel.setLayout(westLayout);
		this.getContentPane().add(buttonPanel, BorderLayout.WEST);
	}

	private void buildNavigationPanelButton() {
		currentLevelIndexModel.setMinimum(EditorConstante.MIN_LEVEL_ID);
		currentLevelIndex.setModel(currentLevelIndexModel);
		currentLevelPanel.setLayout(currentLevelLayout);
		currentLevelPanel.setBorder(currentLevelBorder);
		currentLevelPanel.add(currentLevelIndex);
		currentLevelPanel.add(addLevel);
		currentLevelPanel.add(delLevel);
		currentTypeLevelIndexModel.setMinimum(EditorConstante.MIN_TYPE_ID);
		currentTypeLevelIndexModel.setMaximum(EditorConstante.MAX_TYPE_ID);
		currentTypeLevelIndex.setModel(currentTypeLevelIndexModel);
		currentTypePanel.setLayout(currentTypeLayout);
		currentTypePanel.setBorder(currentTypeBorder);
		currentTypePanel.add(currentTypeLevel);
		currentTypePanel.add(currentTypeLevelIndex);
		filePanel.setLayout(fileLayout);
		filePanel.setBorder(fileBorder);
		filePanel.add(openLoadFileChooser);
		filePanel.add(openSaveFileChooser);
		panelNavigation.setBorder(borderNavigation);
		panelNavigation.setLayout(layoutNavigation);
		panelNavigation.add(filePanel);
		panelNavigation.add(currentTypePanel);
		panelNavigation.add(currentLevelPanel);
		this.getContentPane().add(panelNavigation, BorderLayout.NORTH);
	}

	private void buildParameterPanelButton() {
		verticalPlatformIndexModel.setMinimum(EditorConstante.MIN_PLATFORM_ID);
		verticalPlatformIndexModel.setMaximum(EditorConstante.MAX_PLATFORM_ID);
		verticalPlatformIndexSpinner.setModel(verticalPlatformIndexModel);
		verticalPlatformIndexPanel.setBorder(verticalPlatformIndexBorder);
		verticalPlatformIndexPanel.setLayout(verticalPlatformLayout);
		verticalPlatformIndexPanel.add(verticalPlatformIndexSpinner);
		horizontalPlatformIndexModel.setMinimum(EditorConstante.MIN_PLATFORM_ID);
		horizontalPlatformIndexModel.setMaximum(EditorConstante.MAX_PLATFORM_ID);
		horizontalPlatformIndexSpinner.setModel(horizontalPlatformIndexModel);
		horizontalPlatformIndexPanel.setBorder(horizontalPlatformIndexBorder);
		horizontalPlatformIndexPanel.setLayout(horizontalPlatformLayout);
		horizontalPlatformIndexPanel.add(horizontalPlatformIndexSpinner);
		backgroundIndexModel.setMinimum(EditorConstante.MIN_BACKGROUND_ID);
		backgroundIndexModel.setMaximum(EditorConstante.MAX_BACKGROUND_ID);
		backgroundIndexSpinner.setModel(backgroundIndexModel);
		backgroundIndexPanel.setBorder(backgroundIndexBorder);
		backgroundIndexPanel.setLayout(backgroundLayout);
		backgroundIndexPanel.add(backgroundIndexSpinner);
		nextLevelIndexPanel.setBorder(nextLevelIndexBorder);
		nextLevelIndexPanel.setLayout(nextLevelIndexLayout);
		nextLevelIndexSpinnerModel.setMinimum(-1);
		nextLevelIndexSpinner.setModel(nextLevelIndexSpinnerModel);
		nextLevelIndexPanel.add(nextLevelIndexSpinner);
		showPlatformLevelPanel.setBorder(showPlatformLevelBorder);
		showPlatformLevelPanel.setLayout(showPlatformLevelLayout);
		showPlatformLevelPanel.add(showPlatformLevelCheckBox);
		frenchLevelNameIndexPanel.setBorder(frenchLevelNameIndexBorder);
		frenchLevelNameIndexPanel.setLayout(frenchLevelNameIndexLayout);
		frenchLevelNameIndexPanel.add(frenchLevelNameIndexTextField);
		englishLevelNameIndexPanel.setBorder(englishLevelNameIndexBorder);
		englishLevelNameIndexPanel.setLayout(englishLevelNameIndexLayout);
		englishLevelNameIndexPanel.add(englishLevelNameIndexTextField);
		spanishLevelNameIndexPanel.setBorder(spanishLevelNameIndexBorder);
		spanishLevelNameIndexPanel.setLayout(spanishLevelNameIndexLayout);
		spanishLevelNameIndexPanel.add(spanishLevelNameIndexTextField);
		panelParameters.setBorder(borderParameters);
		// panelParameters.add(verticalPlatformIndexPanel);
		// panelParameters.add(horizontalPlatformIndexPanel);
		// panelParameters.add(backgroundIndexPanel);
		// panelParameters.add(nextLevelIndexPanel);
		// panelParameters.add(showPlatformLevelPanel);
		// panelParameters.add(frenchLevelNameIndexPanel);
		// panelParameters.add(englishLevelNameIndexPanel);
		// panelParameters.add(spanishLevelNameIndexPanel);

		JLabel enableLabel = new JLabel(message.getString("properties.platform.enable"), JLabel.TRAILING);
		panelParameters.setLayout(panelParametersLayout);
		JCheckBox enableCheckBox = new JCheckBox();
		enableLabel.setLabelFor(enableCheckBox);
		panelParameters.add(enableLabel);
		panelParameters.add(enableCheckBox);

		SpringUtilities.makeCompactGrid(panelParameters, 1, 2, 2, 2, 2, 2);
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
		currentLevelIndexModel = new SpinnerNumberModel();
		currentLevelIndex = new JSpinner();

		currentTypePanel = new JPanel();
		currentTypeLayout = new GridLayout();
		currentTypeBorder = BorderFactory.createTitledBorder(message.getString("type.border"));
		currentTypeLevel = new JLabel(message.getString("currentLevel.label"));
		currentTypeLevel.setToolTipText(message.getString("currentLevel.tooltip"));
		currentTypeLevelIndexModel = new SpinnerNumberModel();
		currentTypeLevelIndex = new JSpinner();
		currentTypeLevelIndex.setToolTipText(message.getString("currentLevel.tooltip"));

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
		
		removeHoleButton = new JButton(message.getString("editor.button.hole.remove"));
		removeRailButton = new JButton(message.getString("editor.button.rail.remove"));
		removeTrolleyButton = new JButton(message.getString("editor.button.trolley.remove"));
		removeInterrupteurButton = new JButton(message.getString("editor.button.interrupteur.remove"));
		removeMineButton = new JButton(message.getString("editor.button.mine.remove"));
		removeTeleporterButton = new JButton(message.getString("editor.button.teleporter.remove"));
		removeWallButton = new JButton(message.getString("editor.button.wall.remove"));
		removeStartPlayerButton = new JButton(message.getString("editor.button.startPlayer.remove"));
		
		

		// properties
		panelParameters = new JPanel();
		panelParametersLayout = new SpringLayout();
		// layoutParameters = new GridLayout();
		// layoutParameters.setColumns(EditorConstante.NB_COLUMN_PARAMETER);
		// layoutParameters.setRows(EditorConstante.NB_ROW_PARAMETER);

		borderParameters = BorderFactory.createTitledBorder("Properties");

		verticalPlatformIndexPanel = new JPanel();
		verticalPlatformLayout = new GridLayout();
		verticalPlatformIndexBorder = BorderFactory
				.createTitledBorder(message.getString("properties.border.platformVertical"));
		verticalPlatformIndexModel = new SpinnerNumberModel();
		verticalPlatformIndexSpinner = new JSpinner();
		backgroundIndexPanel = new JPanel();
		backgroundLayout = new GridLayout();
		backgroundIndexBorder = BorderFactory.createTitledBorder(message.getString("properties.border.background"));
		backgroundIndexModel = new SpinnerNumberModel();
		backgroundIndexSpinner = new JSpinner();
		horizontalPlatformIndexPanel = new JPanel();
		horizontalPlatformLayout = new GridLayout();
		horizontalPlatformIndexBorder = BorderFactory
				.createTitledBorder(message.getString("properties.border.platformHorizontal"));
		horizontalPlatformIndexModel = new SpinnerNumberModel();
		horizontalPlatformIndexSpinner = new JSpinner();
		nextLevelIndexPanel = new JPanel();
		nextLevelIndexLayout = new GridLayout();
		nextLevelIndexBorder = BorderFactory.createTitledBorder(message.getString("properties.border.nextLevel"));
		nextLevelIndexSpinnerModel = new SpinnerNumberModel();
		nextLevelIndexSpinner = new JSpinner();
		showPlatformLevelPanel = new JPanel();
		showPlatformLevelLayout = new GridLayout();
		showPlatformLevelBorder = BorderFactory.createTitledBorder(message.getString("properties.border.showPlatform"));
		showPlatformLevelCheckBox = new JCheckBox();
		frenchLevelNameIndexPanel = new JPanel();
		frenchLevelNameIndexLayout = new GridLayout();
		frenchLevelNameIndexBorder = BorderFactory
				.createTitledBorder(message.getString("properties.border.frenchName"));
		frenchLevelNameIndexTextField = new JTextField();
		englishLevelNameIndexPanel = new JPanel();
		englishLevelNameIndexLayout = new GridLayout();
		englishLevelNameIndexBorder = BorderFactory
				.createTitledBorder(message.getString("properties.border.englishName"));
		englishLevelNameIndexTextField = new JTextField();
		spanishLevelNameIndexPanel = new JPanel();
		spanishLevelNameIndexLayout = new GridLayout();
		spanishLevelNameIndexBorder = BorderFactory
				.createTitledBorder(message.getString("properties.border.spanishName"));
		spanishLevelNameIndexTextField = new JTextField();

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
		// /*************************************************************************************
		// *
		// * --- ENNEMIE ---
		// *
		// *************************************************************************************/
		// ceriseButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_CERISE;
		// }
		// });
		// orangeButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_ORANGE;
		// }
		// });
		// pommeButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_POMME;
		// }
		// });
		// bananeButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_BANANE;
		// }
		// });
		// litchiButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_LITCHI;
		// }
		// });
		// fraiseButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_FRAISE;
		// }
		// });
		// framboiseButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_FRAMBOISE;
		// }
		// });
		// citronButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_CITRON;
		// }
		// });
		// abricotButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_ABRICOT;
		// }
		// });
		// abricotnainsButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_ABRICOT_NAIN;
		// }
		// });
		// annanasButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_ANANAS;
		// }
		// });
		// kiwiButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_KIWI;
		// }
		// });
		// pastequeButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_PASTEQUE;
		// }
		// });
		// pruneButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_PRUNE;
		// }
		// });
		// scieButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_SCIE;
		// }
		// });
		// poireButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_POIRE;
		// }
		// });
		// blobButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_BLOB;
		// }
		// });
		//
		// /*************************************************************************************
		// *
		// * --- ELEMENT ---
		// *
		// *************************************************************************************/
		// selectButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.SELECT;
		// }
		// });
		// deleteButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.DELETE;
		// }
		// });
		// verticalPlatformButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.DRAW_VERTICAL_PLATFORM;
		// }
		// });
		// horizontalPlatformButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.DRAW_HORIZONTAL_PLATFORM;
		// }
		// });
		// vortexButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_VORTEX;
		// }
		// });
		// teleporterButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_TELEPORTER;
		// }
		// });
		// rayonButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_RAYON;
		// }
		// });
		// pickButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_PICK;
		// }
		// });
		// lockButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_LOCK;
		// }
		// });
		// doorButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_DOOR;
		// }
		// });
		// eventButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_EVENT;
		// }
		// });
		// startButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_PLAYER_SPAWN;
		// }
		// });
		// pointButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_OBJECT_POINT;
		// }
		// });
		// effectButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_OBJECT_EFFECT;
		// }
		// });
		// decorButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_DECOR;
		// }
		// });
		// itemButton.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// action = ActionEnum.ADD_ITEM;
		// }
		// });
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
		spanishLevelNameIndexTextField.setText(levelService2.getLevelName("es"));
		englishLevelNameIndexTextField.setText(levelService2.getLevelName("en"));
		frenchLevelNameIndexTextField.setText(levelService2.getLevelName("fr"));
	}
}
