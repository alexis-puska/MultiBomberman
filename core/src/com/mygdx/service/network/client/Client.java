package com.mygdx.service.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.game.MultiBombermanGame;

public class Client extends Thread {

	private final MultiBombermanGame game;
	private boolean status;
	private BufferedReader bufferReader;
	private OutputStream outputStream;

	public Client(final MultiBombermanGame game, Socket client) {
		this.game = game;
		this.status = true;
		outputStream = client.getOutputStream();
		bufferReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}

	@Override
	public void run() {
		System.out.println("start client");
		while (status) {
			try {
				String line = bufferReader.readLine();
				if (line == null) {
					Gdx.app.log("CLIENT", "buffer null connexion lost");
					this.status = false;
					break;
				}
				Gdx.app.log("CLIENT", line);
			} catch (IOException e) {
				Gdx.app.log("Client", "IOException run");
			}
		}
		try {
			this.bufferReader.close();
			this.outputStream.close();
		} catch (IOException e) {
			Gdx.app.log("Client", "IOException run");
		}
	}

	public void send(byte[] exp) {
		if (status) {
			try {
				outputStream.write(exp);
			} catch (IOException e) {
				Gdx.app.log("Client", "IOException send");
			}
		}
	}
}
