package com.mygdx.service.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;
import com.mygdx.service.Context;

public class DiscoveryServer extends Thread {

	private static final String CLASS_NAME = "DiscoveryServer.class";
	private static final int MAX_PACKET_SIZE = 2048;
	private DatagramSocket socket;
	private boolean status;

	@Override
	public void run() {
		Gdx.app.debug(CLASS_NAME, "start discovery serveur");
		this.status = true;
		final int max_errors = 5;
		int errorCount = 0;
		final String MY_IP = NetworkUtils.getMyAddress().getHostAddress();

		try {
			InetAddress addr = InetAddress.getByName("0.0.0.0");
			// InetAddress addr = NetworkUtils.getMyAddress();
			socket = new DatagramSocket(Constante.NETWORK_DISCOVERY_PORT, addr);
			socket.setBroadcast(true);
		} catch (Exception ex) {
			Gdx.app.error(CLASS_NAME, "Erreur lancement server discovery : " + Constante.NETWORK_DISCOVERY_PORT);
			return;
		}
		Gdx.app.log(CLASS_NAME, "Server listening on port : " + Constante.NETWORK_DISCOVERY_PORT);
		while (true) {
			// Receive a packet
			byte[] recvBuf = new byte[MAX_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
			try {
				// wait for a packet
				socket.receive(packet);
			} catch (IOException ioe) {
				errorCount++;
				if (errorCount >= max_errors)
					return;
				continue;
			}
			errorCount = 0;
			InetAddress clientAddress = packet.getAddress();
			int clientPort = packet.getPort();
			String message = new String(packet.getData()).trim();
			if (message.startsWith(Constante.NETWORK_DISCOVERY_REQUEST)) {
				String reply = Constante.NETWORK_DISCOVERY_REPLY + MY_IP + ":" + Context.getPort();
				byte[] sendData = reply.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
				try {
					socket.send(sendPacket);
				} catch (IOException ioe) {
					Gdx.app.error(CLASS_NAME, "IOException sending service discovery reply");
				}
			} else {
				Gdx.app.debug(CLASS_NAME, "Not a bomberman discovery request");
			}
			if (!status) {
				break;
			}
		}
	}

	public void kill() {
		if (status) {
			status = false;
			this.socket.close();
			Gdx.app.log(CLASS_NAME, "discovery server killed");
		}
	}

}
