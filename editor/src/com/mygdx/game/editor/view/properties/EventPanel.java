package com.mygdx.game.editor.view.properties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mygdx.constante.EditorConstante;
import com.mygdx.game.editor.constant.EnabledElementEnum;
import com.mygdx.game.editor.constant.MusicEnum;
import com.mygdx.game.editor.constant.SoundEnum;
import com.mygdx.game.editor.domain.level.event.EnableElement;
import com.mygdx.game.editor.domain.level.event.Event;
import com.mygdx.game.editor.domain.level.event.Message;
import com.mygdx.game.editor.service.LevelService;
import com.mygdx.game.editor.utils.SpringUtilities;
import com.mygdx.game.editor.view.DrawPanel;
import com.mygdx.game.editor.view.properties.renderer.EnableElementRenderer;
import com.mygdx.game.editor.view.properties.renderer.MessageRenderer;

public class EventPanel extends JPanel {

	private static final long serialVersionUID = -4090876979915495722L;
	private Event event;

	private ResourceBundle message;
	private JFrame parent;
	private DrawPanel drawPanel;
	private LevelService levelService;

	private int lastEnableElementSelectedIndex;
	private int lastMessageSelectedIndex;

	/*********************************
	 * MAIN PANEL
	 *********************************/

	private BorderLayout mainLayout;
	protected JLabel idLabel;
	protected JSpinner idField;

	/*********************************
	 * TRIGGER : left panel
	 *********************************/
	private JPanel triggerPanel;
	private Border triggerBorder;
	private SpringLayout triggerLayout;

	private JPanel triggeredPanel;
	private Border triggeredBorder;
	private SpringLayout triggeredLayout;
	private JLabel onlyOnceLabel;
	private JCheckBox onlyOnceCheckBox;

	private JPanel nearTriggerPanel;
	private Border nearTriggerBorder;
	private SpringLayout nearTriggerLayout;
	private JLabel onNearestLabel;
	private JCheckBox onNearestCheckBox;
	private JLabel xLabel;
	private SpinnerNumberModel modelX;
	private JSpinner xSpinner;
	private JLabel yLabel;
	private SpinnerNumberModel modelY;
	private JSpinner ySpinner;
	private JLabel dxLabel;
	private SpinnerNumberModel modelDX;
	private JSpinner dxSpinner;
	private JLabel dyLabel;
	private SpinnerNumberModel modelDY;
	private JSpinner dySpinner;
	private JLabel itemIdLabel;
	private JSpinner itemIdSpinner;

	private JPanel countDownPanel;
	private Border countDownBorder;
	private SpringLayout countDouwnLayout;
	private JLabel countDownLabel;
	private JCheckBox countDownCheckBox;
	private JLabel countDownValueLabel;
	private JSpinner countDownValueSpinner;

	private JPanel conditionPanel;
	private Border conditionBorder;
	private SpringLayout conditionLayout;
	private JLabel onBirthLabel;
	private JCheckBox onBirthCheckBox;
	private JLabel onDeathLabel;
	private JCheckBox onDeathCheckBox;
	private JLabel onLevelEnterLabel;
	private JCheckBox onLevelEnterCheckBox;
	private JLabel noMoreEnnemieLabel;
	private JCheckBox noMoreEnnemieCheckBox;
	private JLabel explosionLabel;
	private JCheckBox explosionCheckBox;

	private JPanel optionPanel;
	private Border optionBorder;
	private SpringLayout optionLayout;
	private JLabel mirrorLabel;
	private JCheckBox mirrorCheckBox;
	private JLabel nightmareLabel;
	private JCheckBox nightmareCheckBox;
	private JLabel timeAttackLabel;
	private JCheckBox timeAttackCheckBox;
	private JLabel multiLabel;
	private JCheckBox multiCheckBox;
	private JLabel ninjaLabel;
	private JCheckBox ninjaCheckBox;

	/*********************************
	 * ACTION : center panel
	 *********************************/

	private JPanel actionPanel;
	private Border actionBorder;
	private BorderLayout actionLayout;

	// common
	private JPanel commonActionPanel;
	private Border commonActionBorder;
	private SpringLayout commonActionLayout;
	private JLabel songLabel;
	private JComboBox<MusicEnum> songComboBox;
	private JLabel soundLabel;
	private JComboBox<SoundEnum> soundComboBox;
	private JLabel darknessLabel;
	private JTextField darknessTextField;
	private JLabel iceLabel;
	private JTextField iceTextField;

	private JPanel centerActionPanel;
	private GridLayout centerActionLayout;

