package com.mygdx.dto.server;

import java.io.Serializable;

import com.mygdx.enumeration.HeartBeatStatusEnum;

public class HeartBeatResponse implements Serializable {

	private static final long serialVersionUID = 4014123445915673220L;

	private HeartBeatStatusEnum status;

	public HeartBeatResponse() {
		super();
	}

	public HeartBeatResponse(HeartBeatStatusEnum status) {
		super();
		this.status = status;
	}

	public HeartBeatStatusEnum getStatus() {
		return status;
	}

	public void setStatus(HeartBeatStatusEnum status) {
		this.status = status;
	}

}
