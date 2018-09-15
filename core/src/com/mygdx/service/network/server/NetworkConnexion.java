package com.mygdx.service.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.PlayerService;

public class NetworkConnexion extends Thread {

	private final Socket socket;
	private final Server server;
	private PlayerService playerService;
	private final String remoteAddress;

	// active
	private static boolean status;

	// guid of client
	private String guid;

	private int player;

	public NetworkConnexion(Socket socket, final Server server, final MultiBombermanGame game) {
		status = true;
		this.socket = socket;
		this.server = server;

		// TODO to remove after network protocole up !
		this.player = 1;

		this.playerService = game.getPlayerService();
		this.remoteAddress = socket.getRemoteAddress();
		Gdx.app.log("NetworkConnexion", String.format("new client connexion : %s", remoteAddress));
	}

	public void close() {
		Gdx.app.log("NetworkConnexion", String.format("fermeture connexion de : %s", remoteAddress));
		socket.dispose();
		status = false;
	}

	@Override
	public void run() {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
				while (status) {

					String received = null;
					try {
						received = buffer.readLine();
					} catch (IOException ez) {
						Gdx.app.log("", ez.getMessage());
					}
					out.println(received);
					if (received == null) {
						Gdx.app.log("NetworkConnexion", String.format("Deconnection brutale de : %s", remoteAddress));
						status = false;
						break;
					}

					if (received.equals("end")) {
						Gdx.app.log("NetworkConnexion", String.format("Deconnection de : %s", remoteAddress));
						socket.dispose();
						status = false;
						break;
					}
					decode(received);
					if (status == true) {
					}
				}
			} catch (Exception e) {
				Gdx.app.log("Exception", e.getMessage());
			}
		} catch (Exception e) {
			Gdx.app.log("Exception", e.getMessage());
		}

	}

	private void decode(String received) {
		Gdx.app.log("NetworkConnexion", String.format("recu de %s : %s", remoteAddress, received));
		playerService.move(this.guid, 0, PovDirection.east);
	}

	private boolean decodeFirstStep(String receive) {

		return false;
	}

	public int getPlayer() {
		return player;
	}

	public String getGuid() {
		return guid;
	}

}
