package com.mygdx.service.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.mygdx.constante.Constante;
import com.mygdx.exception.ServerPortAlreadyInUseException;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.Context;
import com.mygdx.service.network.client.Client;
import com.mygdx.service.network.server.Server;
import com.mygdx.service.network.server.UpnpService;

public class NetworkService {

	private static final String CLASS_NAME = "NetworkService";
	private final MultiBombermanGame game;
	private Server server;
	private Client client;
	private UpnpService upnpService;

	private String externalIp;
	private String hostName;
	private String internetIp;

	public NetworkService(final MultiBombermanGame game) {
		this.game = game;
		upnpService = new UpnpService();
		Context.setPort(Constante.NETWORK_PORT);
		retrieveIp();
	}

	/***********************************
	 * ----- Server part -----
	 ***********************************/
	public boolean initServer() {
		server = new Server(game);
		try {
			server.init();
			server.start();
			if (Context.isUseUpnp()) {
				upnpService.openPortWithUpnp();
			}
		} catch (ServerPortAlreadyInUseException ex) {
			Gdx.app.error(CLASS_NAME, "Serveur KO");
			return false;
		}
		return true;
	}

	public void stopServer() {
		if (Context.isUseUpnp()) {
			upnpService.closePortWithUpnp();
		}
		server.kill();
	}

	
	/***********************************
	 * ----- client part -----
	 ***********************************/
	public boolean connectToServer(int port, String ip) {
		SocketHints socketHints = new SocketHints();
		socketHints.connectTimeout = 1000;
		try {
			Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
			if (clientSocket.isConnected()) {
				Gdx.app.debug(CLASS_NAME, "connected !");
				this.client = new Client(clientSocket);
				this.client.start();
				return true;
			}
		} catch (GdxRuntimeException ex) {
			Gdx.app.error(CLASS_NAME, "CONNECT TO SERVER ERROR");
			return false;
		}
		return false;
	}

	public void disconnectFromServer() {
		this.client.kill();
	}

	/***********************************
	 * ----- other -----
	 ***********************************/
	private void retrieveIp() {
		URL whatismyip;
		try {
			whatismyip = new URL(Constante.NETWORK_IP_SERVICE);
			try (BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()))) {
				String ip = in.readLine();
				String[] ips = ip.split(", ");
				if (ips.length > 0) {
					internetIp = ips[ips.length - 1];
				}
				InetAddress inetAddress = InetAddress.getLocalHost();
				externalIp = inetAddress.getHostAddress();
				hostName = inetAddress.getHostName();
			} catch (IOException ex) {
				Gdx.app.error(CLASS_NAME, "Error getting internet ip from : " + Constante.NETWORK_IP_SERVICE);
			}
		} catch (MalformedURLException e1) {
			Gdx.app.error(CLASS_NAME, "MalformedURLException : " + e1.getMessage());

		}
		Gdx.app.debug(CLASS_NAME, "IP internet:- " + internetIp);
		Gdx.app.debug(CLASS_NAME, "IP Address:- " + externalIp);
		Gdx.app.debug(CLASS_NAME, "Host Name:- " + hostName);
	}

	public String getExternalIp() {
		return externalIp;
	}

	public String getHostName() {
		return hostName;
	}

	public String getInternetIp() {
		return internetIp;
	}

	public Server getServer() {
		return server;
	}

	public Client getClient() {
		return client;
	}

	public void acceptConnexion(boolean state) {
		this.server.acceptConnexion(state);
	}

	public void sendDirection(Integer integer, PovDirection direction) {
		if (this.client != null && this.client.isStatus()) {

			switch (direction) {
			case center:
				this.client.send(("event:" + integer + ":CENTER\n").getBytes());
				break;
			case east:
				this.client.send(("event:" + integer + ":RIGHT\n").getBytes());
				break;
			case north:
				this.client.send(("event:" + integer + ":UP\n").getBytes());
				break;
			case northEast:
				break;
			case northWest:
				break;
			case south:
				this.client.send(("event:" + integer + ":DOWN\n").getBytes());
				break;
			case southEast:
				break;
			case southWest:
				break;
			case west:
				this.client.send(("event:" + integer + ":LEFT\n").getBytes());
				break;
			default:
				this.client.send(("event:" + integer + ":CENTER\n").getBytes());
				break;
			}
		}
	}

	public void sendDropBombe(Integer integer) {
		if (this.client != null && this.client.isStatus()) {
			this.client.send(("event:" + integer + ":DROP\n").getBytes());
		}
	}

	public void sendSpeedUp(Integer integer) {
		if (this.client != null && this.client.isStatus()) {
			this.client.send(("event:" + integer + ":SPEED_UP\n").getBytes());
		}
	}

	public void sendSpeedDown(Integer integer) {
		if (this.client != null && this.client.isStatus()) {
			this.client.send(("event:" + integer + ":SPEED_DOWN\n").getBytes());
		}
	}

	public void sendThrowBombe(Integer integer) {
		if (this.client != null && this.client.isStatus()) {
			this.client.send(("event:" + integer + ":THROW\n").getBytes());
		}
	}

}
