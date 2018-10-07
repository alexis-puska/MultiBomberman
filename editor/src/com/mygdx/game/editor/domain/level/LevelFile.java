package com.mygdx.game.editor.domain.level;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevelFile implements Serializable {

	private static final long serialVersionUID = -5410889236338101940L;

	private List<Type> type;
}
