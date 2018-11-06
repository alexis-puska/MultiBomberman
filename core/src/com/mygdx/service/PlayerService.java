package com.mygdx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.domain.Player;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.domain.level.Level;
import com.mygdx.domain.level.StartPlayer;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.main.MultiBombermanGame;
import com.mygdx.service.input_processor.ControlEventListener;
import com.mygdx.service.input_processor.MenuListener;
import com.mygdx.service.network.server.NetworkConnexion;
import com.mygdx.view.ClientViewScreen;
import com.mygdx.view.GameScreen;
import com.mygdx.view.SkinScreen;

/**
 * @author Alexis PUSKARCZYK
 *
 */
public class PlayerService {

	private static final String CLASS_NAME = "PlayerService.class";

	private final MultiBombermanGame mbGame;

	private Map<Integer, PlayerDefinition> definitions;
	private Map<Integer, ControlEventListener> controlEventListeners;

	// map pour rediriger l'evenement d'un controller distant vers la definition ou
	// un joueur
	private Map<String, Map<Integer, Integer>> networkController;

	// map pour rediriger l'evenement d'nu controller local vers la definition ou un
	// joueur
	private Map<Controller, Integer> localController;

	private Map<Controller, Integer> controllerToIndex;

	private int firstHumanIdx;

	private MenuListener menuListener;

	public MenuListener getMenuListener() {
		return menuListener;
	}

	public void setMenuListener(MenuListener menuListener) {
		this.menuListener = menuListener;
	}

	public PlayerService(MultiBombermanGame mbGame) {
		this.mbGame = mbGame;
		definitions = new HashMap<>();
		definitions.put(0, new PlayerDefinition(0, PlayerTypeEnum.HUMAN));
		for (int i = 1; i < 16; i++) {
			definitions.put(i, new PlayerDefinition(i, PlayerTypeEnum.CPU));
		}
	}

	public int getNbHumanPlayerFromDefinition() {
		int nb = 0;
		for (Entry<Integer, PlayerDefinition> definition : definitions.entrySet()) {
			if (definition.getValue().getPlayerType() == PlayerTypeEnum.HUMAN) {
				nb++;
			}
		}
		return nb;
	}

	public int getNbNetworkPlayerFromDefinition() {
		int nb = 0;
		for (Entry<Integer, PlayerDefinition> definition : definitions.entrySet()) {
			if (definition.getValue().getPlayerType() == PlayerTypeEnum.NET) {
				nb++;
			}
		}
		return nb;
	}

	/**
	 * Une fois que l'utilisateur principale a definit si un utilisateur est
	 * CPU/HUMAN/NONE/NET, on effectue le cablage avec les differents controller
	 * local/distant disponible. Instanciation des differentes map.
	 */
	public void validePlayerType() {
		firstHumanIdx = -1;
		localController = new HashMap<>();
		Array<Controller> controllers = Controllers.getControllers();
		int controllerIndex = 0;
		if (controllers.size != 0) {
			for (Entry<Integer, PlayerDefinition> entry : definitions.entrySet()) {
				PlayerDefinition definition = entry.getValue();
				if (definition.getPlayerType() == PlayerTypeEnum.HUMAN && controllers.get(controllerIndex) != null) {
					localController.put(controllers.get(controllerIndex), entry.getKey());
					controllerIndex++;
					if (controllerIndex >= controllers.size) {
						break;
					}
				}
			}
		}
		for (Entry<Integer, PlayerDefinition> entry : definitions.entrySet()) {
			PlayerDefinition definition = entry.getValue();
			if (definition.getPlayerType() == PlayerTypeEnum.HUMAN && firstHumanIdx == -1) {
				firstHumanIdx = definition.getPosition();
				break;
			}
		}
	}

	public void initControllerMap() {
		controllerToIndex = new HashMap<>();
		Array<Controller> controllers = Controllers.getControllers();
		int controllerIndex = 0;
		for (Controller controller : controllers) {
			controllerToIndex.put(controller, controllerIndex);
			controllerIndex++;
		}
	}

	public void valideNetWorkPlayerType() {
		validePlayerType();
		networkController = new HashMap<>();
		Map<String, NetworkConnexion> lnc = mbGame.getNetworkService().getServer().getConnexions();
		int indexDefinition = 0;
		for (Entry<String, NetworkConnexion> nc : lnc.entrySet()) {
			Map<Integer, Integer> networkConnexionMapping = new HashMap<>();
			for (int i = 0; i < nc.getValue().getPlayer(); i++) {
				while (indexDefinition < 16) {
					if (definitions.get(indexDefinition).getPlayerType() == PlayerTypeEnum.NET) {
						networkConnexionMapping.put(i, indexDefinition);
						indexDefinition++;
						break;
					}
					indexDefinition++;
				}
			}
			networkController.put(nc.getValue().getUuid(), networkConnexionMapping);
		}
		Gdx.app.debug(CLASS_NAME, "mapping network done : DONE");
	}

