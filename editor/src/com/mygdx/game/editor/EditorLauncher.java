package com.mygdx.game.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mygdx.constante.EditorConstante;
import com.mygdx.game.editor.constant.ActionEnum;
import com.mygdx.game.editor.constant.EnnemieTypeEnum;
import com.mygdx.game.editor.domain.level.Decor;
import com.mygdx.game.editor.domain.level.Door;
import com.mygdx.game.editor.domain.level.Ennemie;
import com.mygdx.game.editor.domain.level.Identifiable;
import com.mygdx.game.editor.domain.level.Item;
import com.mygdx.game.editor.domain.level.LevelFile;
import com.mygdx.game.editor.domain.level.Lock;
import com.mygdx.game.editor.domain.level.Pick;
import com.mygdx.game.editor.domain.level.Platform;
import com.mygdx.game.editor.domain.level.Rayon;
import com.mygdx.game.editor.domain.level.Teleporter;
import com.mygdx.game.editor.domain.level.Vortex;
import com.mygdx.game.editor.domain.level.event.Event;
import com.mygdx.game.editor.service.FileService;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.service.SpriteService;
import com.mygdx.game.editor.utils.CoordinateUtils;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.BackgroundDrawPanel;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.PlatformDrawPanel;
import com.mygdx.game.editor.view.properties.DecorPanel;
import com.mygdx.game.editor.view.properties.DoorPanel;
import com.mygdx.game.editor.view.properties.EnnemiePanel;
import com.mygdx.game.editor.view.properties.EventPanel;
import com.mygdx.game.editor.view.properties.ItemPanel;
import com.mygdx.game.editor.view.properties.LockPanel;
import com.mygdx.game.editor.view.properties.PickPanel;
import com.mygdx.game.editor.view.properties.PlatformPanel;
import com.mygdx.game.editor.view.properties.RayonPanel;
import com.mygdx.game.editor.view.properties.TeleporterPanel;
import com.mygdx.game.editor.view.properties.VortexPanel;
import com.mygdx.game.editor.view.properties.renderer.IdentifiableComboBoxRenderer;

public class EditorLauncher extends JFrame {

	private static final Logger LOG = LogManager.getLogger(EditorLauncher.class);
	private static final long serialVersionUID = -8256444946608935363L;
	private static final int OFFSET = 10;

	// services
	private final FileService fileService;
	private final SpriteService spriteService;
	private final LevelService levelService;

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
	private JPanel platformPanel;
	private Border platformBorder;
	private PlatformDrawPanel platformDrawPanel;
	private JPanel backgroundPanel;
	private Border backgroundBorder;
	private BackgroundDrawPanel backgroundDrawPanel;

	/****************
	 * DRAW
	 ***************/
	private JPanel centerPanel;
	private BorderLayout drawLayout;
	private DrawPanel drawPanel;
	private JFrame eventJFrame;
	private JFrame mapViewJFrame;

	/****************
	 * properties
	 ***************/
	private JPanel editionIdentifiablePanel;
	private BorderLayout editionIdentifiableLayout;
	private JPanel selectionIdentifiablePanel;
	private Border selectionIdentifiableBorder;
	private SpringLayout selectionIdentifiableLayout;
	private JLabel selectionIdentifiableLabel;
	private JComboBox<Identifiable> selectionIdentifiableComboBox;
	private JPanel identifiablePropertiesPanel;

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
	private JPanel panelEnnemies;
	private Border borderEnnemies;
	private GridLayout layoutEnnemies;
	private JButton ceriseButton;
	private JButton orangeButton;
	private JButton pommeButton;
	private JButton bananeButton;
	private JButton litchiButton;
	private JButton fraiseButton;
	private JButton framboiseButton;
	private JButton citronButton;
	private JButton abricotButton;
	private JButton abricotnainsButton;
	private JButton annanasButton;
	private JButton kiwiButton;
	private JButton pastequeButton;
	private JButton pruneButton;
	private JButton scieButton;
	private JButton poireButton;
	private JButton blobButton;

	/****************
	 * ELEMENTS
	 ***************/
	private JPanel panelElement;
	private Border borderElement;
	private GridLayout layoutElement;
	private JButton selectButton;
	private JButton deleteButton;
	private JButton verticalPlatformButton;
	private JButton horizontalPlatformButton;
	private JButton vortexButton;
	private JButton teleporterButton;
	private JButton rayonButton;
	private JButton pickButton;
	private JButton doorButton;
	private JButton lockButton;
	private JButton eventButton;
	private JButton startButton;
	private JButton pointButton;
	private JButton effectButton;
	private JButton decorButton;
	private JButton itemButton;

