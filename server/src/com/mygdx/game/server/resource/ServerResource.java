package com.mygdx.game.server.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mygdx.dto.server.HeartBeatResponse;
import com.mygdx.dto.server.ServerList;
import com.mygdx.dto.server.ServerRegistration;
import com.mygdx.game.server.service.ServerService;

@RestController
public class ServerResource {

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
		serverService.registerServer(serverRegistration);
	}

	@GetMapping("/api/unregister/{uuid}")
	public void unregisterServer(@PathVariable("uuid") String uuid) {
		serverService.unregisterServer(uuid);
	}

	@GetMapping("/api/hearthbeat/{uuid}")
	public @ResponseBody HeartBeatResponse hearthbeat(@PathVariable("uuid") String uuid) {
		return serverService.hearthBeatServer(uuid);
	}
}
