package com.mygdx.dto.level.common;

import java.io.Serializable;

import com.mygdx.enumeration.LocaleEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TextDTO implements Serializable {

	private static final long serialVersionUID = 4557602529622087772L;

	private LocaleEnum lang;
	private String value;

	public TextDTO(TextDTO original) {
		this.lang = original.lang;
		this.value = original.value;
	}

	public LocaleEnum getLang() {
		return lang;
	}

	public void setLang(LocaleEnum lang) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
