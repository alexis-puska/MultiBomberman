package com.mygdx.utils;

import java.util.Arrays;

public class NetworkBufferUtils {
	
	private static final int BUFFER_SIZE = 65535;

	private byte[] buffer = new byte[BUFFER_SIZE];
	private int size = 0;

	public void newGameFrame() {
		Arrays.fill(buffer, (byte) 0);
	}

	public int getSize() {
		return this.size;
	}

	public byte[] getBuffer() {
		return this.buffer;
	}

}
