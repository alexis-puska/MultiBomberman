package com.mygdx.game.server.dto;

import java.io.Serializable;
import java.util.List;

public class ServerList implements Serializable {

	private static final long serialVersionUID = -6486535246863304843L;

	private List<Server> servers;

	public List<Server> getServers() {
		return servers;
	}

	public void setServers(List<Server> servers) {
		this.servers = servers;
	}

}