	public void incPlayerType(int index) {
		definitions.get(index).incPlayerType(Context.getGameMode() == GameModeEnum.SERVER);
	}

	public void decPlayerType(int index) {
		definitions.get(index).decPlayerType(Context.getGameMode() == GameModeEnum.SERVER);
	}

	public PlayerTypeEnum getPlayerType(int index) {
		return definitions.get(index).getPlayerType();
	}

	public CharacterEnum getPlayerCharacter(int index) {
		return definitions.get(index).getCharacter();
	}

	public CharacterColorEnum getPlayerColor(int index) {
		return definitions.get(index).getColor();
	}

	public List<Player> generatePlayer(World world, Level level, List<StartPlayer> startPlayer) {
		controlEventListeners = new HashMap<>();
		List<Player> players = new ArrayList<>();

		int idx = 0;
		for (Entry<Integer, PlayerDefinition> def : definitions.entrySet()) {
			Gdx.app.log(CLASS_NAME,
					"GENERATE PLAYER : " + def.getValue().getPosition() + ",  "
							+ def.getValue().getPlayerType().toString() + ",  "
							+ def.getValue().getCharacter().toString() + ",  " + def.getValue().getColor().toString());
			if (idx < startPlayer.size()) {
				Player p = new Player(world, mbGame, level, def.getValue().getCharacter(), def.getValue().getColor(),
						startPlayer.get(idx), Context.getStrength(), Context.getBombe());
				players.add(p);
				controlEventListeners.put(def.getKey(), p);
				idx++;
			}
		}
		return players;
	}

	/**************************************
	 * --- NETWORK CONTROLLER ---
	 **************************************/
	public void move(String uuid, int index, PovDirection direction) {
		if (this.networkController != null) {
			if (mbGame.getScreen().getClass() == GameScreen.class) {
				Gdx.app.debug(CLASS_NAME, "GameScreen specifique code");
				if (networkController.containsKey(uuid)) {
					controlEventListeners.get(this.networkController.get(uuid).get(index)).move(direction);
				}
			} else if (mbGame.getScreen().getClass() == SkinScreen.class) {
				switch (direction) {
				case east:
					definitions.get(networkController.get(uuid).get(index)).incCharacter();
					break;
				case north:
					definitions.get(networkController.get(uuid).get(index)).incCharacterColor();
					break;
				case northEast:
					definitions.get(networkController.get(uuid).get(index)).incCharacterColor();
					definitions.get(networkController.get(uuid).get(index)).incCharacter();
					break;
				case northWest:
					definitions.get(networkController.get(uuid).get(index)).incCharacterColor();
					definitions.get(networkController.get(uuid).get(index)).decCharacter();
					break;
				case south:
					definitions.get(networkController.get(uuid).get(index)).decCharacterColor();
					break;
				case southEast:
					definitions.get(networkController.get(uuid).get(index)).decCharacterColor();
					definitions.get(networkController.get(uuid).get(index)).incCharacter();
					break;
				case southWest:
					definitions.get(networkController.get(uuid).get(index)).decCharacterColor();
					definitions.get(networkController.get(uuid).get(index)).decCharacter();
					break;
				case west:
					definitions.get(networkController.get(uuid).get(index)).decCharacter();
					break;
				case center:
				default:
					break;
				}
			}
		} else {
			Gdx.app.debug(CLASS_NAME, " Map NULL");
		}
	}