	// EnableElement
	private JPanel enableElementPanel;
	private Border enableElementBorder;
	private BorderLayout enableElementLayout;
	private JList<EnableElement> enableElementList;
	private JScrollPane enableElementListScollPane;
	private EnableElementRenderer enableElementRenderer;
	private DefaultListModel<EnableElement> enableElementListModel;
	private JPanel enableElementButtonPanel;
	private GridLayout enableElementButtonLayout;
	private JButton addEnableElement;
	private JButton delEnableElement;
	// editElement
	private JPanel enableElementEditPanel;
	private Border enableElementEditBorder;
	private SpringLayout enableElementEditLayout;
	private JLabel enableElementIdLabel;
	private JTextField enableElementIdTextField;
	private JLabel enableElementTypeLabel;
	private JComboBox<String> enableElementTypeComboBox;
	private JLabel enableElementStatusLabel;
	private JCheckBox enableElementStatusCheckBox;

	// Message
	private JPanel messagePanel;
	private Border messageBorder;
	private BorderLayout messageLayout;
	private JList<Message> messageList;
	private JScrollPane messageListScollPane;
	private MessageRenderer messageRenderer;
	private DefaultListModel<Message> messageListModel;
	private JPanel messageButtonPanel;
	private GridLayout messageButtonLayout;
	private JButton addmessage;
	private JButton delmessage;
	// edit message
	private JPanel messageEditPanel;
	private Border messageEditBorder;
	private SpringLayout messageEditLayout;
	private JLabel timeoutLabel;
	private JTextField timeoutTextField;
	private JLabel espagnolLabel;
	private JTextField espagnolextField;
	private JLabel englishLabel;
	private JTextField englishTextField;
	private JLabel frenchLabel;
	private JTextField frenchTextField;

	public EventPanel(ResourceBundle message, JFrame parent, DrawPanel drawPanel, LevelService levelService,
			String name, Event event) {
		this.message = message;
		this.parent = parent;
		this.drawPanel = drawPanel;
		this.event = event;
		this.levelService = levelService;
		this.lastEnableElementSelectedIndex = -1;
		this.lastMessageSelectedIndex = -1;

		initComponent();
		buildTriggerPanel();
		buildActionPanel();

		initValue();
		initListeners();
	}

	public void updateRayon() {
		levelService.updateEvent(event);
	}

