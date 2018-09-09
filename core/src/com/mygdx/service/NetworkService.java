package com.mygdx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;
import com.mygdx.exception.ServerPortAlreadyInUseException;
import com.mygdx.service.network.Server;
import com.mygdx.service.network.UpnpService;

public class NetworkService {

	private final static String CLASS_NAME = "NetworkService";
	private Server server;
	private UpnpService upnpService;

	private String externalIp;
	private String hostName;
	private String internetIp;

	public NetworkService() {
		upnpService = new UpnpService();
		Context.port = Constante.NETWORK_PORT;
		retrieveIp();
	}

	public boolean initServer() {
		server = new Server();
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

}
