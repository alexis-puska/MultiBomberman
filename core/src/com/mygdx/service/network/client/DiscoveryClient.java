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
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.server.Server;
import com.mygdx.dto.server.ServerList;
import com.mygdx.service.network.dto.DiscoveryServerInfo;

public class DiscoveryClient {

	private static final String CLASS_NAME = "DiscoveryClient.class";
	private static final int MAX_PACKET_SIZE = 2048;
	private static final int TIMEOUT = 2000; // milliseonds

	private ObjectMapper mapper;
	private List<Server> internetServer;
	private List<DiscoveryServerInfo> localNetworkServer;

	public DiscoveryClient() {
		this.mapper = new ObjectMapper();
		this.internetServer = null;
		this.localNetworkServer = null;
	}

	public List<Server> getInternetServer() {
		return internetServer;
	}

	public List<DiscoveryServerInfo> getLocalNetworkServer() {
		return localNetworkServer;
	}

	public void refreshLocalNetworkServerList() {
		localNetworkServer = null;
		byte[] receiveBuffer = new byte[MAX_PACKET_SIZE];
		DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		String result = "";
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setBroadcast(true);
			socket.setSoTimeout(TIMEOUT);
			byte[] packetData = Constante.NETWORK_DISCOVERY_REQUEST.getBytes();
			InetAddress broadcastAddress = null;
			broadcastAddress = InetAddress.getByName("255.255.255.255");
			int servicePort = Constante.NETWORK_DISCOVERY_PORT;
			DatagramPacket packet = new DatagramPacket(packetData, packetData.length, broadcastAddress, servicePort);
			socket.send(packet);
			Gdx.app.log(CLASS_NAME,
					"Sent discovery packet to : " + broadcastAddress.getHostAddress() + " " + servicePort);
			while (true) {
				try {

					socket.receive(receivePacket);
					String reply = new String(receivePacket.getData());
					int k = reply.indexOf(Constante.NETWORK_DISCOVERY_REPLY);
					if (k < 0) {
						break;
					}
					k += Constante.NETWORK_DISCOVERY_REPLY.length(); // skip prefix
					result = reply.substring(k).trim();
					if (result.contains(":")) {
						String ip = result.substring(0, result.indexOf(':'));
						int port = Integer.parseInt(result.substring(result.indexOf(':') + 1));
						if (localNetworkServer == null) {
							localNetworkServer = new ArrayList<>();
						}
						localNetworkServer.add(new DiscoveryServerInfo(ip, port));
					}
				} catch (SocketTimeoutException ste) {
					break;
				} catch (IOException ioe) {
					break;
				}
			}
		} catch (UnknownHostException e) {
			Gdx.app.error(CLASS_NAME, "unknow broadcast adress");
		} catch (SocketException sex) {
			Gdx.app.error(CLASS_NAME, "SocketException creating broadcast socket : " + sex.getMessage());
			throw new RuntimeException(sex);

		} catch (IOException e) {
			Gdx.app.error(CLASS_NAME, "IOException : " + e.getMessage());
		}
	}

	public void refreshInternetServerList() {
		internetServer = null;
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl("http://" + Constante.NETWORK_DISCOVERY_SERVER_INTERNET + ":"
				+ Constante.NETWORK_DISCOVERY_SERVER_INTERNET_PORT + "/api/servers");

		Gdx.app.log(CLASS_NAME, "debut");
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				ServerList list = null;
				try {
					list = mapper.readValue(httpResponse.getResultAsString(), ServerList.class);
				} catch (IOException e) {
					Gdx.app.error(CLASS_NAME, "IOException");
				}
				Gdx.app.log(CLASS_NAME, "callback");
				list.getServers().stream().forEach(s -> Gdx.app.log(CLASS_NAME, s.toString()));
				if (localNetworkServer == null) {
					localNetworkServer = new ArrayList<>();
				}
				internetServer = list.getServers();
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error(CLASS_NAME, "error on hearthbeat - stop hearthbeat");
			}

			@Override
			public void cancelled() {
				Gdx.app.log(CLASS_NAME, "cancelled");
			}
		});
		Gdx.app.log(CLASS_NAME, "fin");
	}
}