	public void initComponent() {
		/***********************************
		 * MAIN
		 ***********************************/

		mainLayout = new BorderLayout();
		this.setLayout(mainLayout);

		/***********************************
		 * TRIGGER - WEST
		 ***********************************/
		triggerPanel = new JPanel();
		triggerLayout = new SpringLayout();
		triggerBorder = BorderFactory.createTitledBorder(message.getString("properties.event.trigger.border"));

		triggeredPanel = new JPanel();
		triggeredBorder = BorderFactory.createTitledBorder(message.getString("properties.event.trigger.once.border"));
		triggeredLayout = new SpringLayout();
		onlyOnceLabel = new JLabel(message.getString("properties.event.trigger.once.label"));
		onlyOnceCheckBox = new JCheckBox();

		nearTriggerPanel = new JPanel();
		nearTriggerBorder = BorderFactory.createTitledBorder(message.getString("properties.event.trigger.near.border"));
		nearTriggerLayout = new SpringLayout();
		onNearestLabel = new JLabel(message.getString("properties.event.trigger.near.label"));
		onNearestCheckBox = new JCheckBox();
		xLabel = new JLabel(message.getString("properties.event.trigger.near.x"));
		modelX = new SpinnerNumberModel(0.0, 0.0, 20.0, 0.5);
		xSpinner = new JSpinner(modelX);
		yLabel = new JLabel(message.getString("properties.event.trigger.near.y"));
		modelY = new SpinnerNumberModel(0.0, 0.0, 25.0, 0.5);
		ySpinner = new JSpinner(modelY);
		dxLabel = new JLabel(message.getString("properties.event.trigger.near.dx"));
		modelDX = new SpinnerNumberModel(0.0, 0.0, 20.0, 0.5);
		dxSpinner = new JSpinner(modelDX);
		dyLabel = new JLabel(message.getString("properties.event.trigger.near.dy"));
		modelDY = new SpinnerNumberModel(0.0, 0.0, 25.0, 0.5);
		dySpinner = new JSpinner(modelDY);
		itemIdLabel = new JLabel(message.getString("properties.event.trigger.near.itemId"));
		itemIdSpinner = new JSpinner();

		countDownPanel = new JPanel();
		countDownBorder = BorderFactory
				.createTitledBorder(message.getString("properties.event.trigger.countdown.border"));
		countDouwnLayout = new SpringLayout();
		countDownLabel = new JLabel(message.getString("properties.event.trigger.countdown.label"));
		countDownCheckBox = new JCheckBox();
		countDownValueLabel = new JLabel(message.getString("properties.event.trigger.countdown.value"));
		countDownValueSpinner = new JSpinner();

		conditionPanel = new JPanel();
		conditionBorder = BorderFactory
				.createTitledBorder(message.getString("properties.event.trigger.condition.border"));
		conditionLayout = new SpringLayout();
		onBirthLabel = new JLabel(message.getString("properties.event.trigger.condition.onBirth"));
		onBirthCheckBox = new JCheckBox();
		onDeathLabel = new JLabel(message.getString("properties.event.trigger.condition.onDeath"));
		onDeathCheckBox = new JCheckBox();
		onLevelEnterLabel = new JLabel(message.getString("properties.event.trigger.condition.onLevelEnter"));
		onLevelEnterCheckBox = new JCheckBox();
		noMoreEnnemieLabel = new JLabel(message.getString("properties.event.trigger.condition.noMoreEnnemie"));
		noMoreEnnemieCheckBox = new JCheckBox();
		explosionLabel = new JLabel(message.getString("properties.event.trigger.condition.explosion"));
		explosionCheckBox = new JCheckBox();

		optionPanel = new JPanel();
		optionBorder = BorderFactory.createTitledBorder(message.getString("properties.event.trigger.option.border"));
		optionLayout = new SpringLayout();
		mirrorLabel = new JLabel(message.getString("properties.event.trigger.option.mirror"));
		mirrorCheckBox = new JCheckBox();
		nightmareLabel = new JLabel(message.getString("properties.event.trigger.option.nightmare"));
		nightmareCheckBox = new JCheckBox();
		timeAttackLabel = new JLabel(message.getString("properties.event.trigger.option.timeAttack"));
		timeAttackCheckBox = new JCheckBox();
		multiLabel = new JLabel(message.getString("properties.event.trigger.option.multi"));
		multiCheckBox = new JCheckBox();
		ninjaLabel = new JLabel(message.getString("properties.event.trigger.option.ninja"));
		ninjaCheckBox = new JCheckBox();

		/***********************************
		 * ACTION - CENTER
		 ***********************************/

		actionPanel = new JPanel();
		actionBorder = BorderFactory.createTitledBorder(message.getString("properties.event.action.border"));
		actionLayout = new BorderLayout();

		// common
		commonActionPanel = new JPanel();
		commonActionBorder = BorderFactory
				.createTitledBorder(message.getString("properties.event.action.common.border"));
		commonActionLayout = new SpringLayout();

		songLabel = new JLabel(message.getString("properties.event.action.common.music"));
		songComboBox = new JComboBox<>(MusicEnum.values());
		songComboBox.addItem(null);
		songLabel.setToolTipText(message.getString("properties.event.action.common.music.tooltip"));
		songComboBox.setToolTipText(message.getString("properties.event.action.common.music.tooltip"));

		soundLabel = new JLabel(message.getString("properties.event.action.common.sound"));
		soundComboBox = new JComboBox<>(SoundEnum.values());
		soundComboBox.addItem(null);
		soundLabel.setToolTipText(message.getString("properties.event.action.common.sound.tooltip"));
		soundComboBox.setToolTipText(message.getString("properties.event.action.common.sound.tooltip"));

		darknessLabel = new JLabel(message.getString("properties.event.action.common.darkness"));
		darknessTextField = new JTextField();
		darknessLabel.setToolTipText(message.getString("properties.event.action.common.darkness.tooltip"));
		darknessTextField.setToolTipText(message.getString("properties.event.action.common.darkness.tooltip"));

		iceLabel = new JLabel(message.getString("properties.event.action.common.ice"));
		iceTextField = new JTextField();
		iceLabel.setToolTipText(message.getString("properties.event.action.common.ice.tooltip"));
		iceTextField.setToolTipText(message.getString("properties.event.action.common.ice.tooltip"));

		// CENTER
		centerActionPanel = new JPanel();
		centerActionLayout = new GridLayout();

		// EnableElement
		enableElementPanel = new JPanel();
		enableElementBorder = BorderFactory
				.createTitledBorder(message.getString("properties.event.action.enableElement.border"));
		enableElementLayout = new BorderLayout();
		enableElementList = new JList<>();
		enableElementListScollPane = new JScrollPane();
		enableElementListModel = new DefaultListModel<>();
		enableElementRenderer = new EnableElementRenderer();
		enableElementButtonPanel = new JPanel();
		enableElementButtonLayout = new GridLayout();

		addEnableElement = new JButton(message.getString("properties.event.action.enableElement.add"));
		delEnableElement = new JButton(message.getString("properties.event.action.enableElement.delete"));

		// Message
		messagePanel = new JPanel();
		messageBorder = BorderFactory.createTitledBorder(message.getString("properties.event.action.message.border"));
		messageLayout = new BorderLayout();
		messageList = new JList<>();
		messageListScollPane = new JScrollPane();
		messageRenderer = new MessageRenderer();
		messageListModel = new DefaultListModel<>();
		messageButtonPanel = new JPanel();
		messageButtonLayout = new GridLayout();
		addmessage = new JButton(message.getString("properties.event.action.message.add"));
		delmessage = new JButton(message.getString("properties.event.action.message.delete"));

	}

