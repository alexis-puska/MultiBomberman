package com.mygdx.dto.server;

import java.io.Serializable;

import com.mygdx.enumeration.ServerStateEnum;

public class ServerInformation implements Serializable {

	private static final long serialVersionUID = -4215336721617638077L;

	private ServerStateEnum state;
	private int player;

	public ServerStateEnum getState() {
		return state;
	}

	public void setState(ServerStateEnum state) {
		this.state = state;
	}

	public int getPlayer() {
		return player;
	}

	public void setPlayer(int player) {
		this.player = player;
	}

}
