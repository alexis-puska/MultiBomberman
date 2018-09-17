package com.mygdx.service.network.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.exception.ServerPortAlreadyInUseException;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;

public class Server extends Thread {

	private static final String CLASS_NAME = "Server.class";

	private final MultiBombermanGame game;
	private ServerSocket serverSocket;
	private List<NetworkConnexion> connexions;
	private Map<String, NetworkConnexion> connexionsValide;
	private boolean status;
	private boolean acceptNewConnexion;
	private int player;

	public Server(final MultiBombermanGame game) {
		this.game = game;
		this.acceptNewConnexion = true;
	}

	@Override
	public void run() {
		Gdx.app.debug(CLASS_NAME, "start serveur");
		while (status) {
			// Create a socket
			try {
				Socket socket = serverSocket.accept(null);
				NetworkConnexion nc = new NetworkConnexion(socket, this, game);
				nc.start();
				connexions.add(nc);
			} catch (GdxRuntimeException ex) {
				Gdx.app.error(CLASS_NAME, "GDX Runtime exception : arret du serveur");
			}
		}
	}

	public void kill() {
		status = false;
		connexions.stream().forEach(nc -> nc.close());
		connexionsValide.entrySet().stream().forEach(e -> e.getValue().close());
		if (serverSocket != null) {
			Gdx.app.debug(CLASS_NAME, "close server");
			serverSocket.dispose();
		}
	}

	public boolean isStarted() {
		return status;
	}

	public void init() throws ServerPortAlreadyInUseException {
		status = true;
		connexions = new ArrayList<>();
		connexionsValide = new HashMap<>();
		acceptNewConnexion = true;
		ServerSocketHints serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0;
		serverSocketHint.reuseAddress = true;
		try {
			serverSocket = Gdx.net.newServerSocket(Protocol.TCP, Context.getPort(), serverSocketHint);
		} catch (GdxRuntimeException ex) {
			throw new ServerPortAlreadyInUseException();
		}

	}

	public boolean isAcceptNewConnexion() {
		return acceptNewConnexion;
	}

	public Map<String, NetworkConnexion> getConnexions() {
		return connexionsValide;
	}

	public void acceptConnexion(boolean state) {
		acceptNewConnexion = state;
	}

	public boolean connexionAlreadyExistBefore(NetworkConnexion networkConnexion) {
		if (connexionsValide.containsKey(networkConnexion.getUuid())) {
			NetworkConnexion old = connexionsValide.get(networkConnexion.getUuid());
			old.close();
			connexionsValide.put(networkConnexion.getUuid(), networkConnexion);
			connexions.remove(networkConnexion);
			return true;
		} else {
			connexions.remove(networkConnexion);
			return false;
		}
	}

	public int getPlayer() {
		return player;
	}

	public void removePlayerDeconnexionBeforeValide(int player) {
		this.player -= player;
	}

	public void valideConnexion(NetworkConnexion networkConnexion) {
		if (!connexionsValide.containsKey(networkConnexion.getUuid())) {
			player += networkConnexion.getPlayer();
		}
		connexionsValide.put(networkConnexion.getUuid(), networkConnexion);
		connexions.remove(networkConnexion);
	}

	public void send(byte[] value) {
		for (Entry<String, NetworkConnexion> valide : connexionsValide.entrySet()) {
			valide.getValue().send(value);
		}
	}
}
