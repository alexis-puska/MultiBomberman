package com.mygdx.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.mygdx.constante.Constante;

public class NetworkService {

	private final static String CLASS_NAME = "NetworkService";

	public NetworkService() {
		getIp();
		initServer();
		openPortWithUpnp();
	}

	public void getIp() {
		try {
			URL whatismyip = new URL(Constante.NETWORK_IP_SERVICE);
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			String[] ips = ip.split(", ");
			for(int i = 0;i<ips.length;i++) {
				Gdx.app.log("NetworkService", String.format("external ip : %s", ips[i]));
			}
		} catch (IOException ex) {
			Gdx.app.error("NetworkService", "Error getting ip from : ");
		}
		System.out.println("end");

	}

	public void initServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ServerSocketHints serverSocketHint = new ServerSocketHints();
				serverSocketHint.acceptTimeout = 0;
				serverSocketHint.reuseAddress = true;
				ServerSocket serverSocket = null;
				serverSocket = Gdx.net.newServerSocket(Protocol.TCP, 7777, serverSocketHint);
				while (true) {
					// Create a socket
					Socket socket = serverSocket.accept(null);
					NetworkConnexion nc = new NetworkConnexion(socket);
					nc.start();
				}
			}
		}).start();
	}

	public void openPortWithUpnp() {
		Gdx.app.log("Starting weupnp", "");
		GatewayDiscover discover = new GatewayDiscover();
		Gdx.app.log("Looking for Gateway Devices", "");
		try {
			discover.discover();
			GatewayDevice d = discover.getValidGateway();
			if (null != d) {
				Gdx.app.log(CLASS_NAME, "Found gateway device : " + d.getModelName() + " " + d.getModelDescription());
			} else {
				Gdx.app.log(CLASS_NAME, "No valid gateway device found.");
				return;
			}
			InetAddress localAddress = d.getLocalAddress();
			Gdx.app.log(CLASS_NAME, "Using local address: " + localAddress);
			String externalIPAddress = d.getExternalIPAddress();
			Gdx.app.log(CLASS_NAME, "External address: " + externalIPAddress);
			Gdx.app.log(CLASS_NAME, "Attempting to map port : " + Constante.NETWORK_PORT);
			PortMappingEntry portMapping = new PortMappingEntry();
			Gdx.app.log(CLASS_NAME,
					"Querying device to see if mapping for port " + Constante.NETWORK_PORT + " already exists");
			if (!d.getSpecificPortMappingEntry(Constante.NETWORK_PORT, "TCP", portMapping)) {
				Gdx.app.log(CLASS_NAME, "Port was already mapped. Aborting test.");
			} else {
				Gdx.app.log(CLASS_NAME, "Sending port mapping request");
				if (!d.addPortMapping(Constante.NETWORK_PORT, Constante.NETWORK_PORT, localAddress.getHostAddress(),
						"TCP", "test")) {
					Gdx.app.log(CLASS_NAME, "Port mapping attempt failed");
					Gdx.app.log(CLASS_NAME, "Test FAILED");
				} else {
					Gdx.app.log(CLASS_NAME,
							"Mapping successful: waiting {0} seconds before removing." + Constante.NETWORK_WAIT_TIME);
					Thread.sleep(1000 * Constante.NETWORK_WAIT_TIME);
					d.deletePortMapping(Constante.NETWORK_PORT, "TCP");

					Gdx.app.log(CLASS_NAME, "Port mapping removed");
					Gdx.app.log(CLASS_NAME, "Test SUCCESSFUL");
				}
			}
			Gdx.app.log(CLASS_NAME, "Stopping weupnp");
		} catch (SocketException e) {
			Gdx.app.log(CLASS_NAME, "SocketException : " + e.getMessage());
		} catch (UnknownHostException e) {
			Gdx.app.log(CLASS_NAME, "UnknownHostException : " + e.getMessage());
		} catch (IOException e) {
			Gdx.app.log(CLASS_NAME, "IOException : " + e.getMessage());
		} catch (SAXException e) {
			Gdx.app.log(CLASS_NAME, "SAXException : " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Gdx.app.log(CLASS_NAME, "ParserConfigurationException : " + e.getMessage());
		} catch (InterruptedException e) {
			Gdx.app.log(CLASS_NAME, "InterruptedException : " + e.getMessage());
		}
	}
}
