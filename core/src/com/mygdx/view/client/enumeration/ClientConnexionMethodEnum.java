package com.mygdx.view.client.enumeration;

public enum ClientConnexionMethodEnum {
	IP, 
	LOCAL, 
	INTERNET;

	public static ClientConnexionMethodEnum next(ClientConnexionMethodEnum val) {
		return values()[(val.ordinal() + 1) % values().length];
	}

	public static ClientConnexionMethodEnum previous(ClientConnexionMethodEnum val) {
		if ((val.ordinal() - 1) < 0) {
			return values()[values().length - 1];
		}
		return values()[(val.ordinal() - 1)];
	}
}
