package com.mygdx.service;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.domain.enumeration.SoundCommandEnum;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.enumeration.SoundEnum;
import com.mygdx.service.network.enumeration.NetworkRequestEnum;
import com.mygdx.service.network.server.Server;

/**
 * Play musique or a sound in game.
 * 
 * @author alexispuskarczyk
 */
public class SoundService {

	private static final String CLASS_NAME = "SoundService.class";
	private static final float MUSIQUE_VOLUME = 0.2f;
	private static final float MINE_VOLUME = 0.3f;
	private static final float VALIDE_BIP_VOLUME = 0.1f;
	private static final float CANCEL_VOLUME = 0.1f;

	private static SoundService instance = new SoundService();
	private Map<Long, Long> networkLoopSoundId;

	/*******************
	 * --- musique ---
	 *******************/
	private Music musicBattle;
	private Music musicMenu;

	/*******************
	 * --- son ---
	 *******************/
	private Sound soundBip;
	private Sound soundBouncd;
	private Sound soundBurn;
	private Sound soundCancel;
	private Sound soundEnd;
	private Sound soundFire;
	private Sound soundHole1;
	private Sound soundHole2;
	private Sound soundHole3;
	private Sound soundKick;
	private Sound soundLouis;
	private Sound soundMine;
	private Sound soundTeleporterClose;
	private Sound soundTeleporterOpen;
	private Sound soundTrolley;
	private Sound soundValide;
	private Sound soundAzizLightFr;
	private Sound soundAzizLightEn;
	private Sound soundDrawGame;

	/*********************
	 * --- NETWORK ---
	 *********************/
	private Server server;

	public SoundService() {
		Gdx.app.debug(CLASS_NAME, "Init");
		/*******************
		 * --- musique ---
		 *******************/
		musicBattle = Gdx.audio.newMusic(Gdx.files.internal("music/music_battle.mp3"));
		musicMenu = Gdx.audio.newMusic(Gdx.files.internal("music/music_menu.mp3"));
		/*******************
		 * --- son ---
		 *******************/
		soundBip = Gdx.audio.newSound(Gdx.files.internal("sound/sound_bip.wav"));
		soundBouncd = Gdx.audio.newSound(Gdx.files.internal("sound/sound_bounce.wav"));
		soundBurn = Gdx.audio.newSound(Gdx.files.internal("sound/sound_burn.wav"));
		soundCancel = Gdx.audio.newSound(Gdx.files.internal("sound/sound_cancel.wav"));
		soundEnd = Gdx.audio.newSound(Gdx.files.internal("sound/sound_end.wav"));
		soundFire = Gdx.audio.newSound(Gdx.files.internal("sound/sound_fire.wav"));
		soundHole1 = Gdx.audio.newSound(Gdx.files.internal("sound/sound_hole1.wav"));
		soundHole2 = Gdx.audio.newSound(Gdx.files.internal("sound/sound_hole2.wav"));
		soundHole3 = Gdx.audio.newSound(Gdx.files.internal("sound/sound_hole3.wav"));
		soundKick = Gdx.audio.newSound(Gdx.files.internal("sound/sound_kick.wav"));
		soundLouis = Gdx.audio.newSound(Gdx.files.internal("sound/sound_louis.wav"));
		soundMine = Gdx.audio.newSound(Gdx.files.internal("sound/sound_mine.wav"));
		soundTeleporterClose = Gdx.audio.newSound(Gdx.files.internal("sound/sound_teleporter_close.wav"));
		soundTeleporterOpen = Gdx.audio.newSound(Gdx.files.internal("sound/sound_teleporter_open.wav"));
		soundTrolley = Gdx.audio.newSound(Gdx.files.internal("sound/sound_trolley.wav"));
		soundValide = Gdx.audio.newSound(Gdx.files.internal("sound/sound_valide.wav"));
		soundAzizLightFr = Gdx.audio.newSound(Gdx.files.internal("sound/sound_aziz_light_fr.wav"));
		soundAzizLightEn = Gdx.audio.newSound(Gdx.files.internal("sound/sound_aziz_light_en.wav"));
		soundDrawGame = Gdx.audio.newSound(Gdx.files.internal("sound/sound_draw.wav"));
		networkLoopSoundId = new HashMap<>();
	}

	public static SoundService getInstance() {
		return instance;
	}

	/*******************
	 * --- NETWORK ---
	 ******************/
	public void setServer(Server server) {
		this.server = server;
	}

