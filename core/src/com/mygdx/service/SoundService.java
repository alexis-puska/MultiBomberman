package com.mygdx.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.enumeration.MusicEnum;
import com.mygdx.enumeration.SoundEnum;

/**
 * Play musique or a sound in game.
 * 
 * @author alexispuskarczyk
 */
public class SoundService {

	private static final String CLASS_NAME = "SoundService.class";

	private static SoundService instance = new SoundService();

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
	private Sound soundValide;
	private MusicEnum lastMusicPlayed;

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
		soundValide = Gdx.audio.newSound(Gdx.files.internal("sound/sound_valide.wav"));
	}

	public static SoundService getInstance() {
		return instance;
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
	}

	public void playMusic(MusicEnum musicEnum) {
		stopMusic();
		switch (musicEnum) {
		case BATTLE:
			musicBattle.play();
			musicBattle.setLooping(true);
			break;
		case MENU:
		default:
			musicMenu.play();
			musicMenu.setLooping(true);
			break;
		}
		lastMusicPlayed = musicEnum;
	}

	public void playLastMusic() {
		playMusic(lastMusicPlayed);
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
			soundCancel.play();
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
		case VALIDE:
			soundValide.play();
			break;
		case BIP:
		default:
			soundBip.play();
		}
	}
}
