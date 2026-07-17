package org.targol.resoplan.ui.utils.events;

import javafx.event.Event;
import javafx.event.EventType;

public class GenericActionEvent extends Event {

	private static final long serialVersionUID = -3385174978941478162L;

	public static final EventType<GenericActionEvent> ANY = new EventType<>(Event.ANY, "APP_UI_ANY"); //$NON-NLS-1$

	public static final EventType<GenericActionEvent> SHOW_CATALOG = new EventType<>(ANY, "SHOW_CATALOG"); //$NON-NLS-1$
	public static final EventType<GenericActionEvent> SHOW_ALIGN = new EventType<>(ANY, "SHOW_ALIGN"); //$NON-NLS-1$
	public static final EventType<GenericActionEvent> SHOW_NETWORKS = new EventType<>(ANY, "SHOW_NETWORKS"); //$NON-NLS-1$
	public static final EventType<GenericActionEvent> SHOW_DEBIT = new EventType<>(ANY, "SHOW_DEBIT"); //$NON-NLS-1$
	public static final EventType<GenericActionEvent> PREF_CHANGE = new EventType<>(ANY, "PREF_CHANGE"); //$NON-NLS-1$

	public GenericActionEvent(final EventType<? extends Event> eventType) {
		super(eventType);
	}
}