	public void buildTriggerPanel() {

		triggeredPanel.setBorder(triggeredBorder);
		triggeredPanel.setLayout(triggeredLayout);
		triggeredPanel.add(onlyOnceLabel);
		triggeredPanel.add(onlyOnceCheckBox);
		SpringUtilities.makeCompactGrid(triggeredPanel, 1, 2, 6, 6, 6, 6);

		nearTriggerPanel.setBorder(nearTriggerBorder);
		nearTriggerPanel.setLayout(nearTriggerLayout);
		nearTriggerPanel.add(onNearestLabel);
		nearTriggerPanel.add(onNearestCheckBox);
		nearTriggerPanel.add(xLabel);
		nearTriggerPanel.add(xSpinner);
		nearTriggerPanel.add(yLabel);
		nearTriggerPanel.add(ySpinner);
		nearTriggerPanel.add(dxLabel);
		nearTriggerPanel.add(dxSpinner);
		nearTriggerPanel.add(dyLabel);
		nearTriggerPanel.add(dySpinner);
		nearTriggerPanel.add(itemIdLabel);
		nearTriggerPanel.add(itemIdSpinner);
		SpringUtilities.makeCompactGrid(nearTriggerPanel, 6, 2, 6, 6, 6, 6);

		countDownPanel.setBorder(countDownBorder);
		countDownPanel.setLayout(countDouwnLayout);
		countDownPanel.add(countDownLabel);
		countDownPanel.add(countDownCheckBox);
		countDownPanel.add(countDownValueLabel);
		countDownPanel.add(countDownValueSpinner);
		SpringUtilities.makeCompactGrid(countDownPanel, 2, 2, 6, 6, 6, 6);

		conditionPanel.setBorder(conditionBorder);
		conditionPanel.setLayout(conditionLayout);
		conditionPanel.add(onBirthLabel);
		conditionPanel.add(onBirthCheckBox);
		conditionPanel.add(onDeathLabel);
		conditionPanel.add(onDeathCheckBox);
		conditionPanel.add(onLevelEnterLabel);
		conditionPanel.add(onLevelEnterCheckBox);
		conditionPanel.add(noMoreEnnemieLabel);
		conditionPanel.add(noMoreEnnemieCheckBox);
		conditionPanel.add(explosionLabel);
		conditionPanel.add(explosionCheckBox);
		SpringUtilities.makeCompactGrid(conditionPanel, 5, 2, 6, 6, 6, 6);

		optionPanel.setBorder(optionBorder);
		optionPanel.setLayout(optionLayout);
		optionPanel.add(mirrorLabel);
		optionPanel.add(mirrorCheckBox);
		optionPanel.add(nightmareLabel);
		optionPanel.add(nightmareCheckBox);
		optionPanel.add(timeAttackLabel);
		optionPanel.add(timeAttackCheckBox);
		optionPanel.add(multiLabel);
		optionPanel.add(multiCheckBox);
		optionPanel.add(ninjaLabel);
		optionPanel.add(ninjaCheckBox);
		SpringUtilities.makeCompactGrid(optionPanel, 5, 2, 6, 6, 6, 6);

		triggerPanel.setLayout(triggerLayout);
		triggerPanel.setBorder(triggerBorder);
		triggerPanel.add(triggeredPanel);
		triggerPanel.add(nearTriggerPanel);
		triggerPanel.add(countDownPanel);
		triggerPanel.add(conditionPanel);
		triggerPanel.add(optionPanel);
		triggerPanel.setPreferredSize(new Dimension(EditorConstante.EVENT_FRAME_TRIGGER_PANEL_WIDTH, 700));
		SpringUtilities.makeCompactGrid(triggerPanel, 5, 1, 6, 6, 6, 6);
		this.add(triggerPanel, BorderLayout.WEST);
	}

