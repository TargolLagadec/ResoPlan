package org.targol.resoplan.utils;

import java.util.prefs.Preferences;

import org.targol.resoplan.ui.utils.ThemesHelper.Theme;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

public class PreferencesHelper {

	private static final String PREF_THEME = "general.theme"; //$NON-NLS-1$
	public static final String PREF_SHOW_RULERS = "network.showRulers"; //$NON-NLS-1$
	public static final String PREF_SHOW_GRID = "network.showGrid"; //$NON-NLS-1$

	private static final Preferences prefs = Preferences.userNodeForPackage(PreferencesHelper.class);


	static  {
		final Theme theme = getCurrentTheme();
		if (theme == null) {
			setCurrentTheme(Theme.GREY);
		}
	}

	public static Theme getCurrentTheme() {
		final String theme = prefs.get(PREF_THEME, null);
		if (theme != null) {
			return Theme.valueOf(theme);
		}
		return Theme.GREY;
	}

	public static void setCurrentTheme(final Theme theme) {
		prefs.put(PREF_THEME, theme.toString());
		UiEventBus.send(ThemeChangeEvent.change(theme));
	}

	public static String getPreference(final String prefKey) {
		return prefs.get(prefKey, null);
	}

	public static void setPreference(final String prefKey, final String value) {
		prefs.put(prefKey, value);
	}

	public static int getIntPreference(final String prefKey) {
		return prefs.getInt(prefKey, 0);
	}

	public static void setIntPreference(final String prefKey, final int value) {
		prefs.putInt(prefKey, value);
	}

	public static boolean getBoolPreference(final String prefKey) {
		return prefs.getBoolean(prefKey, false);
	}

	public static void setBoolPreference(final String prefKey, final boolean value) {
		prefs.putBoolean(prefKey, value);
	}
}
