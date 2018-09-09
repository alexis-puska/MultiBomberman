package com.mygdx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.mygdx.domain.Player;
import com.mygdx.domain.PlayerDefinition;
import com.mygdx.service.input_processor.ControlEventListener;

/**
 * @author Alexis PUSKARCZYK
 *
 */
public class PlayerService {

	private Map<NetworkConnexion, Map<Integer, ControlEventListener>> networkController;
	private Map<Controller, ControlEventListener> localController;
	private PlayerDefinition[] definitions;

	public PlayerService() {
		networkController = new HashMap<>();
		localController = new HashMap<>();
		definitions = new PlayerDefinition[16];
	}

	public void setNetworkConnexion(NetworkConnexion networkConnexion) {
		
	}
	
	public List<Player> generatePlayer() {
		return null;
	}

	/**************************************
	 * --- NETWORK CONTROLLER ---
	 **************************************/
	public void move(NetworkConnexion networkConnextion, int index, PovDirection direction) {
		this.networkController.get(networkConnextion).get(index).move(direction);
	}

	public void dropBombe(NetworkConnexion networkConnexion, int index) {
		this.networkController.get(networkConnexion).get(index).dropBombe();
	}

	public void throwBombe(NetworkConnexion networkConnexion, int index) {
		this.networkController.get(networkConnexion).get(index).throwBombe();
	}

	public void speedUp(NetworkConnexion networkConnexion, int index) {
		this.networkController.get(networkConnexion).get(index).speedUp();
	}

	public void speedDown(NetworkConnexion networkConnexion, int index) {
		this.networkController.get(networkConnexion).get(index).speedDown();
	}

	/**************************************
	 * --- LOCAL CONTROLLER ---
	 **************************************/
	public void move(Controller controller, PovDirection direction) {
		this.localController.get(controller).move(direction);
	}

	public void dropBombe(Controller controller) {
		this.localController.get(controller).dropBombe();
	}

	public void throwBombe(Controller controller) {
		this.localController.get(controller).throwBombe();
	}

	public void speedUp(Controller controller) {
		this.localController.get(controller).speedUp();
	}

	public void speedDown(Controller controller) {
		this.localController.get(controller).speedDown();
	}

}
