package com.mygdx.service.network.server;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mygdx.constante.Constante;
import com.mygdx.dto.server.HeartBeatResponse;
import com.mygdx.enumeration.HeartBeatStatusEnum;
import com.mygdx.service.Context;

public class HeartBeat extends Thread {

	private static final String LOG_NAME = "HeartBeat.class";

	private boolean status;

	private ObjectMapper mapper;

	public HeartBeat() {
		mapper = new ObjectMapper();
		this.status = true;
	}

	@Override
	public void run() {
		while (status) {
			hearthbeat();
			try {
				Thread.sleep(Constante.NETWORK_REGISTRATION_HEARTHBEAT_TIME);
			} catch (InterruptedException e) {
				Gdx.app.error(LOG_NAME, "HeartBeat InterruptedException : " + e.getMessage());
				Thread.currentThread().interrupt();
			}
		}
	}

	public void kill() {
		status = false;
	}

	public boolean isStarted() {
		return status;
	}

	public void hearthbeat() {
		HttpRequest request = new HttpRequest(HttpMethods.GET);
		request.setUrl("http://localhost:8080/api/hearthbeat/" + Context.getUuid());
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				try {
					HeartBeatResponse response = mapper.readValue(httpResponse.getResultAsString(),
							HeartBeatResponse.class);
					if (response.getStatus() == HeartBeatStatusEnum.KO) {
						Gdx.app.error(LOG_NAME, "STATUS NOT UPDATED");
					}
				} catch (IOException e) {
					Gdx.app.error(LOG_NAME, "IOException");
				}
			}

			@Override
			public void failed(Throwable t) {
				Gdx.app.error(LOG_NAME, "error on hearthbeat - stop hearthbeat");
				status = false;
			}

			@Override
			public void cancelled() {
				Gdx.app.log(LOG_NAME, "cancelled");
			}
		});
	}

}