	public void clearServer() {
		this.server = null;
	}

	/*******************
	 * --- musique ---
	 *******************/
	public void stopMusic() {
		if (musicBattle.isPlaying()) {
			musicBattle.stop();
		} else if (musicMenu.isPlaying()) {
			musicMenu.stop();
		}
		if (this.server != null) {
			createStopMusiqueCommand();
		}
	}

	public void playMusic(MusicEnum musicEnum) {
		stopMusic();
		switch (musicEnum) {
		case BATTLE:
			musicBattle.setVolume(MUSIQUE_VOLUME);
			musicBattle.play();
			musicBattle.setLooping(true);
			break;
		case MENU:
		default:
			musicMenu.setVolume(MUSIQUE_VOLUME);
			musicMenu.play();
			musicMenu.setLooping(true);
			break;
		}
		if (this.server != null) {
			createPlayMusiqueCommand(musicEnum);
		}
	}

	/*******************
	 * --- son ---
	 *******************/
	public void playSound(SoundEnum soundEnum) {
		switch (soundEnum) {
		case BOUNCE:
			soundBouncd.play();
			break;
		case BURN:
			soundBurn.play();
			break;
		case CANCEL:
			soundCancel.play(CANCEL_VOLUME);
			break;
		case END:
			soundEnd.play();
			break;
		case FIRE:
			soundFire.play();
			break;
		case HOLE1:
			soundHole1.play();
			break;
		case HOLE2:
			soundHole2.play();
			break;
		case HOLE3:
			soundHole3.play();
			break;
		case KICK:
			soundKick.play();
			break;
		case LOUIS:
			soundLouis.play();
			break;
		case MINE:
			soundMine.play();
			break;
		case TELEPORTER_CLOSE:
			soundTeleporterClose.play();
			break;
		case TELEPORTER_OPEN:
			soundTeleporterOpen.play();
			break;
		case TROLLEY:
			soundTrolley.play();
			break;
		case VALIDE:
			soundValide.play(VALIDE_BIP_VOLUME);
			break;
		case AZIZ_LIGHT_EN:
			soundAzizLightEn.play();
			break;
		case AZIZ_LIGHT_FR:
			soundAzizLightFr.play();
			break;
		case DRAW:
			soundDrawGame.play();
			break;
		case BIP:
		default:
			soundBip.play(VALIDE_BIP_VOLUME);
		}
		if (this.server != null) {
			createPlaySoundCommand(soundEnum);
		}
	}

	public long loopSound(SoundEnum soundEnum) {
		long id = 0;
		switch (soundEnum) {
		case BOUNCE:
			id = soundBouncd.loop();
			break;
		case BURN:
			id = soundBurn.loop();
			break;
		case CANCEL:
			id = soundCancel.loop();
			break;
		case END:
			id = soundEnd.loop();
			break;
		case FIRE:
			id = soundFire.loop();
			break;
		case HOLE1:
			id = soundHole1.loop();
			break;
		case HOLE2:
			id = soundHole2.loop();
			break;
		case HOLE3:
			id = soundHole3.loop();
			break;
		case KICK:
			id = soundKick.loop();
			break;
		case LOUIS:
			id = soundLouis.loop();
			break;
		case MINE:
			id = soundMine.loop(MINE_VOLUME);
			break;
		case TELEPORTER_CLOSE:
			id = soundTeleporterClose.loop();
			break;
		case TELEPORTER_OPEN:
			id = soundTeleporterOpen.loop();
			break;
		case TROLLEY:
			id = soundTrolley.loop();
			break;
		case VALIDE:
			id = soundValide.loop();
			break;
		case AZIZ_LIGHT_EN:
			id = soundAzizLightEn.loop();
			break;
		case AZIZ_LIGHT_FR:
			id = soundAzizLightFr.loop();
			break;
		case DRAW:
			id = soundDrawGame.loop();
			break;
		case BIP:
		default:
			id = soundBip.loop();
		}
		if (this.server != null) {
			createLoopSoundCommand(soundEnum, id);
		}
		return id;
	}