	/********************
	 * LEVEL PROPERTIES
	 *******************/
	private JPanel panelParameters;
	private GridLayout layoutParameters;
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
		this.fileService = new FileService();
		this.spriteService = new SpriteService(fileService);
		this.levelService = new LevelService();

		LevelFile levelFile = new LevelFile();
		levelFile.setType(new ArrayList<>());
		for (int i = 0; i <= EditorConstante.MAX_TYPE_ID; i++) {
			com.mygdx.game.editor.domain.level.Type type = new com.mygdx.game.editor.domain.level.Type();
			type.setId(i);
			type.setLevel(new ArrayList<>());
			levelFile.getType().add(type);
		}
		this.levelService.putLevelFile(levelFile);
		this.action = ActionEnum.SELECT;
	}

	private void Launch() {
		LOG.info("Nb level in file : " + levelService.getNbLevel());
		this.getContentPane().setLayout(new BorderLayout());
		initComponent();
		initListeners();
		buildWestPanel();
		buildEastPanel();
		buildDrawElement();
		buildNavigationPanelButton();
		buildElementPanelButton();
		buildEnnemiePanelButton();
		buildParameterPanelButton();
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
	private void buildIdentifiablePanelEdition(List<Identifiable> objs) {
		selectionIdentifiableLabel = new JLabel(message.getString("selectionIdentifiable.border"));
		selectionIdentifiableComboBox = new JComboBox<>();
		selectionIdentifiableBorder = BorderFactory
				.createTitledBorder(message.getString("selectionIdentifiable.combo"));
		selectionIdentifiableLayout = new SpringLayout();
		selectionIdentifiablePanel = new JPanel();
		selectionIdentifiablePanel.setBorder(selectionIdentifiableBorder);
		selectionIdentifiablePanel.setLayout(selectionIdentifiableLayout);
		selectionIdentifiablePanel.add(selectionIdentifiableLabel);
		selectionIdentifiablePanel.add(selectionIdentifiableComboBox);
		SpringUtilities.makeCompactGrid(selectionIdentifiablePanel, 1, 2, 6, 6, 6, 6);
		editionIdentifiableLayout = new BorderLayout();
		editionIdentifiablePanel = new JPanel();
		editionIdentifiablePanel.setLayout(editionIdentifiableLayout);
		editionIdentifiablePanel.add(selectionIdentifiablePanel, BorderLayout.NORTH);
		selectionIdentifiableComboBox.setRenderer(new IdentifiableComboBoxRenderer(message));
		selectionIdentifiableComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Identifiable identifiable = (Identifiable) selectionIdentifiableComboBox.getSelectedItem();
					if (identifiable != null) {
						if (eventJFrame != null && eventJFrame.isVisible()) {
							eventJFrame.dispose();
						}
						if (mapViewJFrame != null && mapViewJFrame.isVisible()) {
							mapViewJFrame.dispose();
						}
						switchIdentifiable(identifiable);
					}
				}
			}
		});
		for (Identifiable identifiable : objs) {
			selectionIdentifiableComboBox.addItem(identifiable);
		}
		centerPanel.add(editionIdentifiablePanel, BorderLayout.SOUTH);
	}

	private void buildDrawElement() {
		drawPanel.setSize(EditorConstante.SCREEN_SIZE_X, EditorConstante.SCREEN_SIZE_Y);
		drawPanel.setVisible(true);
		centerPanel.setBackground(Color.LIGHT_GRAY);
		centerPanel.setLayout(drawLayout);
		centerPanel.add(drawPanel, BorderLayout.CENTER);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
	}

	private void buildWestPanel() {
		westLayout.setRows(2);
		westPanel.setLayout(westLayout);
		this.getContentPane().add(westPanel, BorderLayout.WEST);
	}

	private void buildEastPanel() {
		platformDrawPanel.setSize(EditorConstante.PANEL_PLATFORM_BACKGROUD_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		platformDrawPanel.setVisible(true);
		platformPanel.setBorder(platformBorder);
		platformPanel.add(platformDrawPanel);
		backgroundDrawPanel.setSize(EditorConstante.PANEL_PLATFORM_BACKGROUD_WIDTH, EditorConstante.SCREEN_SIZE_Y);
		backgroundDrawPanel.setVisible(true);
		backgroundPanel.setBorder(backgroundBorder);
		backgroundPanel.add(backgroundDrawPanel);
		eastLayout.setColumns(2);
		eastPanel.setLayout(eastLayout);
		eastPanel.add(platformPanel);
		eastPanel.add(backgroundPanel);
		this.getContentPane().add(eastPanel, BorderLayout.EAST);
	}

	private void buildEnnemiePanelButton() {
		panelEnnemies.setBorder(borderEnnemies);
		panelEnnemies.setLayout(layoutEnnemies);
		panelEnnemies.add(ceriseButton);
		panelEnnemies.add(orangeButton);
		panelEnnemies.add(pommeButton);
		panelEnnemies.add(bananeButton);
		panelEnnemies.add(litchiButton);
		panelEnnemies.add(fraiseButton);
		panelEnnemies.add(framboiseButton);
		panelEnnemies.add(citronButton);
		panelEnnemies.add(abricotButton);
		panelEnnemies.add(abricotnainsButton);
		panelEnnemies.add(annanasButton);
		panelEnnemies.add(kiwiButton);
		panelEnnemies.add(pastequeButton);
		panelEnnemies.add(pruneButton);
		panelEnnemies.add(scieButton);
		panelEnnemies.add(poireButton);
		panelEnnemies.add(blobButton);
		westPanel.add(panelEnnemies);
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

	private void buildElementPanelButton() {
		panelElement.setBorder(borderElement);
		panelElement.setLayout(layoutElement);
		panelElement.add(selectButton);
		panelElement.add(deleteButton);
		panelElement.add(verticalPlatformButton);
		panelElement.add(horizontalPlatformButton);
		panelElement.add(vortexButton);
		panelElement.add(teleporterButton);
		panelElement.add(rayonButton);
		panelElement.add(pickButton);
		panelElement.add(doorButton);
		panelElement.add(lockButton);
		panelElement.add(eventButton);
		panelElement.add(startButton);
		panelElement.add(pointButton);
		panelElement.add(effectButton);
		panelElement.add(decorButton);
		panelElement.add(itemButton);
		westPanel.add(panelElement);
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
		panelParameters.setLayout(layoutParameters);
		panelParameters.setBorder(borderParameters);
		panelParameters.add(verticalPlatformIndexPanel);
		panelParameters.add(horizontalPlatformIndexPanel);
		panelParameters.add(backgroundIndexPanel);
		panelParameters.add(nextLevelIndexPanel);
		panelParameters.add(showPlatformLevelPanel);
		panelParameters.add(frenchLevelNameIndexPanel);
		panelParameters.add(englishLevelNameIndexPanel);
		panelParameters.add(spanishLevelNameIndexPanel);
		this.getContentPane().add(panelParameters, BorderLayout.SOUTH);
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
		platformPanel = new JPanel();
		platformBorder = BorderFactory.createTitledBorder(message.getString("platform.border"));
		platformDrawPanel = new PlatformDrawPanel(spriteService);
		backgroundPanel = new JPanel();
		backgroundBorder = BorderFactory.createTitledBorder(message.getString("background.border"));
		backgroundDrawPanel = new BackgroundDrawPanel(spriteService);

		// draw
		centerPanel = new JPanel();
		drawLayout = new BorderLayout();
		drawPanel = new DrawPanel(spriteService, levelService);

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
		panelEnnemies = new JPanel();
		borderEnnemies = BorderFactory.createTitledBorder(message.getString("ennemie.border"));
		layoutEnnemies = new GridLayout();
		layoutEnnemies.setColumns(EditorConstante.NB_COLUMN_ENNEMIE);
		layoutEnnemies.setRows(EditorConstante.NB_ROW_ENNEMIE);
		ceriseButton = new JButton(message.getString("ennemie.type.cerise"));
		orangeButton = new JButton(message.getString("ennemie.type.orange"));
		pommeButton = new JButton(message.getString("ennemie.type.pomme"));
		litchiButton = new JButton(message.getString("ennemie.type.litchi"));
		fraiseButton = new JButton(message.getString("ennemie.type.fraise"));
		framboiseButton = new JButton(message.getString("ennemie.type.framboise"));
		citronButton = new JButton(message.getString("ennemie.type.citron"));
		abricotButton = new JButton(message.getString("ennemie.type.abricot"));
		abricotnainsButton = new JButton(message.getString("ennemie.type.nainBricot"));
		annanasButton = new JButton(message.getString("ennemie.type.annanas"));
		kiwiButton = new JButton(message.getString("ennemie.type.kiwi"));
		pastequeButton = new JButton(message.getString("ennemie.type.pasteque"));
		pruneButton = new JButton(message.getString("ennemie.type.prune"));
		scieButton = new JButton(message.getString("ennemie.type.scie"));
		poireButton = new JButton(message.getString("ennemie.type.poire"));
		blobButton = new JButton(message.getString("ennemie.type.blob"));
		bananeButton = new JButton(message.getString("ennemie.type.banane"));

		// element
		panelElement = new JPanel();
		borderElement = BorderFactory.createTitledBorder(message.getString("element.border"));
		layoutElement = new GridLayout();
		layoutElement.setColumns(EditorConstante.COL_ELEMENT_PANEL);
		layoutElement.setRows(EditorConstante.ROW_ELEMENT_PANEL);
		selectButton = new JButton(message.getString("element.button.select"));
		deleteButton = new JButton(message.getString("element.button.delete"));
		verticalPlatformButton = new JButton(message.getString("element.button.vpf"));
		horizontalPlatformButton = new JButton(message.getString("element.button.hpf"));
		vortexButton = new JButton(message.getString("element.button.vortex"));
		teleporterButton = new JButton(message.getString("element.button.teleporter"));
		rayonButton = new JButton(message.getString("element.button.rayon"));
		pickButton = new JButton(message.getString("element.button.pick"));
		doorButton = new JButton(message.getString("element.button.door"));
		lockButton = new JButton(message.getString("element.button.lock"));
		eventButton = new JButton(message.getString("element.button.event"));
		startButton = new JButton(message.getString("element.button.startPlayer"));
		pointButton = new JButton(message.getString("element.button.point"));
		effectButton = new JButton(message.getString("element.button.effect"));
		decorButton = new JButton(message.getString("element.button.decor"));
		itemButton = new JButton(message.getString("element.button.item"));

		// properties
		panelParameters = new JPanel();
		layoutParameters = new GridLayout();
		layoutParameters.setColumns(EditorConstante.NB_COLUMN_PARAMETER);
		layoutParameters.setRows(EditorConstante.NB_ROW_PARAMETER);
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

		/*************************************************************************************
		 *
		 * --- NAVIGATION ---
		 * 
		 *************************************************************************************/
		currentLevelIndex.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					LOG.info("ChangeLevel : " + (Integer) text.getValue());
					levelService.setCurrentLevelIndex((Integer) text.getValue());
					loadPropertiesLevel();
					drawPanel.repaint();
				}
			}
		});
		currentLevelIndex.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					JSpinner text = (JSpinner) e.getSource();
					if (text.getValue() != null) {
						LOG.info("ChangeLevel : " + (Integer) text.getValue());
						levelService.setCurrentLevelIndex((Integer) text.getValue());
						loadPropertiesLevel();
						drawPanel.repaint();
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
		currentTypeLevelIndex.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					LOG.info("Change Type : " + (Integer) text.getValue());
					levelService.setCurrentTypeIndex((Integer) text.getValue());
					currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
					centerPanel.updateUI();
					loadPropertiesLevel();
					drawPanel.repaint();
				}
			}
		});
		currentTypeLevelIndex.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					JSpinner text = (JSpinner) e.getSource();
					if (text.getValue() != null) {
						LOG.info("Change Type : " + (Integer) text.getValue());
						levelService.setCurrentTypeIndex((Integer) text.getValue());
						currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
						centerPanel.updateUI();
						loadPropertiesLevel();
						drawPanel.repaint();
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
		addLevel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelService.createLevel();
				loadPropertiesLevel();
				repaint();
			}
		});
		delLevel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				levelService.deleteLevel();
				loadPropertiesLevel();
				repaint();
			}
		});
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
//					try {
//						levelService.putLevelFile(
//								fileService.readJsonFile(new FileInputStream(new File(absolutePathFile))));
//						currentLevelIndex.setValue((Integer) levelService.getCurrentLevelIndex());
//						currentTypeLevelIndex.setValue((Integer) levelService.getCurrentTypeIndex());
//						centerPanel.updateUI();
//						loadPropertiesLevel();
//						repaint();
//					} catch (FileNotFoundException e) {
//						LOG.error("", e.getMessage());
//					}
					System.out.println(
							"You chose to open this file: " + loadFileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});

		openSaveFileChooser.addActionListener(arg0 -> {
			int returnVal = saveFileChooser.showSaveDialog(panelNavigation);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				LevelFile levelFile = levelService.getLevelFile();
//				absolutePathFile = saveFileChooser.getSelectedFile().getAbsolutePath();
//				if (!absolutePathFile.endsWith(".json")) {
//					absolutePathFile += ".json";
//				}
//				fileService.writeJson(levelFile, new File(absolutePathFile));
//				centerPanel.updateUI();
//				loadPropertiesLevel();
//				repaint();
//				System.out.println("You chose to open this file: " + absolutePathFile);
			}
		});
		/*************************************************************************************
		 *
		 * --- PROPERTIES LEVEL ---
		 * 
		 *************************************************************************************/

		horizontalPlatformIndexSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					LOG.info("setHorizontalPlatformId : " + (Integer) text.getValue());
					levelService.setHorizontalPlatformId((Integer) text.getValue());
					drawPanel.repaint();
				}
			}
		});
		horizontalPlatformIndexSpinner.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					JSpinner text = (JSpinner) e.getSource();
					if (text.getValue() != null) {
						LOG.info("setHorizontalPlatformId : " + (Integer) text.getValue());
						levelService.setHorizontalPlatformId((Integer) text.getValue());
						drawPanel.repaint();
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

		verticalPlatformIndexSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					LOG.info("setVerticalPlatformId : " + (Integer) text.getValue());
					levelService.setVerticalPlatformId((Integer) text.getValue());
					drawPanel.repaint();
				}
			}
		});
		verticalPlatformIndexSpinner.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					JSpinner text = (JSpinner) e.getSource();
					if (text.getValue() != null) {
						LOG.info("setVerticalPlatformId : " + (Integer) text.getValue());
						levelService.setVerticalPlatformId((Integer) text.getValue());
						drawPanel.repaint();
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

		backgroundIndexSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					LOG.info("setBackgroundId : " + (Integer) text.getValue());
					levelService.setBackgroundId((Integer) text.getValue());
					drawPanel.repaint();
				}
			}
		});
		backgroundIndexSpinner.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
					JSpinner text = (JSpinner) e.getSource();
					if (text.getValue() != null) {
						LOG.info("setBackgroundId : " + (Integer) text.getValue());
						levelService.setBackgroundId((Integer) text.getValue());
						drawPanel.repaint();
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

		nextLevelIndexSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					levelService.setNextLevelId((Integer) text.getValue());
					drawPanel.repaint();
				}
				LOG.info("NextLevel change : " + text.getValue());
			}
		});
		nextLevelIndexSpinner.addKeyListener(new KeyListener() {
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

		showPlatformLevelCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				levelService.setShowPlatform(showPlatformLevelCheckBox.isSelected());
			}
		});

		englishLevelNameIndexTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService.setLevelName("en", englishLevelNameIndexTextField.getText());
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
		frenchLevelNameIndexTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService.setLevelName("fr", frenchLevelNameIndexTextField.getText());
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
		spanishLevelNameIndexTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				levelService.setLevelName("es", spanishLevelNameIndexTextField.getText());
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

		/*************************************************************************************
		 *
		 * --- ENNEMIE ---
		 * 
		 *************************************************************************************/
		ceriseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_CERISE;
			}
		});
		orangeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_ORANGE;
			}
		});
		pommeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_POMME;
			}
		});
		bananeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_BANANE;
			}
		});
		litchiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_LITCHI;
			}
		});
		fraiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_FRAISE;
			}
		});
		framboiseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_FRAMBOISE;
			}
		});
		citronButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_CITRON;
			}
		});
		abricotButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_ABRICOT;
			}
		});
		abricotnainsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_ABRICOT_NAIN;
			}
		});
		annanasButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_ANANAS;
			}
		});
		kiwiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_KIWI;
			}
		});
		pastequeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_PASTEQUE;
			}
		});
		pruneButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_PRUNE;
			}
		});
		scieButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_SCIE;
			}
		});
		poireButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_POIRE;
			}
		});
		blobButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_BLOB;
			}
		});

		/*************************************************************************************
		 *
		 * --- ELEMENT ---
		 * 
		 *************************************************************************************/
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.SELECT;
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.DELETE;
			}
		});
		verticalPlatformButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.DRAW_VERTICAL_PLATFORM;
			}
		});
		horizontalPlatformButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.DRAW_HORIZONTAL_PLATFORM;
			}
		});
		vortexButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_VORTEX;
			}
		});
		teleporterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_TELEPORTER;
			}
		});
		rayonButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_RAYON;
			}
		});
		pickButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_PICK;
			}
		});
		lockButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_LOCK;
			}
		});
		doorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_DOOR;
			}
		});
		eventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_EVENT;
			}
		});
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_PLAYER_SPAWN;
			}
		});
		pointButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_OBJECT_POINT;
			}
		});
		effectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_OBJECT_EFFECT;
			}
		});
		decorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_DECOR;
			}
		});
		itemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				action = ActionEnum.ADD_ITEM;
			}
		});
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

		int caseX = (x - OFFSET) / EditorConstante.GRID_SIZE;
		int caseY = CoordinateUtils.clicGridY(y);
		int invY = CoordinateUtils.clickY(y);

		switch (action) {
		case DRAW_VERTICAL_PLATFORM:
			addVerticalPlatform(xFirst, yFirst, caseY);
			break;
		case DRAW_HORIZONTAL_PLATFORM:
			addHorizontalPlatform(xFirst, yFirst, caseX);
			break;
		case ADD_TELEPORTER:
			addTeleporter(xFirst, yFirst, caseX, caseY);
			break;
		case ADD_RAYON:
			addRayon(xFirst, yFirst, caseX, caseY);
			break;
		case SELECT:
		case DELETE:
			break;
		case ADD_ITEM:
			this.addItem(caseX, caseY);
			break;
		case ADD_DECOR:
			this.addDecor(x, invY);
			break;
		case ADD_LOCK:
			this.addLock(caseX, caseY);
			break;
		case ADD_DOOR:
			this.addDoor(caseX, caseY);
			break;
		case ADD_VORTEX:
			this.addVortex(caseX, caseY);
			break;
		case ADD_PICK:
			this.addPick(caseX, caseY);
			break;
		case ADD_EVENT:
			this.addEvent(caseX, caseY);
			break;
		case ADD_PLAYER_SPAWN:
			this.addPlayerSpawn(caseX, caseY);
			break;
		case ADD_OBJECT_POINT:
			this.addObjectPoint(caseX, caseY);
			break;
		case ADD_OBJECT_EFFECT:
			this.addObjectEffect(caseX, caseY);
			break;
		case ADD_CERISE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.CERISE);
			break;
		case ADD_ORANGE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.ORANGE);
			break;
		case ADD_POMME:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.POMME);
			break;
		case ADD_BANANE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.BANANE);
			break;
		case ADD_LITCHI:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.LITCHI);
			break;
		case ADD_FRAISE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.FRAISE);
			break;
		case ADD_FRAMBOISE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.FRAMBOISE);
			break;
		case ADD_CITRON:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.CITRON);
			break;
		case ADD_ABRICOT:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.ABRICOT);
			break;
		case ADD_ABRICOT_NAIN:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.ABRICOT_NAIN);
			break;
		case ADD_ANANAS:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.ANNANAS);
			break;
		case ADD_KIWI:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.KIWI);
			break;
		case ADD_PASTEQUE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.PASTEQUE);
			break;
		case ADD_PRUNE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.PRUNE);
			break;
		case ADD_SCIE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.SCIE);
			break;
		case ADD_POIRE:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.POIRE);
			break;
		case ADD_BLOB:
			this.addEnnemie(caseX, caseY, EnnemieTypeEnum.BLOB);
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

		int caseX = (x - OFFSET) / EditorConstante.GRID_SIZE;
		int caseY = CoordinateUtils.clicGridY(y);
		switch (action) {
		case DRAW_VERTICAL_PLATFORM:
		case DRAW_HORIZONTAL_PLATFORM:
		case ADD_TELEPORTER:
		case ADD_RAYON:
			xFirst = caseX;
			yFirst = caseY;
			break;
		case SELECT:
		case DELETE:
		case ADD_DECOR:
		case ADD_ITEM:
		case ADD_VORTEX:
		case ADD_PICK:
		case ADD_DOOR:
		case ADD_LOCK:
		case ADD_EVENT:
		case ADD_PLAYER_SPAWN:
		case ADD_OBJECT_POINT:
		case ADD_OBJECT_EFFECT:
		case ADD_CERISE:
		case ADD_ORANGE:
		case ADD_POMME:
		case ADD_BANANE:
		case ADD_LITCHI:
		case ADD_FRAISE:
		case ADD_FRAMBOISE:
		case ADD_CITRON:
		case ADD_ABRICOT:
		case ADD_ABRICOT_NAIN:
		case ADD_ANANAS:
		case ADD_KIWI:
		case ADD_PASTEQUE:
		case ADD_PRUNE:
		case ADD_SCIE:
		case ADD_BLOB:
		case ADD_POIRE:
			break;
		}
	}

	private void click(int x, int y) {
		int invY = CoordinateUtils.clickY(y);
		switch (action) {
		case SELECT:
			selectElement(x - OFFSET, invY);
			break;
		case DELETE:
			deleteElement(x - OFFSET, invY);
			break;
		case DRAW_VERTICAL_PLATFORM:
		case DRAW_HORIZONTAL_PLATFORM:
		case ADD_VORTEX:
		case ADD_ITEM:
		case ADD_DECOR:
		case ADD_TELEPORTER:
		case ADD_RAYON:
		case ADD_PICK:
		case ADD_DOOR:
		case ADD_LOCK:
		case ADD_EVENT:
		case ADD_PLAYER_SPAWN:
		case ADD_OBJECT_POINT:
		case ADD_OBJECT_EFFECT:
		case ADD_CERISE:
		case ADD_ORANGE:
		case ADD_POMME:
		case ADD_BANANE:
		case ADD_LITCHI:
		case ADD_FRAISE:
		case ADD_FRAMBOISE:
		case ADD_CITRON:
		case ADD_ABRICOT:
		case ADD_ABRICOT_NAIN:
		case ADD_ANANAS:
		case ADD_KIWI:
		case ADD_PASTEQUE:
		case ADD_PRUNE:
		case ADD_SCIE:
		case ADD_BLOB:
		case ADD_POIRE:
			break;
		}

	}

	/*************************************************************************************
	 * 
	 * --- TREAT FUNCTION ---
	 * 
	 *************************************************************************************/

	private void addRayon(int x, int y, int x2, int y2) {
		levelService.addRayon(x, y, x2, y2);
		repaint();
	}

	private void addTeleporter(int x, int y, int x2, int y2) {
		levelService.addTeleporter(x, y, x2, y2);
		repaint();
	}

	private void addHorizontalPlatform(int x, int y, int x2) {
		levelService.addPlatform(x, y, x2, false);
		repaint();
	}

	private void addVerticalPlatform(int x, int y, int y2) {
		levelService.addPlatform(x, y, y2, true);
		repaint();
	}

	private void addEnnemie(int x, int y, EnnemieTypeEnum type) {
		levelService.addEnnemie(x, y, type);
		repaint();
	}

	private void addDecor(int x, int y) {
		levelService.addDecor(x, y);
		repaint();
	}

	private void addVortex(int x, int y) {
		levelService.addVortex(x, y);
		repaint();
	}

	private void addPick(int x, int y) {
		levelService.addPick(x, y);
		repaint();
	}

	private void addDoor(int x, int y) {
		levelService.addDoor(x, y);
		repaint();
	}

	private void addLock(int x, int y) {
		levelService.addLock(x, y);
		repaint();
	}

	private void addEvent(int x, int y) {
		levelService.addEvent(x, y);
		repaint();
	}

	private void addPlayerSpawn(int x, int y) {
		levelService.addPlayerSpawn(x, y);
		repaint();
	}

	private void addObjectPoint(int x, int y) {
		levelService.addObjectPoint(x, y);
		repaint();
	}

	private void addObjectEffect(int x, int y) {
		levelService.addObjectEffect(x, y);
		repaint();
	}

	private void addItem(int x, int y) {
		levelService.addItem(x, y);
		repaint();
	}

	private void deleteElement(int x, int y) {
		levelService.deleteElement(x, y);
		repaint();
	}

	private void selectElement(int x, int y) {
		List<Identifiable> objs = levelService.getProperties(x, y);
		if (editionIdentifiablePanel != null) {
			centerPanel.remove(editionIdentifiablePanel);
		}
		if (eventJFrame != null && eventJFrame.isVisible()) {
			eventJFrame.dispose();
		}
		if (mapViewJFrame != null && mapViewJFrame.isVisible()) {
			mapViewJFrame.dispose();
		}

		if (objs != null && !objs.isEmpty()) {
			buildIdentifiablePanelEdition(objs);
			// switchIdentifiable(objs.get(0));
		} else {
			centerPanel.updateUI();
		}
	}

	private void switchIdentifiable(Identifiable obj) {
		if (obj.getClass().equals(Item.class)) {
			treatItemProperties((Item) obj);
		} else if (obj.getClass().equals(Decor.class)) {
			treatDecorProperties((Decor) obj);
		} else if (obj.getClass().equals(Door.class)) {
			treatDoorProperties((Door) obj);
		} else if (obj.getClass().equals(Ennemie.class)) {
			treatEnnemieProperties((Ennemie) obj);
		} else if (obj.getClass().equals(Event.class)) {
			treatEventProperties((Event) obj);
		} else if (obj.getClass().equals(Lock.class)) {
			treatLockProperties((Lock) obj);
		} else if (obj.getClass().equals(Pick.class)) {
			treatPickProperties((Pick) obj);
		} else if (obj.getClass().equals(Platform.class)) {
			treatPlatformProperties((Platform) obj);
		} else if (obj.getClass().equals(Rayon.class)) {
			treatRayonProperties((Rayon) obj);
		} else if (obj.getClass().equals(Teleporter.class)) {
			treatTeleporterProperties((Teleporter) obj);
		} else if (obj.getClass().equals(Vortex.class)) {
			treatVortexProperties((Vortex) obj);
		}
	}

	private void treatItemProperties(Item item) {
		identifiablePropertiesPanel = new ItemPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.item.border"), item);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatEventProperties(Event event) {
		eventJFrame = new JFrame(message.getString("properties.event.title") + event.getId());
		eventJFrame.getContentPane().setLayout(new BorderLayout());
		identifiablePropertiesPanel = new EventPanel(message, eventJFrame, drawPanel, levelService,
				message.getString("properties.event.title"), event);
		eventJFrame.add(identifiablePropertiesPanel);
		eventJFrame.setLocationRelativeTo(null);
		eventJFrame.setSize(1200, 800);
		eventJFrame.setVisible(true);
		centerPanel.updateUI();
	}

	private void treatVortexProperties(Vortex vortex) {
		identifiablePropertiesPanel = new VortexPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.vortex.border"), vortex);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatEnnemieProperties(Ennemie ennemie) {
		identifiablePropertiesPanel = new EnnemiePanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.ennemie.border"), ennemie);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatDecorProperties(Decor decor) {
		identifiablePropertiesPanel = new DecorPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.decor.border"), decor);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatDoorProperties(Door door) {
		identifiablePropertiesPanel = new DoorPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.door.border"), door);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatLockProperties(Lock lock) {
		identifiablePropertiesPanel = new LockPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.lock.border"), lock);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatPickProperties(Pick pick) {
		identifiablePropertiesPanel = new PickPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.pick.border"), pick);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatPlatformProperties(Platform platform) {
		identifiablePropertiesPanel = new PlatformPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.platform.border"), platform);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatRayonProperties(Rayon rayon) {
		identifiablePropertiesPanel = new RayonPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.rayon.border"), rayon);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	private void treatTeleporterProperties(Teleporter teleporter) {
		identifiablePropertiesPanel = new TeleporterPanel(message, centerPanel, drawPanel, levelService,
				message.getString("properties.teleporter.border"), teleporter);
		editionIdentifiablePanel.add(identifiablePropertiesPanel, BorderLayout.SOUTH);
	}

	public void repaint() {
		this.drawPanel.repaint();
	}

	public void loadPropertiesLevel() {
		verticalPlatformIndexSpinner.setValue((Integer) levelService.getVerticalPlatformId());
		horizontalPlatformIndexSpinner.setValue((Integer) levelService.getHorizontalPlatformId());
		backgroundIndexSpinner.setValue((Integer) levelService.getBackgroundId());
		nextLevelIndexSpinner.setValue((Integer) levelService.getNextLevelId());
		showPlatformLevelCheckBox.setSelected(levelService.isShowPlatform());
		spanishLevelNameIndexTextField.setText(levelService.getLevelName("es"));
		englishLevelNameIndexTextField.setText(levelService.getLevelName("en"));
		frenchLevelNameIndexTextField.setText(levelService.getLevelName("fr"));
	}
}
