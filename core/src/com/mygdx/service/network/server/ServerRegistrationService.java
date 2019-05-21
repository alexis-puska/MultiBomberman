package com.mygdx.service.network.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.server.ServerRegistration;
import com.mygdx.service.Context;

public class ServerRegistrationService {

	private static final String CANCELLED = "cancelled";
	private static final String RESPONSE = "response: ";
	private static final String HTTP = "http://";
	private static final String CLASS_NAME = "ServerRegistrationService.class";
	private HeartBeatService hearthbeatService;
	private boolean registered;

	public ServerRegistrationService() {
		this.hearthbeatService = new HeartBeatService();
		this.registered = false;
	}

	public void create() {
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl(HTTP + Constante.NETWORK_DISCOVERY_SERVER_INTERNET + ":"
				+ Constante.NETWORK_DISCOVERY_SERVER_INTERNET_PORT + "/api/servers");
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log(CLASS_NAME, RESPONSE + httpResponse.getResultAsString());
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error(CLASS_NAME, "something went wrong", t);
			}

			@Override
			public void cancelled() {
				Gdx.app.log(CLASS_NAME, CANCELLED);
			}
		});
	}

	public void register(String internetIp, String externalIp) {
		HttpRequest request = new HttpRequest(HttpMethods.POST);
		request.setUrl(HTTP + Constante.NETWORK_DISCOVERY_SERVER_INTERNET + ":"
				+ Constante.NETWORK_DISCOVERY_SERVER_INTERNET_PORT + "/api/register");
		ServerRegistration registration = new ServerRegistration();
		registration.setCurrentNetPlayer(0);
		registration.setMaxNetPlayer(0);
		registration.setMaxPlayer(0);
		registration.setWanIp(internetIp);
		registration.setLanIp(externalIp);
		registration.setPort(Context.getPort());
		registration.setUuid(Context.getUuid());
		try {
			request.setContent(new ObjectMapper().writeValueAsString(registration));
			request.setHeader("content-type", "application/json");
		} catch (JsonProcessingException e) {
			Gdx.app.log("", "JsonProcessingException", e);
		}
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				Gdx.app.log(CLASS_NAME, RESPONSE + httpResponse.getResultAsString());
				hearthbeatService.start();
				registered = true;
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error(CLASS_NAME, "error when register server");
			}

			@Override
			public void cancelled() {
				Gdx.app.log(CLASS_NAME, CANCELLED);
			}
		});
	}

	public void unregister() {
		hearthbeatService.stop();
		if (registered) {
			HttpRequest request = new HttpRequest(HttpMethods.GET);
			request.setUrl(HTTP + Constante.NETWORK_DISCOVERY_SERVER_INTERNET + ":"
					+ Constante.NETWORK_DISCOVERY_SERVER_INTERNET_PORT + "/api/unregister/" + Context.getUuid());
			request.setHeader("content-type", "application/json");
			Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
				@Override
				public void handleHttpResponse(HttpResponse httpResponse) {
					Gdx.app.log(CLASS_NAME, RESPONSE + httpResponse.getResultAsString());
				}

				@Override
				public void failed(Throwable t) {
					Gdx.app.error(CLASS_NAME, "error at unregister server");
				}

				@Override
				public void cancelled() {
					Gdx.app.log(CLASS_NAME, CANCELLED);
				}
			});
		}
	}

}
