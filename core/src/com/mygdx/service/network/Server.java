package com.mygdx.service.network;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.service.NetworkConnexion;

public class Server extends Thread {

	private ServerSocket serverSocket;
	private List<NetworkConnexion> ncl;
	private static boolean status;

	@Override
	public void run() {
		System.out.println("start serveur");
		while (status) {
			// Create a socket
			try {
				Socket socket = serverSocket.accept(null);
				NetworkConnexion nc = new NetworkConnexion(socket);
				nc.start();
				ncl.add(nc);
			} catch (GdxRuntimeException ex) {
				Gdx.app.log("Server", "GDX Runtime exception : arret du serveur");
			}
		}
	}

	public void kill() {
		status = false;
		ncl.stream().forEach(nc -> nc.close());
		if (serverSocket != null) {
			Gdx.app.log("Server", "close server");
			serverSocket.dispose();
		}
	}

	public boolean isStarted() {
		return status;
	}

	public void init() throws ServerPortAlreadyInUseException{
		status = true;
		ncl = new ArrayList<>();
		ServerSocketHints serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0;
		serverSocketHint.reuseAddress = true;
		try {
			serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 7777, serverSocketHint);
		}catch ( GdxRuntimeException ex) {
			throw new ServerPortAlreadyInUseException();
		}
		
	}
}