	public void buildActionPanel() {

		// common
		commonActionPanel.setBorder(commonActionBorder);
		commonActionPanel.setLayout(commonActionLayout);

		commonActionPanel.add(songLabel);
		commonActionPanel.add(songComboBox);
		commonActionPanel.add(soundLabel);
		commonActionPanel.add(soundComboBox);
		commonActionPanel.add(darknessLabel);
		commonActionPanel.add(darknessTextField);
		commonActionPanel.add(iceLabel);
		commonActionPanel.add(iceTextField);
		SpringUtilities.makeCompactGrid(commonActionPanel, 4, 2, 6, 6, 6, 6);

		// EnableElement
		enableElementPanel.setBorder(enableElementBorder);
		enableElementPanel.setLayout(enableElementLayout);
		enableElementButtonLayout.setColumns(2);
		enableElementButtonLayout.setRows(1);
		enableElementButtonPanel.setLayout(enableElementButtonLayout);
		enableElementButtonPanel.add(addEnableElement);
		enableElementButtonPanel.add(delEnableElement);
		enableElementList.setModel(enableElementListModel);
		enableElementList.setCellRenderer(enableElementRenderer);
		enableElementListScollPane.setViewportView(enableElementList);
		enableElementPanel.add(enableElementListScollPane, BorderLayout.CENTER);
		enableElementPanel.add(enableElementButtonPanel, BorderLayout.SOUTH);

		// Message
		messagePanel.setBorder(messageBorder);
		messagePanel.setLayout(messageLayout);
		messageButtonLayout.setRows(1);
		messageButtonLayout.setColumns(2);
		messageButtonPanel.setLayout(messageButtonLayout);
		messageButtonPanel.add(addmessage);
		messageButtonPanel.add(delmessage);
		messageList.setModel(messageListModel);
		messageList.setCellRenderer(messageRenderer);
		messageListScollPane.setViewportView(messageList);
		messagePanel.add(messageListScollPane, BorderLayout.CENTER);
		messagePanel.add(messageButtonPanel, BorderLayout.SOUTH);

		centerActionLayout.setColumns(1);
		centerActionLayout.setRows(2);
		centerActionPanel.setLayout(centerActionLayout);
		centerActionPanel.add(enableElementPanel);
		centerActionPanel.add(messagePanel);

		actionPanel.setBorder(actionBorder);
		actionPanel.setLayout(actionLayout);
		actionPanel.add(commonActionPanel, BorderLayout.NORTH);
		actionPanel.add(centerActionPanel, BorderLayout.CENTER);
		this.add(actionPanel, BorderLayout.CENTER);

	}

	public void buildEnableElementEditPanel(EnableElement enableElement) {
		if (enableElement != null) {
			if (enableElementEditPanel != null) {
				enableElementPanel.remove(enableElementEditPanel);
			}
			enableElementEditPanel = new JPanel();
			enableElementEditBorder = BorderFactory
					.createTitledBorder(message.getString("properties.event.action.enableElement.edit.border"));
			enableElementEditLayout = new SpringLayout();
			enableElementIdLabel = new JLabel(message.getString("properties.event.action.enableElement.edit.id"));
			enableElementIdTextField = new JTextField();
			enableElementIdLabel.setLabelFor(enableElementIdTextField);
			enableElementTypeLabel = new JLabel(message.getString("properties.event.action.enableElement.edit.type"));
			enableElementTypeComboBox = new JComboBox<>(EnabledElementEnum.getValues());
			enableElementTypeLabel.setLabelFor(enableElementTypeComboBox);
			enableElementStatusLabel = new JLabel(
					message.getString("properties.event.action.enableElement.edit.status"));
			enableElementStatusCheckBox = new JCheckBox();
			enableElementStatusLabel.setLabelFor(enableElementStatusCheckBox);
			enableElementEditPanel.setLayout(enableElementEditLayout);
			enableElementEditPanel.setBorder(enableElementEditBorder);
			enableElementEditPanel.add(enableElementIdLabel);
			enableElementEditPanel.add(enableElementIdTextField);
			enableElementEditPanel.add(enableElementTypeLabel);
			enableElementEditPanel.add(enableElementTypeComboBox);
			enableElementEditPanel.add(enableElementStatusLabel);
			enableElementEditPanel.add(enableElementStatusCheckBox);
			SpringUtilities.makeCompactGrid(enableElementEditPanel, 3, 2, 6, 6, 6, 6);
			initListenersEnableElement();
			enableElementPanel.add(enableElementEditPanel, BorderLayout.NORTH);
			if (enableElement.getElementType() != null) {
				enableElementTypeComboBox.setSelectedItem(enableElement.getElementType().name());
			}
			enableElementIdTextField.setText(Integer.toString(enableElement.getId()));
			enableElementStatusCheckBox.setSelected(enableElement.isNewState());
			enableElementPanel.revalidate();
			enableElementPanel.repaint();
		}
	}

