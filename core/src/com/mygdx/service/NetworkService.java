package com.mygdx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;
import com.mygdx.service.network.Server;
import com.mygdx.service.network.ServerPortAlreadyInUseException;
import com.mygdx.service.network.UpnpService;

public class NetworkService {

	private final static String CLASS_NAME = "NetworkService";
	private Server server;
	private UpnpService upnpService;
	private String externalIp;
	

	public NetworkService() {
		getIp();
		upnpService = new UpnpService();
	}

	public void getIp() {
		try {
			URL whatismyip = new URL(Constante.NETWORK_IP_SERVICE);
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			String[] ips = ip.split(", ");
			for (int i = 0; i < ips.length; i++) {
				Gdx.app.log(CLASS_NAME, String.format("external ip : %s", ips[i]));
			}
		} catch (IOException ex) {
			Gdx.app.error(CLASS_NAME, "Error getting ip from : ");
		}
	}

	public boolean initServer() {
		server = new Server();
		try {
			server.init();
			server.start();
//			upnpService.openPortWithUpnp();
		} catch (ServerPortAlreadyInUseException ex) {
			Gdx.app.log("", "Serveur KO");
			return false;
		}
		return true;
	}

	public void stopServer() {
//		upnpService.closePortWithUpnp();
		server.kill();
	}
}
