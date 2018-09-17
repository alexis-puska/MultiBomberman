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
import com.mygdx.service.Context;
import com.mygdx.service.PlayerService;

public class NetworkConnexion extends Thread {

	private static final String CLASS_NAME = "NetworkConnexion.class";

	private final Socket socket;
	private final Server server;
	private PlayerService playerService;
	private final String remoteAddress;

	// active
	private boolean status;

	private OutputStream out;

	// uuid of client
	private String uuid;

	private int player;

	public NetworkConnexion(Socket socket, final Server server, final MultiBombermanGame game) {
		this.status = true;
		this.socket = socket;
		this.server = server;

		// TODO to remove after network protocole up !
		this.player = 1;
		

		this.playerService = game.getPlayerService();
		this.remoteAddress = socket.getRemoteAddress();
		Gdx.app.debug(CLASS_NAME, String.format("new client connexion : %s", remoteAddress));
	}

	public void close() {
		Gdx.app.debug(CLASS_NAME, String.format("fermeture connexion de : %s", remoteAddress));
		socket.dispose();
	}

	@Override
	public void run() {
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			this.out = socket.getOutputStream();

			// init conversation with client
			out.write("hello\r\n".getBytes());
			while (status) {
				String received = buffer.readLine();

				// Perte de connexion
				if (received == null) {
					Gdx.app.error(CLASS_NAME, String.format("Deconnection brutale de : %s", remoteAddress));
					if (server.isAcceptNewConnexion()) {
						server.removePlayerDeconnexionBeforeValide(player);
					}
					status = false;

				} else {

					// Reception UUID
					if (received.startsWith("uuid:")) {
						try {
							UUID uuidObject = UUID.fromString(received.substring(5, received.length()));
							this.uuid = uuidObject.toString();
							Gdx.app.debug(CLASS_NAME, this.uuid);

							// si accepte encore les connexion, demande nombre de joueur
							if (server.isAcceptNewConnexion()) {
								out.write("nbp\n".getBytes());
							} else {
								// verification si deja connecté avant
								if (!server.connexionAlreadyExistBefore(this)) {
									// si non on expédie l'erreur et on ferme la connexion
									out.write("error_in_game\n".getBytes());
									socket.dispose();
									status = false;
								} else {
									// si oui on écoute les evenements et autorise le client é les envoyé
									out.write("event\n".getBytes());
								}
							}
						} catch (IllegalArgumentException exception) {
							Gdx.app.error(CLASS_NAME, "uuid check error");
						}
						Gdx.app.debug(CLASS_NAME, "uuid check OK");
					}

					// Réception nombre de joueur
					if (received.startsWith("nbp:")) {
						String[] part = received.split(":");
						player = Integer.parseInt(part[1]);
						if (server.getPlayer() + player <= Context.getExternalPlayer()) {
							server.valideConnexion(this);
							out.write("event\n".getBytes());
						} else {
							out.write("error_to_many_player\n".getBytes());
							socket.dispose();
							status = false;
						}
					}

					// Réception event controller / keyboard
					if (received.startsWith("event:")) {
						String[] part = received.split(":");
						int controllerIndex = Integer.parseInt(part[1]);
						String button = part[2];
						switch (NetworkControllerEventEnum.valueOf(button)) {
						case DOWN:
							playerService.move(this.uuid, controllerIndex, PovDirection.south);
							break;
						case DROP:
							playerService.dropBombe(this.uuid, controllerIndex);
							break;
						case LEFT:
							playerService.move(this.uuid, controllerIndex, PovDirection.west);
							break;
						case RIGHT:
							playerService.move(this.uuid, controllerIndex, PovDirection.east);
							break;
						case SELECT:
							break;
						case SPEED_DOWN:
							playerService.speedDown(this.uuid, controllerIndex);
							break;
						case SPEED_UP:
							playerService.speedUp(this.uuid, controllerIndex);
							break;
						case START:
							break;
						case THROW:
							playerService.throwBombe(this.uuid, controllerIndex);
							break;
						case UP:
							playerService.move(this.uuid, controllerIndex, PovDirection.north);
							break;
						default:
							playerService.move(this.uuid, controllerIndex, PovDirection.center);
							break;
						}
					}

					// déconnexion
					if (received.equals("end")) {
						Gdx.app.debug(CLASS_NAME, String.format("Deconnection de : %s", remoteAddress));
						if (server.isAcceptNewConnexion()) {
							server.removePlayerDeconnexionBeforeValide(player);
						}
						socket.dispose();
						status = false;
					}
				}
				decode(received);
			}
		} catch (IOException ez) {
			Gdx.app.error(CLASS_NAME, "IOException : " + ez.getMessage());
		} catch (Exception e) {
			Gdx.app.error(CLASS_NAME, "Exception : " + e.getMessage());
		}
	}

	private void decode(String received) {
		Gdx.app.debug(CLASS_NAME, String.format("recu de %s : %s", remoteAddress, received));
	}

	public int getPlayer() {
		return player;
	}

	public String getUuid() {
		return uuid;
	}

	public void send(byte[] value) {
		try {
			out.write(value);
		} catch (IOException e) {
			Gdx.app.error("CLASS_NAME", "IOException : " + e.getMessage());
		}
	}
}
