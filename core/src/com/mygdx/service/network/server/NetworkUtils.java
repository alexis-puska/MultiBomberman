package com.mygdx.service.network.server;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.badlogic.gdx.Gdx;

public class NetworkUtils {
	private static final String CLASS_NAME = "NetworkUtils.class";
	static final String IP_V4_PATTERN = "\\d+(\\.\\d+){3}";

	private NetworkUtils() {
		throw new IllegalStateException("NetworkUtils class");
	}

	/**
	 * Return this server's InetAddress
	 * 
	 * @return InetAddress of this server or null address cannot be obtained.
	 */
	public static InetAddress getMyAddress() {
		InetAddress inetAddr = null;
		List<InetAddress> addrs = getAllAddresses();
		// try to choose a non-local IPv4 address
		for (InetAddress addr : addrs) {
			if (addr.isLoopbackAddress() || addr.isLinkLocalAddress())
				continue;
			if (addr.getHostAddress().matches(IP_V4_PATTERN))
				return addr;
		}
		// didn't find a match. Try LocalHost address.
		try {
			inetAddr = InetAddress.getLocalHost();
		} catch (Exception e) {
			Gdx.app.log(CLASS_NAME,"NetworkUtil.getMyAddress: " + e.getMessage());
		}
		return inetAddr;
	}

	/**
	 * Return all active addresses of this server, except loopback address.
	 */
	public static List<InetAddress> getAllAddresses() {
		List<InetAddress> addrlist = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ipaddr = addresses.nextElement();
					addrlist.add(ipaddr);
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return addrlist;
	}
}