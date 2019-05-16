package com.mygdx.service.network.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class DiscoveryServerInfo {
	private String ip;
	private int port;
}
