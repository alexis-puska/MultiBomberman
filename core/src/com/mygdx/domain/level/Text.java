package com.mygdx.domain.level;

import com.mygdx.enumeration.LocaleEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Text {
	private LocaleEnum lang;
	private String value;

}
