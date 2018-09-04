package com.mygdx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;

public class NetworkConnexion extends Thread {

	private final Socket socket;
	private final String remoteAddress;
	private static boolean status;

	public NetworkConnexion(Socket socket) {
		this.socket = socket;
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
