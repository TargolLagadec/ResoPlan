package org.targol.resoplan.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "i18n.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(final String key, final Object... arguments) {
		final String pattern = getString(key);
		return MessageFormat.format(pattern, arguments);
	}

}
