package com.mygdx.game.editor.domain.level;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.editor.domain.level.event.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Level extends Identifiable {

	private int next;
	private boolean showPlatform;
	private int background;
	private int verticalPlateform;
	private int horizontalPlateform;
	private List<LevelName> name;
	private List<Decor> decor;
	private List<Event> event;
	private List<Door> door;
	private List<Lock> lock;
	private List<Pick> pick;
	private List<Platform> platform;
	private List<Rayon> rayon;
	private List<Teleporter> teleporter;
	private List<Vortex> vortex;
	private List<Ennemie> ennemies;
	private Position startPlayers;
	private Position startEffectObjets;
	private Position startPointObjets;
	private List<Item> items;

	public Level(int id, boolean showPlatform, int background, int verticalPlateform, int horizontalPlateform) {
		super(id);
		this.showPlatform = showPlatform;
		this.background = background;
		this.verticalPlateform = verticalPlateform;
		this.horizontalPlateform = horizontalPlateform;
		this.next = 0;
		this.decor = new ArrayList<>();
		this.event = new ArrayList<>();
		this.door = new ArrayList<>();
		this.lock = new ArrayList<>();
		this.pick = new ArrayList<>();
		this.platform = new ArrayList<>();
		this.rayon = new ArrayList<>();
		this.teleporter = new ArrayList<>();
		this.vortex = new ArrayList<>();
		this.ennemies = new ArrayList<>();
		this.startPlayers = null;
		this.startEffectObjets = null;
		this.startPointObjets = null;
		this.items = new ArrayList<>();
	}

	public Level(int id) {
		super(id);
		this.showPlatform = true;
		this.background = 1;
		this.verticalPlateform = 0;
		this.horizontalPlateform = 0;
		this.next = 0;
		this.decor = new ArrayList<>();
		this.event = new ArrayList<>();
		this.door = new ArrayList<>();
		this.lock = new ArrayList<>();
		this.pick = new ArrayList<>();
		this.platform = new ArrayList<>();
		this.rayon = new ArrayList<>();
		this.teleporter = new ArrayList<>();
		this.vortex = new ArrayList<>();
		this.ennemies = new ArrayList<>();
		this.startPlayers = null;
		this.startEffectObjets = null;
		this.startPointObjets = null;
		this.items = new ArrayList<>();
	}

}
