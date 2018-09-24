package com.mygdx.domain.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enableable extends Identifiable {
	protected boolean enable;
	
	public void enable() {
		this.enable = true;
	}

	public void disable() {
		this.enable = false;
	}
}
