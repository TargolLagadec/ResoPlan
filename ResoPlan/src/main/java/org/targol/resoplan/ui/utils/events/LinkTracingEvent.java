package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.HookType;

import javafx.event.EventType;

public class LinkTracingEvent extends GenericActionEvent {
	private static final long serialVersionUID = -5386233855334640088L;

	public static final EventType<LinkTracingEvent> HOOK_ANY = new EventType<>(GenericActionEvent.ANY, "HOOK_ANY");
	public static final EventType<LinkTracingEvent> ELEC = new EventType<>(HOOK_ANY, "NODE_ELEC");
	public static final EventType<LinkTracingEvent> WATER_EVAC = new EventType<>(HOOK_ANY, "NODE_EVAC");
	public static final EventType<LinkTracingEvent> WATER_ALIM = new EventType<>(HOOK_ANY, "NODE_ALIM");
	public static final EventType<LinkTracingEvent> NET = new EventType<>(HOOK_ANY, "NODE_NET");

	private final HookType hook;

	public static LinkTracingEvent of(final LayerType layer, final HookType hook) {
		return switch (layer) {
		case ELEC -> new LinkTracingEvent(ELEC, hook);
		case WATER_EVAC -> new LinkTracingEvent(WATER_EVAC, hook);
		case WATER_ALIM -> new LinkTracingEvent(WATER_ALIM, hook);
		case NET -> new LinkTracingEvent(NET, hook);
		};
	}

	public LinkTracingEvent(final EventType<LinkTracingEvent> eventType, final HookType hook) {
		super(eventType);
		this.hook = Objects.requireNonNull(hook, "Le HookType ne peut pas être null");
	}

	public HookType getHook() {
		return this.hook;
	}

}
