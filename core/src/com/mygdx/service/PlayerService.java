package com.mygdx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.domain.Player;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.enumeration.CharacterColorEnum;
import com.mygdx.enumeration.CharacterEnum;
import com.mygdx.enumeration.GameModeEnum;
import com.mygdx.enumeration.PlayerTypeEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.input_processor.ControlEventListener;
import com.mygdx.service.network.server.NetworkConnexion;
import com.mygdx.view.GameScreen;
import com.mygdx.view.SkinScreen;

/**
 * @author Alexis PUSKARCZYK
 *
 */
public class PlayerService {

	private final MultiBombermanGame game;

	private Map<Integer, ControlEventListener> controlEventListeners;

	private Map<NetworkConnexion, Map<Integer, Integer>> networkController;
	private Map<Controller, Integer> localController;

	private Map<NetworkConnexion, Map<Integer, Integer>> definitionNetworkMapping;
	private Map<Controller, Integer> definitionControllerMapping;
	private PlayerDefinition[] definitions;

	private int firstHumanIdx;

	public PlayerService(MultiBombermanGame game) {
		this.game = game;
		definitions = new PlayerDefinition[16];
		firstHumanIdx = 0;
		definitions[0] = new PlayerDefinition(0, PlayerTypeEnum.HUMAN);
		for (int i = 1; i < 16; i++) {
			definitions[i] = new PlayerDefinition(0, PlayerTypeEnum.CPU);
		}
	}

	/**
	 * Une fois que l'utilisateur principale a definit si un utilisateur est
	 * CPU/HUMAN/NONE/NET, on effectue le cablage avec les differents controller
	 * local/distant disponible. Instanciation des differentes map.
	 */
	public void validePlayerType() {
		networkController = new HashMap<>();
		definitionNetworkMapping = new HashMap<>();
		definitionControllerMapping = new HashMap<>();
		localController = new HashMap<>();
		List<NetworkConnexion> lnc = game.getNetworkService().getServer().getNcl();
		// TODO init map to redirect correct event to player

	}

	public void incPlayerType(int index) {
		switch (definitions[index].getPlayerType()) {
		case CPU:
			definitions[index].setPlayerType(PlayerTypeEnum.HUMAN);
			break;
		case HUMAN:
			if (Context.gameMode == GameModeEnum.SERVER) {
				definitions[index].setPlayerType(PlayerTypeEnum.NET);
			} else {
				definitions[index].setPlayerType(PlayerTypeEnum.CPU);
			}
			break;
		case NET:
			definitions[index].setPlayerType(PlayerTypeEnum.CPU);
			break;
		}
	}

	public String getPlayerType(int index) {
		return definitions[index].getPlayerType().toString();
	}

	public List<Player> generatePlayer() {
		return null;
	}

