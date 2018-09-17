package com.mygdx.service.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.mygdx.service.Context;

public class Client extends Thread {

	private final static String CLASS_NAME = "Client.class";

	private boolean status;
	private Socket client;
	private BufferedReader bufferReader;
	private OutputStream outputStream;

	private String receive;
	private boolean canSendEvent;

	public Client(Socket client) {
		this.client = client;
		this.status = true;
		this.canSendEvent = false;
		outputStream = client.getOutputStream();
		bufferReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
					break;
				}
				Gdx.app.debug(CLASS_NAME, line);

				//request uuid by server
				if (line.startsWith("hello")) {
					outputStream.write(("uuid:" + Context.getUuid() + "\n").getBytes());
				}
				if (line.startsWith("nbp")) {
					outputStream.write("nbp:1\n".getBytes());
				}
				if(line.startsWith("event")) {
					this.canSendEvent = true;
				}
				if(line.startsWith("welcome_back")) {
					this.canSendEvent = true;
				}
				
				this.receive = line;
			} catch (IOException e) {
				status = false;
				Gdx.app.debug(CLASS_NAME, "IOException run 2");
				break;
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
	 * @param buffer value of button pressed by an controller
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