	public void stopSound(SoundEnum soundEnum, long id) {
		switch (soundEnum) {
		case BOUNCE:
			soundBouncd.stop(id);
			break;
		case BURN:
			soundBurn.stop(id);
			break;
		case CANCEL:
			soundCancel.stop(id);
			break;
		case END:
			soundEnd.stop(id);
			break;
		case FIRE:
			soundFire.stop(id);
			break;
		case HOLE1:
			soundHole1.stop(id);
			break;
		case HOLE2:
			soundHole2.stop(id);
			break;
		case HOLE3:
			soundHole3.stop(id);
			break;
		case KICK:
			soundKick.stop(id);
			break;
		case LOUIS:
			soundLouis.stop(id);
			break;
		case MINE:
			soundMine.stop(id);
			break;
		case TELEPORTER_CLOSE:
			soundTeleporterClose.stop(id);
			break;
		case TELEPORTER_OPEN:
			soundTeleporterOpen.stop(id);
			break;
		case TROLLEY:
			soundTrolley.stop(id);
			break;
		case VALIDE:
			soundValide.stop(id);
			break;
		case AZIZ_LIGHT_EN:
			soundAzizLightEn.stop(id);
			break;
		case AZIZ_LIGHT_FR:
			soundAzizLightFr.stop(id);
			break;
		case DRAW:
			soundDrawGame.stop(id);
			break;
		case BIP:
		default:
			soundBip.stop(id);
		}
		if (this.server != null) {
			createStopSoundCommand(soundEnum, id);
		}
	}

	private void createPlayMusiqueCommand(MusicEnum music) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.put((byte) SoundCommandEnum.PLAY_MUSIC.ordinal());
		b.put((byte) music.ordinal());
		concatAndSend(b);
	}

	private void createStopMusiqueCommand() {
		ByteBuffer b = ByteBuffer.allocate(1);
		b.put((byte) SoundCommandEnum.STOP_MUSIC.ordinal());
		concatAndSend(b);
	}

	private void createPlaySoundCommand(SoundEnum sound) {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.put((byte) SoundCommandEnum.PLAY_SOUND.ordinal());
		b.put((byte) sound.ordinal());
		concatAndSend(b);
	}

	private void createLoopSoundCommand(SoundEnum sound, long id) {
		ByteBuffer b = ByteBuffer.allocate(10);
		b.put((byte) SoundCommandEnum.LOOP_SOUND.ordinal());
		b.put((byte) sound.ordinal());
		b.putLong(id);
		concatAndSend(b);
	}

	private void createStopSoundCommand(SoundEnum sound, long id) {
		ByteBuffer b = ByteBuffer.allocate(10);
		b.put((byte) SoundCommandEnum.STOP_SOUND.ordinal());
		b.put((byte) sound.ordinal());
		b.putLong(id);
		concatAndSend(b);
	}

	private void concatAndSend(ByteBuffer buffer) {
		String cmd = NetworkRequestEnum.SOUND.name() + ":" + Base64.getEncoder().encodeToString(buffer.array()) + "\n";
		this.server.send(cmd.getBytes());
	}

	public void decodeSoundCommand(String line) {
		String command = line.substring(line.indexOf(':') + 1);
		ByteBuffer b = ByteBuffer.wrap(Base64.getDecoder().decode(command.getBytes()));
		byte com = b.get();
		byte sound;
		byte music;
		long id;
		long localId;
		if ((int) com < SoundCommandEnum.values().length) {
			switch (SoundCommandEnum.values()[com]) {
			case LOOP_SOUND:
				sound = b.get();
				id = b.getLong();
				if (SoundEnum.values().length > (int) sound) {
					localId = this.loopSound(SoundEnum.values()[sound]);
					this.networkLoopSoundId.put(id, localId);
				}
				break;
			case PLAY_MUSIC:
				music = b.get();
				if (MusicEnum.values().length > (int) music) {
					this.playMusic(MusicEnum.values()[music]);
				}
				break;
			case PLAY_SOUND:
				sound = b.get();
				if (SoundEnum.values().length > (int) sound) {
					this.playSound(SoundEnum.values()[sound]);
				}
				break;
			case STOP_MUSIC:
				this.stopMusic();
				break;
			case STOP_SOUND:
				sound = b.get();
				id = b.getLong();
				if (SoundEnum.values().length > (int) sound && networkLoopSoundId.containsKey(id)) {
					localId = this.networkLoopSoundId.get(id);
					this.stopSound(SoundEnum.values()[sound], localId);
					this.networkLoopSoundId.remove(id);
					Gdx.app.log("SOUND_SERVICE", "network sound size : " + this.networkLoopSoundId.size());
				}
				break;
			default:
				break;

			}
		}
	}
}
