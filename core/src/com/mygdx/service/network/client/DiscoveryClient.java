package com.mygdx.service.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.mygdx.constante.Constante;
import com.mygdx.service.network.dto.DiscoveryServerInfo;

public class DiscoveryClient {

	private static final String CLASS_NAME = "DiscoveryClient.class";
	private static final int MAX_PACKET_SIZE = 2048;
	private static final int TIMEOUT = 2000; // milliseonds

	public static void main(String[] args) {
		DiscoveryClient client = new DiscoveryClient();

		// run it here. This will hang until a response is received
		List<DiscoveryServerInfo> infos = client.call();
		if (!infos.isEmpty()) {
			infos.stream().forEach(i -> System.out.println("Got server address: " + i.toString()));
		} else {
			System.out.println("no server found");
		}
	}

	/**
	 * Create a UDP socket on the service discovery broadcast port.
	 * 
	 * @return open DatagramSocket if successful
	 * @throws RuntimeException if cannot create the socket
	 */
	public DatagramSocket createSocket() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
			socket.setSoTimeout(TIMEOUT);
		} catch (SocketException sex) {
			Gdx.app.error(CLASS_NAME, "SocketException creating broadcast socket : " + sex.getMessage());
			throw new RuntimeException(sex);
		}
		return socket;
	}

	/**
	 * Send broadcast packets with service request string until a response is
	 * received. Return the response as String (even though it should contain an
	 * internet address).
	 * 
	 * @return String received from server. Should be server IP address. Returns
	 *         empty string if failed to get valid reply.
	 */
	public List<DiscoveryServerInfo> call() {
		List<DiscoveryServerInfo> infos = new ArrayList<>();
		byte[] receiveBuffer = new byte[MAX_PACKET_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		DatagramSocket socket = createSocket();
		byte[] packetData = Constante.NETWORK_DISCOVERY_REQUEST.getBytes();
		InetAddress broadcastAddress = null;
		try {
			broadcastAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			Gdx.app.error(CLASS_NAME, "unknow broadcast adress");
		}
		int servicePort = Constante.NETWORK_DISCOVERY_PORT;
		DatagramPacket packet = new DatagramPacket(packetData, packetData.length, broadcastAddress, servicePort);
		// use a loop so we can resend broadcast after timeout
		String result = "";
		try {
			socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(
				CLASS_NAME + "Sent discovery packet to : " + broadcastAddress.getHostAddress() + " " + servicePort);
		while (true) {
			try {

				socket.receive(receivePacket);
				String reply = new String(receivePacket.getData());
				int k = reply.indexOf(Constante.NETWORK_DISCOVERY_REPLY);
				if (k < 0) {
//					logger.warning("Reply does not contain prefix "+DISCOVERY_REPLY);
					break;
				}
				k += Constante.NETWORK_DISCOVERY_REPLY.length(); // skip prefix
				result = reply.substring(k).trim();
				if (result.contains(":")) {
					String ip = result.substring(0, result.indexOf(':'));
					int port = Integer.parseInt(result.substring(result.indexOf(':') + 1));
					infos.add(new DiscoveryServerInfo(ip, port));
//					break;
				}
			} catch (SocketTimeoutException ste) {

				break;

				// time-out while waiting for reply. Send the broadcast again.
			} catch (IOException ioe) {
//				
				break;
			}
		}
		// should close the socket before returning
		if (socket != null)
			socket.close();
		return infos;
	}
}