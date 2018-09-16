package com.mygdx.service.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.net.Socket;
import com.mygdx.enumeration.NetworkControllerEventEnum;
import com.mygdx.game.MultiBombermanGame;
import com.mygdx.service.PlayerService;

public class NetworkConnexion extends Thread {

	private static final String CLASS_NAME = "NetworkConnexion.class";
	
	private final Socket socket;
	private final Server server;
	private PlayerService playerService;
	private final String remoteAddress;

	// active
	private boolean status;
	private BufferedReader buffer;
	private OutputStream out;

	// guid of client
	private String guid;

	private int player;

	public NetworkConnexion(Socket socket, final Server server, final MultiBombermanGame game) {
		status = true;
		this.socket = socket;
		this.server = server;

		// TODO to remove after network protocole up !
		this.player = 1;
		this.status = true;

		this.playerService = game.getPlayerService();
		this.remoteAddress = socket.getRemoteAddress();
		Gdx.app.log("NetworkConnexion", String.format("new client connexion : %s", remoteAddress));
	}

	public void close() {
		Gdx.app.log("NetworkConnexion", String.format("fermeture connexion de : %s", remoteAddress));
		socket.dispose();
	}

	@Override
	public void run() {
		try {
			this.buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.out = socket.getOutputStream();

			// init conversation with client
			out.write("hello\r\n".getBytes());
			while (status) {
				String received = buffer.readLine();
				if (received == null) {
					Gdx.app.log("NetworkConnexion", String.format("Deconnection brutale de : %s", remoteAddress));
					status = false;
					break;
				}
				if (received.startsWith("uuid:")) {
					try {
						UUID uuid = UUID.fromString(received.substring(5, received.length()));
						this.guid = uuid.toString();
						Gdx.app.log("networkConnexion", this.guid);
						// do something
						out.write("NBP\n".getBytes());
					} catch (IllegalArgumentException exception) {
						Gdx.app.log("eee", "uuid check error");
					}
					Gdx.app.log("eee", "uuid check OK");

				}
				if (received.startsWith("nbp:")) {
					String[] part = received.split(":");
					player = Integer.parseInt(part[1]);
				}
				if (received.startsWith("event:")) {
					String[] part = received.split(":");
					int controllerIndex = Integer.parseInt(part[1]);
					String button = part[2];
					switch (NetworkControllerEventEnum.valueOf(button)) {
					case DOWN:
						playerService.move(this.guid, controllerIndex, PovDirection.south);
						break;
					case DROP:
						playerService.dropBombe(this.guid, controllerIndex);
						break;
					case LEFT:
						playerService.move(this.guid, controllerIndex, PovDirection.west);
						break;
					case RIGHT:
						playerService.move(this.guid, controllerIndex, PovDirection.east);
						break;
					case SELECT:
						break;
					case SPEED_DOWN:
						playerService.speedDown(this.guid, controllerIndex);
						break;
					case SPEED_UP:
						playerService.speedUp(this.guid, controllerIndex);
						break;
					case START:
						break;
					case THROW:
						playerService.throwBombe(this.guid, controllerIndex);
						break;
					case UP:
						playerService.move(this.guid, controllerIndex, PovDirection.north);
						break;
					default:
						playerService.move(this.guid, controllerIndex, PovDirection.center);

						break;
					}
				}
				if (received.equals("end")) {
					Gdx.app.log("NetworkConnexion", String.format("Deconnection de : %s", remoteAddress));
					socket.dispose();
					status = false;
					break;
				}
				decode(received);
			}
		} catch (IOException ez) {
			Gdx.app.log(CLASS_NAME, ez.getMessage());
		} catch (Exception e) {
			Gdx.app.log(CLASS_NAME, e.getMessage());
		}
	}

	private void decode(String received) {
		Gdx.app.log("NetworkConnexion", String.format("recu de %s : %s", remoteAddress, received));
		
	}

	private boolean decodeFirstStep(String receive) {
		return false;
	}

	public int getPlayer() {
		return player;
	}

	public String getGuid() {
		return guid;
	}
}
