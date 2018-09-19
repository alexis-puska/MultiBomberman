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
import com.mygdx.view.ClientViewScreen;

public class NetworkService {

	private static final String CLASS_NAME = "NetworkService";

	private static final String EVENT = "event:";
	private final MultiBombermanGame game;
	private Server server;
	private Client client;
	private UpnpService upnpService;

	private String externalIp;
	private String hostName;
	private String internetIp;

	private String lastClientError;

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

	public void sendToClient(String value) {
		server.send((value + "\n").getBytes());
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

	public String getLastClientError() {
		return lastClientError;
	}

	public void resetLastClientError() {
		this.lastClientError = null;
	}

	public void setLastClientError(String lastClientError) {
		if (this.lastClientError == null) {
			this.lastClientError = lastClientError;
		}
	}

	public void acceptConnexion(boolean state) {
		this.server.acceptConnexion(state);
	}

	/***********************************
	 * ----- client part -----
	 ***********************************/
	public boolean connectToServer(int port, String ip) {
		this.resetLastClientError();
		SocketHints socketHints = new SocketHints();
		socketHints.connectTimeout = 1000;
		try {
			Socket clientSocket = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
			if (clientSocket.isConnected()) {
				Gdx.app.debug(CLASS_NAME, "connected !");
				this.client = new Client(clientSocket, this);
				this.client.start();
				return true;
			}
		} catch (GdxRuntimeException ex) {
			Gdx.app.error(CLASS_NAME, "CONNECT TO SERVER ERROR");
			lastClientError = "serverUnreachable";
			return false;
		}
		return false;
	}

	public void disconnectFromServer() {
		this.client.kill();
	}

	public void setClientViewScreen(ClientViewScreen viewScreen) {
		this.client.setViewScreen(viewScreen);
	}

	public void sendDirection(Integer integer, PovDirection direction) {
		if (this.client != null && this.client.isStatus() && integer < Context.getLocalPlayer()) {
			switch (direction) {
			case center:
				this.client.send((EVENT + integer + ":CENTER\n").getBytes());
				break;
			case east:
				this.client.send((EVENT + integer + ":RIGHT\n").getBytes());
				break;
			case north:
				this.client.send((EVENT + integer + ":UP\n").getBytes());
				break;
			case south:
				this.client.send((EVENT + integer + ":DOWN\n").getBytes());
				break;
			case west:
				this.client.send((EVENT + integer + ":LEFT\n").getBytes());
				break;
			case southEast:
			case southWest:
			case northEast:
			case northWest:
				break;
			default:
				this.client.send((EVENT + integer + ":CENTER\n").getBytes());
				break;

			}
		}
	}

	public void sendDropBombe(Integer integer) {
		if (this.client != null && this.client.isStatus() && integer < Context.getLocalPlayer()) {
			this.client.send((EVENT + integer + ":DROP\n").getBytes());
		}
	}

	public void sendSpeedUp(Integer integer) {
		if (this.client != null && this.client.isStatus() && integer < Context.getLocalPlayer()) {
			this.client.send((EVENT + integer + ":SPEED_UP\n").getBytes());
		}
	}

	public void sendSpeedDown(Integer integer) {
		if (this.client != null && this.client.isStatus() && integer < Context.getLocalPlayer()) {
			this.client.send((EVENT + integer + ":SPEED_DOWN\n").getBytes());
		}
	}

	public void sendThrowBombe(Integer integer) {
		if (this.client != null && this.client.isStatus() && integer < Context.getLocalPlayer()) {
			this.client.send((EVENT + integer + ":THROW\n").getBytes());
		}
	}
}
