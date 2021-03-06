package com.mygdx.game.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.mygdx.constante.Constante;
import com.mygdx.dto.server.HeartBeatResponse;
import com.mygdx.dto.server.Lookup;
import com.mygdx.dto.server.Server;
import com.mygdx.dto.server.ServerList;
import com.mygdx.dto.server.ServerRegistration;
import com.mygdx.enumeration.HeartBeatStatusEnum;

@Service
public class ServerService {

	private static final Logger LOG = LogManager.getLogger(ServerService.class);

	private final DatabaseReader dbReader;
	private Map<String, Server> servers;

	public ServerService() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream in = classloader.getResourceAsStream("maxmind/GeoLite2-City.mmdb");
		this.dbReader = new DatabaseReader.Builder(in).build();
		this.servers = new HashMap<>();
	}

	public ServerList getServerList() {
		ServerList serverList = new ServerList();
		serverList.setServers(new ArrayList<>(this.servers.values()));
		return serverList;
	}

	public void registerServer(ServerRegistration serverRegistration) {
		if (!this.servers.containsKey(serverRegistration.getUuid())) {
			Lookup lookup;
			try {
				lookup = retriveInfo(serverRegistration);
				Server server = new Server();
				server.setLookup(lookup);
				server.setLastUpdate(LocalDateTime.now());
				server.setCurrentNetPlayer(serverRegistration.getCurrentNetPlayer());
				server.setMaxNetPlayer(serverRegistration.getMaxNetPlayer());
				server.setPort(serverRegistration.getPort());
				server.setUuid(serverRegistration.getUuid());
				server.setWanIp(serverRegistration.getWanIp());
				server.setLanIp(serverRegistration.getLanIp());
				this.servers.put(serverRegistration.getUuid(), server);
				LOG.info("Registration of : {}", server);
			} catch (IOException e) {
				LOG.error("Lookup IOException : " + e.getMessage());
			} catch (GeoIp2Exception e) {
				LOG.error("Lookup GeoIp2Exception : " + e.getMessage());
			}
		}
	}

	public void unregisterServer(String uuid) {
		this.servers.remove(uuid);
		LOG.info("UnRegistration of server UUID : {}", uuid);
	}

	public HeartBeatResponse hearthBeatServer(String uuid) {
		if (servers.containsKey(uuid)) {
			Server s = servers.get(uuid);
			s.setLastUpdate(LocalDateTime.now());
			servers.put(uuid, s);
			return new HeartBeatResponse(HeartBeatStatusEnum.OK);
		}
		return new HeartBeatResponse(HeartBeatStatusEnum.KO);
	}

	@Scheduled(fixedRate = Constante.NETWORK_REGISTRATION_SCHEDULE_TIME)
	public void checkServerAvailability() {
		for (Entry<String, Server> s : servers.entrySet()) {
			if (s.getValue().getLastUpdate().plus(Constante.NETWORK_REGISTRATION_TIMEOUT, ChronoUnit.MILLIS)
					.isBefore(LocalDateTime.now())) {
				LOG.info("remove server with UUID : " + s.getValue().getUuid());
				servers.remove(s.getKey());
			}
		}
	}

	private Lookup retriveInfo(ServerRegistration server) throws IOException, GeoIp2Exception {
		Lookup lookup = new Lookup();
		InetAddress ipAddress = InetAddress.getByName(server.getWanIp());
		CityResponse response = dbReader.city(ipAddress);
		lookup.setCountryName(response.getCountry().getName());
		lookup.setCityName(response.getCity().getName());
		lookup.setPostal(response.getPostal().getCode());
		lookup.setState(response.getLeastSpecificSubdivision().getName());
		return lookup;
	}
}
