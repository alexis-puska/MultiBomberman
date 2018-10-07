package com.mygdx.game.editor.domain.level.event;

import java.io.Serializable;

import com.mygdx.game.editor.constant.EnabledElementEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EnableElement implements Serializable {
	private static final long serialVersionUID = 7239656902716537398L;
	private int id;
	private EnabledElementEnum elementType;
	private boolean newState;

	public EnableElement() {
		this.id = -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnableElement other = (EnableElement) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
