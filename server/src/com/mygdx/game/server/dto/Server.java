package com.mygdx.game.server.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Server implements Serializable {

	private static final long serialVersionUID = -6520726897648121843L;

	private Lookup lookup;
	@JsonIgnore
	private LocalDateTime lastUpdate;
	@JsonIgnore
	private String uuid;
	private String wanIp;
	private int port;
	private int maxNetPlayer;
	private int currentNetPlayer;
	private int maxPlayer;

	public Lookup getLookup() {
		return lookup;
	}

	public void setLookup(Lookup lookup) {
		this.lookup = lookup;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWanIp() {
		return wanIp;
	}

	public void setWanIp(String wanIp) {
		this.wanIp = wanIp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxNetPlayer() {
		return maxNetPlayer;
	}

	public void setMaxNetPlayer(int maxNetPlayer) {
		this.maxNetPlayer = maxNetPlayer;
	}

	public int getCurrentNetPlayer() {
		return currentNetPlayer;
	}

	public void setCurrentNetPlayer(int currentNetPlayer) {
		this.currentNetPlayer = currentNetPlayer;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

}
