package com.mygdx.service.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
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

	private final static String CLASS_NAME = "NetworkService";
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
		Context.port = Constante.NETWORK_PORT;
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
			if (Context.useUpnp) {
				upnpService.openPortWithUpnp();
			}
		} catch (ServerPortAlreadyInUseException ex) {
			Gdx.app.log(CLASS_NAME, "Serveur KO");
			return false;
		}
		return true;
	}

	public void stopServer() {
		if (Context.useUpnp) {
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
			Socket client = Gdx.net.newClientSocket(Protocol.TCP, ip, port, socketHints);
			if (client.isConnected()) {
				Gdx.app.log("NetworkService", "connected !");
				this.client = new Client(game, client);
				this.client.start();
				return true;
			}
		} catch (GdxRuntimeException ex) {
			Gdx.app.log("CONNECT TO SERVER", "ERROR");
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
		try {
			URL whatismyip = new URL(Constante.NETWORK_IP_SERVICE);
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			String[] ips = ip.split(", ");
			if (ips.length > 0) {
				internetIp = ips[ips.length - 1];
			}
		} catch (IOException ex) {
			Gdx.app.error(CLASS_NAME, "Error getting internet ip from : " + Constante.NETWORK_IP_SERVICE);
		}
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			externalIp = inetAddress.getHostAddress();
			hostName = inetAddress.getHostName();

		} catch (UnknownHostException e) {
			Gdx.app.error(CLASS_NAME, "Error getting network info");
		}
		Gdx.app.log(CLASS_NAME, "IP internet:- " + internetIp);
		Gdx.app.log(CLASS_NAME, "IP Address:- " + externalIp);
		Gdx.app.log(CLASS_NAME, "Host Name:- " + hostName);
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

}
