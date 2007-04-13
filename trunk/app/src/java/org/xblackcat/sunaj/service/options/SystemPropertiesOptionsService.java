package org.xblackcat.sunaj.service.options;

/**
 * Service for retrieving or storing options of the application.
 *
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class SystemPropertiesOptionsService extends AbstractOptionsService {

	private static IOptionsService instance = null;

	public static IOptionsService getInstance() {
		if (instance == null) {
			instance = new SystemPropertiesOptionsService();
		}
		return instance;
	}

	private SystemPropertiesOptionsService() {
	}

	protected String getProperty(String key) {
		return System.getProperty(key);
	}

	protected String setProperty(String key, String value) {
		return System.setProperty(key, value);
	}
}