	/**************************************
	 * --- NETWORK CONTROLLER ---
	 **************************************/
	public void move(NetworkConnexion networkConnexion, int index, PovDirection direction) {
		if (this.networkController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				Gdx.app.log("ControllerAdapter", "GameScreen specifique code");
				if (networkController.containsKey(networkConnexion)) {
					controlEventListeners.get(this.networkController.get(networkConnexion).get(index)).move(direction);
				}
			} else if (game.getScreen().getClass().isInstance(SkinScreen.class)) {
				int idx = definitionNetworkMapping.get(networkConnexion).get(index);
				switch (direction) {
				case center:
					break;
				case east:
					definitions[idx].setCharacter(CharacterEnum.next(definitions[idx].getCharacter()));
					break;
				case north:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					break;
				case northEast:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case northWest:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				case south:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case southEast:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case southWest:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				case west:
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				}
				for (int i = 0; i < 16; i++) {
					if (definitions[i] != null) {
						Gdx.app.log("definition : ", "" + definitions[i].getCharacter().toString() + " "
								+ definitions[i].getColor().toString());
					}
				}
			}
		}
	}

	public void dropBombe(NetworkConnexion networkConnexion, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				if (networkController.containsKey(networkConnexion)) {
					controlEventListeners.get(this.networkController.get(networkConnexion).get(index)).dropBombe();
				}
			}
		}
	}

	public void throwBombe(NetworkConnexion networkConnexion, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				if (networkController.containsKey(networkConnexion)) {
					controlEventListeners.get(this.networkController.get(networkConnexion).get(index)).throwBombe();
				}
			}
		}
	}

	public void speedUp(NetworkConnexion networkConnexion, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				if (networkController.containsKey(networkConnexion)) {
					controlEventListeners.get(this.networkController.get(networkConnexion).get(index)).speedUp();
				}
			}
		}
	}

	public void speedDown(NetworkConnexion networkConnexion, int index) {
		if (this.networkController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				if (networkController.containsKey(networkConnexion)) {
					controlEventListeners.get(this.networkController.get(networkConnexion).get(index)).speedDown();
				}
			}
		}
	}

	/**************************************
	 * --- LOCAL CONTROLLER ---
	 **************************************/

	public void move(Controller controller, PovDirection direction) {
		if (this.localController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				controlEventListeners.get(this.localController.get(controller)).move(direction);
			} else if (game.getScreen().getClass().isInstance(SkinScreen.class)) {
				int idx = definitionControllerMapping.get(controller);
				switch (direction) {
				case center:
					break;
				case east:
					definitions[idx].setCharacter(CharacterEnum.next(definitions[idx].getCharacter()));
					break;
				case north:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					break;
				case northEast:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case northWest:
					definitions[idx].setColor(CharacterColorEnum.previous(definitions[idx].getColor()));
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				case south:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case southEast:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					break;
				case southWest:
					definitions[idx].setColor(CharacterColorEnum.next(definitions[idx].getColor()));
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				case west:
					definitions[idx].setCharacter(CharacterEnum.previous(definitions[idx].getCharacter()));
					break;
				}
				for (int i = 0; i < 16; i++) {
					if (definitions[i] != null) {
						Gdx.app.log("definition : ", "" + definitions[i].getCharacter().toString() + " "
								+ definitions[i].getColor().toString());
					}
				}
			}
		}
	}

	public void dropBombe(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				controlEventListeners.get(this.localController.get(controller)).dropBombe();
			}
		}
	}

	public void throwBombe(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				controlEventListeners.get(this.localController.get(controller)).throwBombe();
			}
		}
	}

	public void speedUp(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				controlEventListeners.get(this.localController.get(controller)).speedUp();
			}
		}
	}

	public void speedDown(Controller controller) {
		if (this.localController != null) {
			if (game.getScreen().getClass().isInstance(GameScreen.class)) {
				controlEventListeners.get(this.localController.get(controller)).speedDown();
			}
		}
	}

	/************************************************
	 * 
	 * -------Keyboard first human mapping -------
	 * 
	 ************************************************/
	public void move(PovDirection direction) {
		if (game.getScreen().getClass().isInstance(SkinScreen.class)) {
			if (firstHumanIdx != -1) {
				switch (direction) {
				case east:
					definitions[firstHumanIdx]
							.setCharacter(CharacterEnum.next(definitions[firstHumanIdx].getCharacter()));
					break;
				case north:
					definitions[firstHumanIdx]
							.setColor(CharacterColorEnum.previous(definitions[firstHumanIdx].getColor()));
					break;
				case south:
					definitions[firstHumanIdx].setColor(CharacterColorEnum.next(definitions[firstHumanIdx].getColor()));
					break;
				case west:
					definitions[firstHumanIdx]
							.setCharacter(CharacterEnum.previous(definitions[firstHumanIdx].getCharacter()));
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
		} else if (game.getScreen().getClass().isInstance(GameScreen.class)) {
			controlEventListeners.get(firstHumanIdx).move(direction);
		}
	}

	public void dropBombe() {
		if (game.getScreen().getClass().isInstance(GameScreen.class)) {
			controlEventListeners.get(firstHumanIdx).dropBombe();
		}
	}

	public void throwBombe() {
		if (game.getScreen().getClass().isInstance(GameScreen.class)) {
			controlEventListeners.get(firstHumanIdx).throwBombe();
		}
	}

	public void speedUp() {
		if (game.getScreen().getClass().isInstance(GameScreen.class)) {
			controlEventListeners.get(firstHumanIdx).speedUp();
		}
	}

	public void speedDown() {
		if (game.getScreen().getClass().isInstance(GameScreen.class)) {
			controlEventListeners.get(firstHumanIdx).speedDown();
		}
	}
}