	public void pressA(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressA();
		}
	}

	public void pressB(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressB();
		}
	}

	public void pressX(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressX();
		}
	}

	public void pressY(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressY();
		}
	}

	public void pressL(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressL();
		}
	}

	public void pressR(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressR();
		}
	}

	public void releaseL(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).releaseL();
		}
	}

	public void releaseR(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).releaseR();
		}
	}

	public void pressSelect(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressSelect();
		}
	}

	public void pressStart(String uuid, int index) {
		if (this.networkController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& networkController.containsKey(uuid)) {
			controlEventListeners.get(this.networkController.get(uuid).get(index)).pressStart();
		}
	}

	/**************************************
	 * --- LOCAL CONTROLLER ---
	 **************************************/
	public void move(Controller controller, PovDirection direction) {
		if (mbGame.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)
				&& controlEventListeners.get(this.localController.get(controller)) != null) {
			controlEventListeners.get(this.localController.get(controller)).move(direction);
		} else if (mbGame.getScreen().getClass() == SkinScreen.class && this.localController.containsKey(controller)) {
			switch (direction) {
			case east:
				definitions.get(localController.get(controller)).incCharacter();
				break;
			case north:
				definitions.get(localController.get(controller)).incCharacterColor();
				break;
			case northEast:
				definitions.get(localController.get(controller)).incCharacterColor();
				definitions.get(localController.get(controller)).incCharacter();
				break;
			case northWest:
				definitions.get(localController.get(controller)).incCharacterColor();
				definitions.get(localController.get(controller)).decCharacter();
				break;
			case south:
				definitions.get(localController.get(controller)).decCharacterColor();
				break;
			case southEast:
				definitions.get(localController.get(controller)).decCharacterColor();
				definitions.get(localController.get(controller)).incCharacter();
				break;
			case southWest:
				definitions.get(localController.get(controller)).decCharacterColor();
				definitions.get(localController.get(controller)).decCharacter();
				break;
			case west:
				definitions.get(localController.get(controller)).decCharacter();
				break;
			case center:
			default:
				break;
			}
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendDirection(controllerToIndex.get(controller), direction);
		} else {
			if (Controllers.getControllers().first() != null
					&& Controllers.getControllers().first().equals(controller)) {
				switch (direction) {
				case east:
					menuListener.pressRight();
					break;
				case north:
					menuListener.pressUp();
					break;
				case northEast:
					break;
				case northWest:
					break;
				case south:
					menuListener.pressDown();
					break;
				case southEast:
					break;
				case southWest:
					break;
				case west:
					menuListener.pressLeft();
					break;
				case center:
				default:
					break;
				}
			}
		}
	}

	public void pressX(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressX();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressX(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressX();
		}
	}

	public void pressY(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressY();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressY(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressY();
		}
	}

	public void pressL(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressL();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressL(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressL();
		}
	}

	public void pressR(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressR();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressR(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressR();
		}
	}

	public void releaseL(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).releaseL();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendReleaseL(controllerToIndex.get(controller));
		}
	}

	public void releaseR(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).releaseR();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendReleaseR(controllerToIndex.get(controller));
		}
	}

	public void pressSelect(Controller controller) {
		if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressSelect();
		}
	}

	public void pressStart(Controller controller) {
		if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressStart();
		}
	}

	public void pressA(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressA();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressA(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressA();
		}
	}

	public void pressB(Controller controller) {
		if (this.localController != null && mbGame.getScreen().getClass() == GameScreen.class
				&& this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).pressB();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement à distance
			mbGame.getNetworkService().sendPressB(controllerToIndex.get(controller));
		} else if (mbGame.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressB();
		}
	}

	/************************************************
	 * 
	 * -------Keyboard first human mapping -------
	 * 
	 ************************************************/
	public void move(PovDirection direction) {
		Gdx.app.debug(CLASS_NAME, "keyboard move event");
		if (mbGame.getScreen().getClass() == SkinScreen.class && firstHumanIdx != -1) {
			Gdx.app.debug(CLASS_NAME, "keyboard move event skin screen");
			switch (direction) {
			case east:
				definitions.get(firstHumanIdx).incCharacter();
				break;
			case north:
				definitions.get(firstHumanIdx).incCharacterColor();
				break;
			case south:
				definitions.get(firstHumanIdx).decCharacterColor();
				break;
			case west:
				definitions.get(firstHumanIdx).decCharacter();
				break;
			case center:
			case northEast:
			case northWest:
			case southEast:
			case southWest:
			default:
				break;
			}
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1
				&& controlEventListeners.get(firstHumanIdx) != null) {
			controlEventListeners.get(firstHumanIdx).move(direction);
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendDirection(0, direction);
		} else {
			switch (direction) {
			case east:
				menuListener.pressRight();
				break;
			case north:
				menuListener.pressUp();
				break;
			case south:
				menuListener.pressDown();
				break;
			case west:
				menuListener.pressLeft();
				break;
			case center:
			case northEast:
			case northWest:
			case southEast:
			case southWest:
			default:
				break;
			}
		}
	}

	public void pressSelect() {
		menuListener.pressSelect();
	}

	public void pressStart() {
		menuListener.pressStart();
	}

	public void pressA() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressA();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressA();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressA(0);
		}
	}

	public void pressB() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressB();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressB();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressB(0);
		}
	}

	public void pressX() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressX();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressX();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressX(0);
		}
	}

	public void pressY() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressY();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressY();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressY(0);
		}
	}

	public void pressL() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressL();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressL();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressL(0);
		}
	}

	public void pressR() {
		if (mbGame.getScreen().getClass() != GameScreen.class) {
			menuListener.pressR();
		} else if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).pressR();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendPressR(0);
		}
	}

	public void releaseL() {
		if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).releaseL();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendReleaseL(0);
		}
	}

	public void releaseR() {
		if (mbGame.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).releaseR();
		} else if (mbGame.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			mbGame.getNetworkService().sendReleaseR(0);
		}
	}
}
