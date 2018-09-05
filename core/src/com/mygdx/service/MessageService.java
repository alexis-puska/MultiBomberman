package com.mygdx.service;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.mygdx.enumeration.LocaleEnum;

/**
 * @author apuskarczyk return specific translation for a code.
 */
public class MessageService {

	private static MessageService instance;

	private FileHandle baseFileHandle;
	private LocaleEnum currentLocale;
	private I18NBundle message;

	/**
	 * Init the service
	 */
	private MessageService() {
		Gdx.app.log("MessageService", "Init");
		baseFileHandle = Gdx.files.internal("i18n/message");
		currentLocale = Context.locale;
		message = I18NBundle.createBundle(baseFileHandle, new Locale(currentLocale.getCode()));
	}

	/**
	 * Update Locale if the context change
	 */
	private void checkLocaleChange() {
		if (Context.locale != currentLocale) {
			currentLocale = Context.locale;
			message = I18NBundle.createBundle(baseFileHandle, new Locale(currentLocale.getCode()));
		}
	}

	public static MessageService getInstance() {
		if (instance == null) {
			instance = new MessageService();
		}
		return instance;
	}

	/**
	 * @param key
	 *            searched translation key
	 * @return the translation
	 */
	public String getMessage(String key) {
		checkLocaleChange();
		return message.get(key);
	}

	/**
	 * @param key
	 *            searched translation key
	 * @param args
	 *            parameters
	 * @return the translation
	 */
	public String getMessage(String key, Object... args) {
		checkLocaleChange();
		return message.format(key, args);
	}
}
