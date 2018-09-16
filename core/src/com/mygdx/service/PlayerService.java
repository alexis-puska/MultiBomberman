package com.mygdx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Array;
import com.mygdx.domain.Player;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.game.MultiBombermanGame;
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

	private final MultiBombermanGame game;

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

	public PlayerService(MultiBombermanGame game) {
		this.game = game;
		definitions = new HashMap<>();
		firstHumanIdx = 0;
		definitions.put(0, new PlayerDefinition(0, PlayerTypeEnum.HUMAN));
		for (int i = 1; i < 16; i++) {
			definitions.put(i, new PlayerDefinition(i, PlayerTypeEnum.CPU));
		}
	}

	/**
	 * Une fois que l'utilisateur principale a definit si un utilisateur est
	 * CPU/HUMAN/NONE/NET, on effectue le cablage avec les differents controller
	 * local/distant disponible. Instanciation des differentes map.
	 */
	public void validePlayerType() {
		localController = new HashMap<>();
		Array<Controller> controllers = Controllers.getControllers();
		int controllerIndex = 0;
		if (controllers.size != 0) {
			for (Entry<Integer, PlayerDefinition> entry : definitions.entrySet()) {
				PlayerDefinition definition = entry.getValue();
				if (definition.getPlayerType() == PlayerTypeEnum.HUMAN) {
					if (controllers.get(controllerIndex) != null) {
						localController.put(controllers.get(controllerIndex), entry.getKey());
						controllerIndex++;
						if (controllerIndex >= controllers.size) {
							break;
						}
					}
				}
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
		List<NetworkConnexion> lnc = game.getNetworkService().getServer().getConnexions();
		int indexDefinition = 0;
		for (NetworkConnexion nc : lnc) {
			Map<Integer, Integer> networkConnexionMapping = new HashMap<>();
			for (int i = 0; i < nc.getPlayer(); i++) {
				while (indexDefinition < 16) {
					if (definitions.get(indexDefinition).getPlayerType() == PlayerTypeEnum.NET) {
						networkConnexionMapping.put(i, indexDefinition);
						break;
					}
					indexDefinition++;
				}
			}
			networkController.put(nc.getGuid(), networkConnexionMapping);
		}
		Gdx.app.log("mapping network done", "DONE");
	}

	public void incPlayerType(int index) {
		switch (definitions.get(index).getPlayerType()) {
		case CPU:
			definitions.get(index).setPlayerType(PlayerTypeEnum.HUMAN);
			break;
		case HUMAN:
			if (Context.getGameMode() == GameModeEnum.SERVER) {
				definitions.get(index).setPlayerType(PlayerTypeEnum.NET);
			} else {
				definitions.get(index).setPlayerType(PlayerTypeEnum.CPU);
			}
			break;
		case NET:
			definitions.get(index).setPlayerType(PlayerTypeEnum.CPU);
			break;
		}
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

	public List<Player> generatePlayer() {
		return null;
	}

	/**************************************
	 * --- NETWORK CONTROLLER ---
	 **************************************/
	public void move(String guid, int index, PovDirection direction) {
		if (this.networkController != null) {
			if (game.getScreen().getClass() == GameScreen.class) {
				Gdx.app.log("ControllerAdapter", "GameScreen specifique code");
				if (networkController.containsKey(guid)) {
					controlEventListeners.get(this.networkController.get(guid).get(index)).move(direction);
				}
			} else if (game.getScreen().getClass() == SkinScreen.class) {
				switch (direction) {
				case center:
					break;
				case east:
					definitions.get(networkController.get(guid).get(index)).incCharacter();
					break;
				case north:
					definitions.get(networkController.get(guid).get(index)).incCharacterColor();
					break;
				case northEast:
					definitions.get(networkController.get(guid).get(index)).incCharacterColor();
					definitions.get(networkController.get(guid).get(index)).incCharacter();
					break;
				case northWest:
					definitions.get(networkController.get(guid).get(index)).incCharacterColor();
					definitions.get(networkController.get(guid).get(index)).decCharacter();
					break;
				case south:
					definitions.get(networkController.get(guid).get(index)).decCharacterColor();
					break;
				case southEast:
					definitions.get(networkController.get(guid).get(index)).decCharacterColor();
					definitions.get(networkController.get(guid).get(index)).incCharacter();
					break;
				case southWest:
					definitions.get(networkController.get(guid).get(index)).decCharacterColor();
					definitions.get(networkController.get(guid).get(index)).decCharacter();
					break;
				case west:
					definitions.get(networkController.get(guid).get(index)).decCharacter();
					break;
				}
			}
		} else {
			Gdx.app.log("NetworkConnexion Map", "NULL");
		}
	}

	public void dropBombe(String guid, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass() == GameScreen.class) {
				if (networkController.containsKey(guid)) {
					controlEventListeners.get(this.networkController.get(guid).get(index)).dropBombe();
				}
			}
		}
	}

	public void throwBombe(String guid, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass() == GameScreen.class) {
				if (networkController.containsKey(guid)) {
					controlEventListeners.get(this.networkController.get(guid).get(index)).throwBombe();
				}
			}
		}
	}

	public void speedUp(String guid, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass() == GameScreen.class) {
				if (networkController.containsKey(guid)) {
					controlEventListeners.get(this.networkController.get(guid).get(index)).speedUp();
				}
			}
		}
	}

	public void speedDown(String guid, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass() == GameScreen.class) {
				if (networkController.containsKey(guid)) {
					controlEventListeners.get(this.networkController.get(guid).get(index)).speedDown();
				}
			}
		}
	}

	/**************************************
	 * --- LOCAL CONTROLLER ---
	 **************************************/
	public void move(Controller controller, PovDirection direction) {
		if (game.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)) {
			controlEventListeners.get(this.localController.get(controller)).move(direction);
		} else if (game.getScreen().getClass() == SkinScreen.class && this.localController.containsKey(controller)) {
			switch (direction) {
			case center:
				break;
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
			}
		} else if (game.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement � distance
			game.getNetworkService().sendDirection(controllerToIndex.get(controller), direction);
		} else {
			if (Controllers.getControllers().first() != null
					&& Controllers.getControllers().first().equals(controller)) {
				switch (direction) {
				case center:
					break;
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
				}
			}
		}
	}

	public void dropBombe(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)) {
				controlEventListeners.get(this.localController.get(controller)).dropBombe();
			}
		} else if (game.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement � distance
			game.getNetworkService().sendDropBombe(controllerToIndex.get(controller));
		}
	}

	public void throwBombe(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)) {
				controlEventListeners.get(this.localController.get(controller)).throwBombe();
			}
		} else if (game.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement � distance
			game.getNetworkService().sendThrowBombe(controllerToIndex.get(controller));
		}
	}

	public void speedUp(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)) {
				controlEventListeners.get(this.localController.get(controller)).speedUp();
			}
		} else if (game.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement � distance
			game.getNetworkService().sendSpeedUp(controllerToIndex.get(controller));
		}
	}

	public void speedDown(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass() == GameScreen.class && this.localController.containsKey(controller)) {
				controlEventListeners.get(this.localController.get(controller)).speedDown();
			}
		} else if (game.getScreen().getClass() == ClientViewScreen.class
				&& this.controllerToIndex.containsKey(controller)) {
			// Expedition evenement � distance
			game.getNetworkService().sendSpeedDown(controllerToIndex.get(controller));
		}
	}

	public void pressSelect(Controller controller) {
		if (game.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressSelect();
		}
	}

	public void pressStart(Controller controller) {
		if (game.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressStart();
		}
	}

	public void pressValide(Controller controller) {
		if (game.getScreen().getClass() != GameScreen.class && Controllers.getControllers().first() != null
				&& Controllers.getControllers().first().equals(controller)) {
			menuListener.pressValide();
		}
	}

	/************************************************
	 * 
	 * -------Keyboard first human mapping -------
	 * 
	 ************************************************/
	public void move(PovDirection direction) {
		Gdx.app.log("playerService", "keyboard move event");
		if (game.getScreen().getClass() == SkinScreen.class && firstHumanIdx != -1) {
			Gdx.app.log("playerService", "keyboard move event skin screen");
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
		} else if (game.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).move(direction);
		} else if (game.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			game.getNetworkService().sendDirection(0, direction);
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

	public void dropBombe() {
		if (game.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).dropBombe();
		} else if (game.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			game.getNetworkService().sendDropBombe(0);
		}
	}

	public void throwBombe() {
		if (game.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).throwBombe();
		} else if (game.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			game.getNetworkService().sendThrowBombe(0);
		}
	}

	public void speedUp() {
		if (game.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).speedUp();
		} else if (game.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			game.getNetworkService().sendSpeedUp(0);
		}
	}

	public void speedDown() {
		if (game.getScreen().getClass() == GameScreen.class && firstHumanIdx != -1) {
			controlEventListeners.get(firstHumanIdx).speedDown();
		} else if (game.getScreen().getClass() == ClientViewScreen.class && firstHumanIdx != -1) {
			game.getNetworkService().sendSpeedDown(0);
		}
	}

	public void pressSelect() {
		menuListener.pressSelect();
	}

	public void pressStart() {
		menuListener.pressStart();
	}

	public void pressValide() {
		menuListener.pressValide();
	}
}
