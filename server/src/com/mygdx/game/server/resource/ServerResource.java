package com.mygdx.game.server.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mygdx.dto.server.ServerRegistration;
import com.mygdx.game.server.dto.ServerList;
import com.mygdx.game.server.service.ServerService;

@RestController
public class ServerResource {

	private static final Logger LOG = LogManager.getLogger(ServerResource.class);

	private final ServerService serverService;

	public ServerResource(ServerService serverService) {
		this.serverService = serverService;
	}

	@GetMapping("/api/servers")
	public @ResponseBody ServerList getServerList() {
		return serverService.getServerList();
	}

	@PostMapping("/api/register")
	public void registerServer(@RequestBody ServerRegistration serverRegistration) {
		LOG.info("Register server from ip : " + serverRegistration.getWanIp());
		serverService.registerServer(serverRegistration);
	}

	@GetMapping("/api/unregister/{uuid}")
	public void unregisterServer(@PathVariable("uuid") String uuid) {
		LOG.info("Unregister server from uuid : " + uuid);
		serverService.unregisterServer(uuid);
	}

	@GetMapping("/api/hearthbeat/{uuid}")
	public void hearthbeat(@PathVariable("uuid") String uuid) {
		LOG.info("hearthbeat from uuid : " + uuid);
		serverService.hearthBeatServer(uuid);
	}
}
