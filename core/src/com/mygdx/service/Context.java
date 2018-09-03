package com.mygdx.service;

import com.mygdx.enumeration.LocaleEnum;

public class Context {

	private static LocaleEnum locale = LocaleEnum.FRENCH;
	
	private Context() {
	}

	public static LocaleEnum getLocale() {
		return locale;
	}

	public static void setLocale(LocaleEnum locale) {
		Context.locale = locale;
	}
	
	public static void resetContext() {
		locale = LocaleEnum.FRENCH;
	}
}
