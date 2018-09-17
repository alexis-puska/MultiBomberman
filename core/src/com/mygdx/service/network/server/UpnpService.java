package com.mygdx.service.network.server;

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
import com.mygdx.service.Context;

public class UpnpService {

	private static final String CLASS_NAME = "UpnpService";
	private final GatewayDiscover gatewayDiscover;
	private GatewayDevice gatewayDevice;

	public UpnpService() {
		gatewayDiscover = new GatewayDiscover();
	}

	public void openPortWithUpnp() {
		Gdx.app.debug(CLASS_NAME,"Starting weupnp");
		Gdx.app.debug(CLASS_NAME, "Looking for Gateway Devices");
		try {
			gatewayDiscover.discover();
			gatewayDevice = gatewayDiscover.getValidGateway();
			if (null != gatewayDevice) {
				Gdx.app.debug(CLASS_NAME, "Found gateway device : " + gatewayDevice.getModelName() + " "
						+ gatewayDevice.getModelDescription());
			} else {
				Gdx.app.debug(CLASS_NAME, "No valid gateway device found.");
				return;
			}
			InetAddress localAddress = gatewayDevice.getLocalAddress();
			String externalIPAddress = gatewayDevice.getExternalIPAddress();
			Gdx.app.debug(CLASS_NAME, "Using local address: " + localAddress + ", External address: " + externalIPAddress
					+ ", Attempting to map port : " + Context.getPort());
			PortMappingEntry portMapping = new PortMappingEntry();
			if (gatewayDevice.getSpecificPortMappingEntry(Context.getPort(), "TCP", portMapping)) {
				Gdx.app.debug(CLASS_NAME, "Port was already mapped. Aborting test.");
			} else {
				Gdx.app.debug(CLASS_NAME, "Sending port mapping request");
				if (!gatewayDevice.addPortMapping(Context.getPort(), Context.getPort(),
						localAddress.getHostAddress(), "TCP", Constante.NETWORK_APPLICATION_UPNP_NAME)) {
					Gdx.app.debug(CLASS_NAME, "Port mapping attempt failed");
				} else {
					Gdx.app.debug(CLASS_NAME, "Mapping successful !!!");
				}
			}
		} catch (SocketException e) {
			Gdx.app.error(CLASS_NAME, "SocketException : " + e.getMessage());
		} catch (UnknownHostException e) {
			Gdx.app.error(CLASS_NAME, "UnknownHostException : " + e.getMessage());
		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : " + e.getMessage());
		} catch (SAXException e) {
			Gdx.app.error(CLASS_NAME, "SAXException : " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Gdx.app.error(CLASS_NAME, "ParserConfigurationException : " + e.getMessage());
		}
	}

	public void closePortWithUpnp() {
		if (gatewayDevice != null) {
			Gdx.app.debug(CLASS_NAME, "Stopping weupnp");
			try {
				gatewayDevice.deletePortMapping(Context.getPort(), "TCP");
			} catch (IOException e) {
				Gdx.app.error(CLASS_NAME, "IOException : " + e.getMessage());
			} catch (SAXException e) {
				Gdx.app.error(CLASS_NAME, "SAXException : " + e.getMessage());
			}
			Gdx.app.debug(CLASS_NAME, "Port mapping removed");
		}
	}
}
