package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.ui.utils.ThemesManager.Theme;

import javafx.event.EventType;

public class ThemeChangeEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<ThemeChangeEvent> THEME_CHANGE = new EventType<>(GenericActionEvent.ANY,
			"THEME_CHANGE"); //$NON-NLS-1$

	public static ThemeChangeEvent change(Theme newTheme) {
		return new ThemeChangeEvent(THEME_CHANGE, newTheme);
	}

	private final Theme theme;

	private ThemeChangeEvent(final EventType<ThemeChangeEvent> event, final Theme theme) {
		super(event);
		this.theme = theme;
	}

	public Theme getTheme() {
		return this.theme;
	}

}