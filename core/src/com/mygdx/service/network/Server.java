package com.mygdx.service.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.exception.ServerPortAlreadyInUseException;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.NetworkConnexion;

public class Server extends Thread {

	private final MultiBombermanGame game;
	private ServerSocket serverSocket;
	private List<NetworkConnexion> temporaryConnexion;
	private Map<String, NetworkConnexion> valideConnexion;
	private static boolean status;

	public Server(final MultiBombermanGame game) {
		this.game = game;
	}

	@Override
	public void run() {
		System.out.println("start serveur");
		while (status) {
			// Create a socket
			try {
				Socket socket = serverSocket.accept(null);
				NetworkConnexion nc = new NetworkConnexion(socket, this, game);
				nc.start();
				temporaryConnexion.add(nc);
			} catch (GdxRuntimeException ex) {
				Gdx.app.log("Server", "GDX Runtime exception : arret du serveur");
			}
		}
	}

	public void kill() {
		status = false;
		temporaryConnexion.stream().forEach(nc -> nc.close());
		valideConnexion.entrySet().stream().forEach(nc -> nc.getValue().close());
		if (serverSocket != null) {
			Gdx.app.log("Server", "close server");
			serverSocket.dispose();
		}
	}

	public boolean isStarted() {
		return status;
	}

	public void init() throws ServerPortAlreadyInUseException {
		status = true;
		temporaryConnexion = new ArrayList<>();
		ServerSocketHints serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0;
		serverSocketHint.reuseAddress = true;
		try {
			serverSocket = Gdx.net.newServerSocket(Protocol.TCP, Context.port, serverSocketHint);
		} catch (GdxRuntimeException ex) {
			throw new ServerPortAlreadyInUseException();
		}

	}

	public List<NetworkConnexion> getNcl() {
		return temporaryConnexion;
	}
}
