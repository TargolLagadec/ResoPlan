package org.targol.resoplan.ui.utils;

import org.targol.resoplan.ui.panels.floornetwork.layers.evac.EvacMode;

import javafx.event.Event;
import javafx.event.EventType;

public class AppActionEvent extends Event {

	public static final EventType<AppActionEvent> ANY = new EventType<>(Event.ANY, "APP_ACTION"); //$NON-NLS-1$

	// global actions
	public static final EventType<AppActionEvent> TRIGGER_CATALOG = new EventType<>(ANY, "TRIGGER_CATALOG"); //$NON-NLS-1$
	public static final EventType<AppActionEvent> TRIGGER_ALIGN = new EventType<>(ANY, "TRIGGER_ALIGN"); //$NON-NLS-1$
	public static final EventType<AppActionEvent> TRIGGER_NETWORKS = new EventType<>(ANY, "TRIGGER_NETWORKS"); //$NON-NLS-1$
	public static final EventType<AppActionEvent> TRIGGER_DEBIT = new EventType<>(ANY, "TRIGGER_DEBIT"); //$NON-NLS-1$

	// evac Layer action
	public static final EventType<AppActionEvent> EVAC_MODE_CHANGED = new EventType<>(ANY, "EVAC_MODE_CHANGED"); //$NON-NLS-1$
	private EvacMode evacMode;

	public AppActionEvent(final EventType<AppActionEvent> eventType) {
		super(eventType);
	}

	// Constructor for evac actions
	public AppActionEvent(final EventType<AppActionEvent> eventType, final EvacMode evacMode) {
		super(eventType);
		this.evacMode = evacMode;
	}

	public EvacMode getEvacMode() {
		return this.evacMode;
	}
}
