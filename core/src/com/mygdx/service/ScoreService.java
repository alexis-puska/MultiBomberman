package com.mygdx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mygdx.domain.Player;
import com.mygdx.domain.common.Drawable;
import com.mygdx.main.MultiBombermanGame;

public class ScoreService extends Drawable {

	private final MultiBombermanGame mbGame;
	private Map<Player, Integer> score;

	public ScoreService(MultiBombermanGame mbGame, List<Player> players) {
		this.mbGame = mbGame;
		this.score = new HashMap<>();
		players.stream().forEach(p -> {
			this.score.put(p, 0);
		});
	}

	public void win(Player player) {
		this.score.put(player, this.score.get(player) + 1);
	}

	public Map<Player, Integer> getScore() {
		return this.score;
	}

	@Override
	public void drawIt() {

	}
}
