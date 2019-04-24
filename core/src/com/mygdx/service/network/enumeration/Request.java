package com.mygdx.service.network.enumeration;

public enum Request {

	CHANGE_SCREEN((byte) 0);

	private byte command;

	private Request(byte command) {
		this.command = command;
	}

	public byte getCommand() {
		return this.command;
	}
}
