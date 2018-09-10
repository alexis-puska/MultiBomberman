package com.mygdx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.network.Server;

public class NetworkConnexion extends Thread {

	private final MultiBombermanGame game;
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
		this.game = game;
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
		BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		while (status) {
			try {
				String received = buffer.readLine();
				out.println(received);
				if (received == null) {
					Gdx.app.log("NetworkConnexion", String.format("Deconnection brutale de : %s", remoteAddress));
					status = false;
					break;
				}
				Gdx.app.log("NetworkConnexion", String.format("recu de %s : %s", remoteAddress, received));
				if (received.equals("end")) {
					Gdx.app.log("NetworkConnexion", String.format("Deconnection de : %s", remoteAddress));
					socket.dispose();
					status = false;
					break;
				}

				if (status == true) {

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void decode(String received) {

		playerService.move(this, 0, PovDirection.center);
	}
}