	public void buildMessageEditPanel(Message messageToEdit) {
		if (messageToEdit != null) {
			if (messageEditPanel != null) {
				messagePanel.remove(messageEditPanel);
			}
			messageEditPanel = new JPanel();
			messageEditBorder = BorderFactory
					.createTitledBorder(message.getString("properties.event.action.message.edit.border"));
			messageEditLayout = new SpringLayout();
			timeoutLabel = new JLabel(message.getString("properties.event.action.message.edit.timeout"));
			timeoutTextField = new JTextField();
			timeoutLabel.setLabelFor(timeoutTextField);
			espagnolLabel = new JLabel(message.getString("properties.event.action.message.edit.es"));
			espagnolextField = new JTextField();
			espagnolLabel.setLabelFor(espagnolextField);
			englishLabel = new JLabel(message.getString("properties.event.action.message.edit.en"));
			englishTextField = new JTextField();
			englishLabel.setLabelFor(englishTextField);
			frenchLabel = new JLabel(message.getString("properties.event.action.message.edit.fr"));
			frenchTextField = new JTextField();
			frenchLabel.setLabelFor(frenchTextField);
			messageEditPanel.setLayout(messageEditLayout);
			messageEditPanel.setBorder(messageEditBorder);
			messageEditPanel.add(timeoutLabel);
			messageEditPanel.add(timeoutTextField);
			messageEditPanel.add(frenchLabel);
			messageEditPanel.add(frenchTextField);
			messageEditPanel.add(englishLabel);
			messageEditPanel.add(englishTextField);
			messageEditPanel.add(espagnolLabel);
			messageEditPanel.add(espagnolextField);

			timeoutTextField.setText(Integer.toString(messageToEdit.getTimeout()));
			espagnolextField.setText(messageToEdit.getEs());
			englishTextField.setText(messageToEdit.getEn());
			frenchTextField.setText(messageToEdit.getFr());

			SpringUtilities.makeCompactGrid(messageEditPanel, 4, 2, 6, 6, 6, 6);
			initListenersMessage();
			messagePanel.add(messageEditPanel, BorderLayout.NORTH);
			messagePanel.revalidate();
			messagePanel.repaint();
		}
	}

	private void initValue() {
		/*********************************
		 * TRIGGER : left panel
		 *********************************/
		onlyOnceCheckBox.setSelected(event.isOnlyOnce());

		onNearestCheckBox.setSelected(event.isNear());
		xSpinner.setValue((Double) event.getX());
		ySpinner.setValue((Double) event.getY());
		dxSpinner.setValue((Double) event.getDx());
		dySpinner.setValue((Double) event.getDy());
		itemIdSpinner.setValue((Integer) event.getItemId());
		//
		countDownCheckBox.setSelected(event.isTime());
		countDownValueSpinner.setValue((Integer) event.getTimeout());

		onBirthCheckBox.setSelected(event.isOnBirth());
		onDeathCheckBox.setSelected(event.isOnDeath());
		onLevelEnterCheckBox.setSelected(event.isOnLevelEnter());
		noMoreEnnemieCheckBox.setSelected(event.isNoMoreEnnemie());
		explosionCheckBox.setSelected(event.isExplosion());

		mirrorCheckBox.setSelected(event.isMirror());
		nightmareCheckBox.setSelected(event.isNightmare());
		timeAttackCheckBox.setSelected(event.isTimeAttackeOption());
		multiCheckBox.setSelected(event.isMultiOption());
		ninjaCheckBox.setSelected(event.isNinja());

		/*********************************
		 * ACTION : center panel
		 *********************************/

		songComboBox.setSelectedItem(event.getSong());
		soundComboBox.setSelectedItem(event.getSound());
		darknessTextField.setText(Integer.toString(event.getDarknessValue()));
		iceTextField.setText(Integer.toString(event.getIceValue()));
		if (event.getEnableElement() != null) {
			for (EnableElement e : event.getEnableElement()) {
				enableElementListModel.addElement(e);
			}
		}
		if (event.getMessage() != null) {
			for (Message e : event.getMessage()) {
				messageListModel.addElement(e);
			}
		}
	}

