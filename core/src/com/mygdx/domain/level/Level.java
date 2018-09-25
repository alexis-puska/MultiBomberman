package com.mygdx.domain.level;

import java.util.List;

import com.mygdx.domain.common.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Level extends Identifiable {

	protected List<Text> name;
	protected List<Variante> variante;
}
