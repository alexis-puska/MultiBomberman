package com.mygdx.service.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.mygdx.service.NetworkConnexion;

public class Server extends Thread {

	private ServerSocket serverSocket;
	private static boolean status;
	
	@Override
	public void run() {
		status = true;
		ServerSocketHints serverSocketHint = new ServerSocketHints();
		serverSocketHint.acceptTimeout = 0;
		serverSocketHint.reuseAddress = true;
		serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 7777, serverSocketHint);
		System.out.println("start serveur");
		while (status) {
			// Create a socket
			Socket socket = serverSocket.accept(null);
			NetworkConnexion nc = new NetworkConnexion(socket);
			nc.start();
		}
	}

	public void kill() {
		status = false;
		serverSocket.dispose();
	}
	
	public boolean isStarted() {
		return status;
	}

}
