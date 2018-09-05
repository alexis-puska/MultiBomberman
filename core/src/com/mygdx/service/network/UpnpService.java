package com.mygdx.service.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.xml.parsers.ParserConfigurationException;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import org.xml.sax.SAXException;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;

public class UpnpService {

	private final static String CLASS_NAME = "UpnpService";
	private final GatewayDiscover gatewayDiscover;
	private GatewayDevice gatewayDevice;

	public UpnpService() {
		gatewayDiscover = new GatewayDiscover();
	}

	public void openPortWithUpnp() {
		Gdx.app.log("Starting weupnp", "");
		Gdx.app.log("Looking for Gateway Devices", "");
		try {
			gatewayDiscover.discover();
			gatewayDevice = gatewayDiscover.getValidGateway();
			if (null != gatewayDevice) {
				Gdx.app.log(CLASS_NAME, "Found gateway device : " + gatewayDevice.getModelName() + " "
						+ gatewayDevice.getModelDescription());
			} else {
				Gdx.app.log(CLASS_NAME, "No valid gateway device found.");
				return;
			}
			InetAddress localAddress = gatewayDevice.getLocalAddress();
			String externalIPAddress = gatewayDevice.getExternalIPAddress();
			Gdx.app.log(CLASS_NAME, "Using local address: " + localAddress + ", External address: " + externalIPAddress
					+ ", Attempting to map port : " + Constante.NETWORK_PORT);
			PortMappingEntry portMapping = new PortMappingEntry();
			if (gatewayDevice.getSpecificPortMappingEntry(Constante.NETWORK_PORT, "TCP", portMapping)) {
				Gdx.app.log(CLASS_NAME, "Port was already mapped. Aborting test.");
			} else {
				Gdx.app.log(CLASS_NAME, "Sending port mapping request");
				if (!gatewayDevice.addPortMapping(Constante.NETWORK_PORT, Constante.NETWORK_PORT,
						localAddress.getHostAddress(), "TCP", Constante.NETWORK_APPLICATION_UPNP_NAME)) {
					Gdx.app.log(CLASS_NAME, "Port mapping attempt failed");
				} else {
					Gdx.app.log(CLASS_NAME, "Mapping successful !!!");
				}
			}
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
		}
	}

	public void closePortWithUpnp() {
		if (gatewayDevice != null) {
			Gdx.app.log(CLASS_NAME, "Stopping weupnp");
			try {
				gatewayDevice.deletePortMapping(Constante.NETWORK_PORT, "TCP");
			} catch (IOException e) {
				Gdx.app.log(CLASS_NAME, "IOException : " + e.getMessage());
			} catch (SAXException e) {
				Gdx.app.log(CLASS_NAME, "SAXException : " + e.getMessage());
			}
			Gdx.app.log(CLASS_NAME, "Port mapping removed");
		}
	}
}
