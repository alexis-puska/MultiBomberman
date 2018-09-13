package com.mygdx.service.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.net.Socket;

public class Client extends Thread {

	private boolean status;
	private Socket client;
	private BufferedReader bufferReader;
	private OutputStream outputStream;

	private String receive;

	public Client(Socket client) {
		this.client = client;
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
				this.receive = line;
			} catch (IOException e) {
				status = false;
				Gdx.app.log("Client", "IOException run 2");
				break;
			}
		}
		try {
			this.bufferReader.close();
			this.outputStream.close();
		} catch (IOException e) {
			Gdx.app.log("Client", "IOException run");
		}

	}

	

	public void kill() {
		status = false;
		this.client.dispose();
	}

	public boolean isStatus() {
		return status;
	}

	public String getReceive() {
		return receive;
	}

	
	/**
	 * Send button event to server
	 * 
	 * @param buffer
	 *            value of button pressed by an controller
	 */
	public void send(byte[] buffer) {
		if (status) {
			try {
				outputStream.write(buffer);
			} catch (IOException e) {
				Gdx.app.log("Client", "IOException send");
			}
		}
	}
	
	
}
