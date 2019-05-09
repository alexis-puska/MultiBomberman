package com.mygdx.service.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.service.Context;
import com.mygdx.service.SoundService;
import com.mygdx.service.network.NetworkService;
import com.mygdx.view.ClientViewScreen;

public class Client extends Thread {

	private static final String CLASS_NAME = "Client.class";

	private boolean status;
	private Socket socket;
	private BufferedReader bufferReader;
	private OutputStream outputStream;
	private ClientViewScreen viewScreen;
	private String errorCodeReceive;
	private NetworkService networkService;

	private boolean canSendEvent;

	public Client(Socket socket, NetworkService networkService, ClientViewScreen clientViewScreen) {
		this.socket = socket;
		this.networkService = networkService;
		this.viewScreen = clientViewScreen;
		this.status = true;
		this.canSendEvent = false;
		outputStream = socket.getOutputStream();
		bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void run() {
		Gdx.app.debug(CLASS_NAME, "start client");
		while (status) {
			try {
				String line = bufferReader.readLine();
				if (line == null) {
					Gdx.app.log(CLASS_NAME, "buffer null connexion lost");
					this.status = false;
					this.networkService.setLastClientError("connexionLost");
				} else {
					Gdx.app.debug(CLASS_NAME, line);
					// request uuid by server
					if (line.startsWith("hello")) {
						outputStream.write(("uuid:" + Context.getUuid() + "\n").getBytes());
					} else if (line.startsWith("nbp")) {
						outputStream.write(("nbp:" + Context.getLocalPlayer() + "\n").getBytes());
					} else if (line.startsWith("event")) {
						this.canSendEvent = true;
					} else if (line.startsWith("welcome_back")) {
						this.canSendEvent = true;
					} else if (line.startsWith("error")) {
						this.networkService.setLastClientError(line);
						Gdx.app.error(CLASS_NAME, line);
					} else if (line.startsWith("game:") && viewScreen != null) {
						viewScreen.receiveGame(line);
					} else if (line.startsWith(SoundService.PREFIX) && viewScreen != null) {
						viewScreen.receiveSound(line);
					} else if (line.startsWith("levelScreen:") && viewScreen != null) {
						viewScreen.receiveLevelScreen(line);
					} else if (line.startsWith("waitScreen:") && viewScreen != null) {
						viewScreen.receiveWaitScreen(line);
					} else if (line.startsWith("rulesScreen:") && viewScreen != null) {
						viewScreen.receiveRuleScreen(line);
					} else if (line.startsWith("skinsScreen:") && viewScreen != null) {
						viewScreen.receiveSkinScreen(line);
					} else if (line.startsWith("levelDefinition:") && viewScreen != null) {
						viewScreen.receiveLevelDef(line);
					} else {
						Gdx.app.log("Client", "recu et non reconnu : " + line);
					}
				}
			} catch (IOException e) {
				status = false;
				this.networkService.setLastClientError("connexionLost");
				Gdx.app.debug(CLASS_NAME, "IOException run 2");
			}
		}
		try {
			this.bufferReader.close();
			this.outputStream.close();
		} catch (IOException e) {
			Gdx.app.debug(CLASS_NAME, "IOException run");
		}
	}

	public void kill() {
		status = false;
		this.socket.dispose();
	}

	public boolean isStatus() {
		return status;
	}

	public String getErrorCodeReceive() {
		return errorCodeReceive;
	}

	/**
	 * Send button event to server
	 * 
	 * @param buffer
	 *            value of button pressed by an controller
	 */
	public void send(byte[] buffer) {
		if (status && canSendEvent) {
			try {
				outputStream.write(buffer);
			} catch (IOException e) {
				Gdx.app.debug(CLASS_NAME, "IOException send");
			}
		}
	}
}