	/***********************************************************
	 * 
	 * --- FRAME LISTENER ---
	 * 
	 ***********************************************************/
	private void initListeners() {

		/********************************
		 * WINDOWS CLOSE
		 ********************************/
		parent.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				save();
			}

			@Override
			public void windowClosed(WindowEvent windowEvent) {
				save();
			}
		});

		/*********************************
		 * TRIGGER : left panel
		 *********************************/
		onlyOnceCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setOnlyOnce(onlyOnceCheckBox.isSelected());
			}
		});

		onNearestCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setNear(onNearestCheckBox.isSelected());
			}
		});
		xSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setX((Double) text.getValue());
					drawPanel.repaint();
				}
			}
		});
		ySpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setY((Double) text.getValue());
					drawPanel.repaint();
				}
			}
		});
		dxSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setDx((Double) text.getValue());
				}
			}
		});
		dySpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setDy((Double) text.getValue());
				}
			}
		});
		itemIdSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setItemId((Integer) text.getValue());
				}
			}
		});
		//
		countDownCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setTime(countDownCheckBox.isSelected());
			}
		});
		countDownValueSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner text = (JSpinner) e.getSource();
				if (text.getValue() != null) {
					event.setTimeout((Integer) text.getValue());
				}
			}
		});

		onBirthCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setOnBirth(onBirthCheckBox.isSelected());
			}
		});
		onDeathCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setOnDeath(onDeathCheckBox.isSelected());
			}
		});
		onLevelEnterCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setOnLevelEnter(onLevelEnterCheckBox.isSelected());
			}
		});
		noMoreEnnemieCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setNoMoreEnnemie(onLevelEnterCheckBox.isSelected());
			}
		});
		explosionCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setExplosion(onLevelEnterCheckBox.isSelected());
			}
		});
		mirrorCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setMirror(mirrorCheckBox.isSelected());
			}
		});
		nightmareCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setNightmare(nightmareCheckBox.isSelected());
			}
		});
		timeAttackCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setTimeAttackeOption(timeAttackCheckBox.isSelected());
			}
		});
		multiCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setMultiOption(multiCheckBox.isSelected());
			}
		});
		ninjaCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				event.setNinja(ninjaCheckBox.isSelected());
			}
		});

		/*********************************
		 * ACTION : center panel
		 *********************************/

		songComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				MusicEnum song = (MusicEnum) songComboBox.getSelectedItem();
				event.setSong(song);
			}
		});
		soundComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				SoundEnum sound = (SoundEnum) soundComboBox.getSelectedItem();
				event.setSound(sound);
			}
		});
		darknessTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					try {
						event.setDarknessValue(Integer.parseInt(darknessTextField.getText()));
					} catch (NumberFormatException ex) {
					}
				}
			}
		});
		darknessTextField.addKeyListener(new KeyListener() {
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
		iceTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					try {
						event.setIceValue(Integer.parseInt(iceTextField.getText()));
					} catch (NumberFormatException ex) {
					}
				}
			}
		});
		iceTextField.addKeyListener(new KeyListener() {
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

		/**************************
		 * 
		 * --- Enable Element ---
		 * 
		 **************************/
		enableElementList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (enableElementList.getSelectedIndex() != -1
						&& lastEnableElementSelectedIndex == enableElementList.getSelectedIndex()) {
					if (enableElementEditPanel != null) {
						enableElementPanel.remove(enableElementEditPanel);
						enableElementPanel.updateUI();
					}
					enableElementList.clearSelection();
					lastEnableElementSelectedIndex = -1;
				} else {
					buildEnableElementEditPanel(enableElementList.getSelectedValue());
					lastEnableElementSelectedIndex = enableElementList.getSelectedIndex();
				}
			}
		});
		addEnableElement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableElementListModel.addElement(new EnableElement());
			}
		});
		delEnableElement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				enableElementListModel.remove(enableElementList.getSelectedIndex());
				if (enableElementEditPanel != null) {
					enableElementPanel.remove(enableElementEditPanel);
					enableElementPanel.updateUI();
				}
			}
		});

		/**************************
		 * 
		 * --- Message ---
		 * 
		 **************************/
		messageList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (messageList.getSelectedIndex() != -1
						&& lastMessageSelectedIndex == messageList.getSelectedIndex()) {
					if (messageEditPanel != null) {
						messagePanel.remove(messageEditPanel);
						messagePanel.updateUI();
					}
					messageList.clearSelection();
					lastMessageSelectedIndex = -1;
				} else {
					buildMessageEditPanel(messageList.getSelectedValue());
					lastMessageSelectedIndex = messageList.getSelectedIndex();
				}
			}
		});
		addmessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				messageListModel.addElement(new Message());
			}
		});
		delmessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				messageListModel.remove(messageList.getSelectedIndex());
				if (messageEditPanel != null) {
					messagePanel.remove(messageEditPanel);
					messagePanel.updateUI();
				}
			}
		});
	}

	/***********************************************************
	 * 
	 * --- ENABLE ELEMENT EDIT LISTENER ---
	 * 
	 ***********************************************************/
	private void initListenersEnableElement() {
		enableElementIdTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					try {
						int idx = enableElementList.getSelectedIndex();
						EnableElement el = enableElementListModel.get(idx);
						el.setId(Integer.parseInt(text.getText()));
						enableElementListModel.remove(idx);
						enableElementListModel.insertElementAt(el, idx);
						enableElementList.setSelectedIndex(idx);
					} catch (NumberFormatException ex) {
					}
				}
			}
		});
		enableElementIdTextField.addKeyListener(new KeyListener() {
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
		enableElementTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String item = (String) e.getItem();
					int idx = enableElementList.getSelectedIndex();
					EnableElement el = enableElementListModel.get(idx);
					el.setElementType(EnabledElementEnum.valueOf(item));
					enableElementListModel.remove(idx);
					enableElementListModel.insertElementAt(el, idx);
					enableElementList.setSelectedIndex(idx);
				}
			}
		});
		enableElementStatusCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int idx = enableElementList.getSelectedIndex();
				EnableElement el = enableElementListModel.get(idx);
				el.setNewState(enableElementStatusCheckBox.isSelected());
				enableElementListModel.remove(idx);
				enableElementListModel.insertElementAt(el, idx);
				enableElementList.setSelectedIndex(idx);
			}
		});
	}

	/***********************************************************
	 * 
	 * --- MESSAGE EDIT LISTENER ---
	 * 
	 ***********************************************************/
	private void initListenersMessage() {
		timeoutTextField.addCaretListener(new CaretListener() {
			public void caretUpdate(javax.swing.event.CaretEvent e) {
				JTextField text = (JTextField) e.getSource();
				if (text.getText() != null && !text.getText().isEmpty()) {
					int idx = messageList.getSelectedIndex();
					Message message = messageListModel.get(idx);
					message.setTimeout(Integer.parseInt(text.getText()));
					messageListModel.remove(idx);
					messageListModel.insertElementAt(message, idx);
					messageList.setSelectedIndex(idx);
				}
			}
		});
		timeoutTextField.addKeyListener(new KeyListener() {
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
		espagnolextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				int idx = messageList.getSelectedIndex();
				Message message = messageListModel.get(idx);
				message.setEs(espagnolextField.getText());
				messageListModel.remove(idx);
				messageListModel.insertElementAt(message, idx);
				messageList.setSelectedIndex(idx);
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
		englishTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				int idx = messageList.getSelectedIndex();
				Message message = messageListModel.get(idx);
				message.setEn(englishTextField.getText());
				messageListModel.remove(idx);
				messageListModel.insertElementAt(message, idx);
				messageList.setSelectedIndex(idx);
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
		frenchTextField.getDocument().addDocumentListener(new DocumentListener() {
			private void updateData() {
				int idx = messageList.getSelectedIndex();
				Message message = messageListModel.get(idx);
				message.setFr(frenchTextField.getText());
				messageListModel.remove(idx);
				messageListModel.insertElementAt(message, idx);
				messageList.setSelectedIndex(idx);
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
	}

	/***********************************************************
	 * 
	 * --- SAVE ---
	 * 
	 ***********************************************************/
	private void save() {
		List<EnableElement> l = new ArrayList<>();
		for (int i = 0; i < enableElementListModel.getSize(); i++) {
			l.add(enableElementListModel.getElementAt(i));
		}
		this.event.setEnableElement(l);
		List<Message> l2 = new ArrayList<>();
		for (int i = 0; i < messageListModel.getSize(); i++) {
			l2.add(messageListModel.getElementAt(i));
		}
		this.event.setMessage(l2);
		levelService.updateEvent(event);
	}
}
