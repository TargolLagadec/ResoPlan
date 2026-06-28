package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.Floor;
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

	private final Floor floor;
	private final HookType hook;

	public static LinkTracingEvent of(final LayerType layer, final Floor floor, final HookType hook) {
		return switch (layer) {
		case ELEC -> new LinkTracingEvent(ELEC, floor, hook);
		case WATER_EVAC -> new LinkTracingEvent(WATER_EVAC, floor, hook);
		case WATER_ALIM -> new LinkTracingEvent(WATER_ALIM, floor, hook);
		case NET -> new LinkTracingEvent(NET, floor, hook);
		};
	}

	public LinkTracingEvent(final EventType<LinkTracingEvent> eventType, final Floor floor, final HookType hook) {
		super(eventType);
		this.floor = Objects.requireNonNull(floor, "Le Floor ne peut pas être null");
		this.hook = Objects.requireNonNull(hook, "Le HookType ne peut pas être null");
	}

	public Floor getFloor() {
		return this.floor;
	}

	public HookType getHook() {
		return this.hook;
	}

}
