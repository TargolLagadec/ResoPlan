package org.targol.resoplan.ui.utils.events;

import javafx.event.Event;
import javafx.event.EventType;

public class GenericActionEvent extends Event {

	private static final long serialVersionUID = -3385174978941478162L;

	public static final EventType<GenericActionEvent> ANY = new EventType<>(Event.ANY, "APP_UI_ANY");

	public static final EventType<GenericActionEvent> TRIGGER_CATALOG = new EventType<>(ANY, "TRIGGER_CATALOG");
	public static final EventType<GenericActionEvent> TRIGGER_ALIGN = new EventType<>(ANY, "TRIGGER_ALIGN");
	public static final EventType<GenericActionEvent> TRIGGER_NETWORKS = new EventType<>(ANY, "TRIGGER_NETWORKS");
	public static final EventType<GenericActionEvent> TRIGGER_DEBIT = new EventType<>(ANY, "TRIGGER_DEBIT");

	public GenericActionEvent(final EventType<? extends Event> eventType) {
		super(eventType);
	}
}
