package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Node;

import javafx.event.EventType;

public class LinkedNodePlacementEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<LinkedNodePlacementEvent> LINKEDNODE_ANY = new EventType<>(GenericActionEvent.ANY,
			"LINKEDNODE_ANY"); //$NON-NLS-1$
	public static final EventType<LinkedNodePlacementEvent> ELEC = new EventType<>(LINKEDNODE_ANY, "NODE_ELEC"); //$NON-NLS-1$
	public static final EventType<LinkedNodePlacementEvent> WATER_EVAC = new EventType<>(LINKEDNODE_ANY, "NODE_EVAC"); //$NON-NLS-1$
	public static final EventType<LinkedNodePlacementEvent> WATER_ALIM = new EventType<>(LINKEDNODE_ANY, "NODE_ALIM"); //$NON-NLS-1$
	public static final EventType<LinkedNodePlacementEvent> NET = new EventType<>(LINKEDNODE_ANY, "NODE_NET"); //$NON-NLS-1$

	public static LinkedNodePlacementEvent of(final LayerType layer, final Floor floor, final Node node) {
		return switch (layer) {
		case ELEC -> new LinkedNodePlacementEvent(ELEC, floor, node);
		case WATER_EVAC -> new LinkedNodePlacementEvent(WATER_EVAC, floor, node);
		case WATER_ALIM -> new LinkedNodePlacementEvent(WATER_ALIM, floor, node);
		case NET -> new LinkedNodePlacementEvent(NET, floor, node);
		};
	}

	private final Floor floor;
	private final Node node;

	private LinkedNodePlacementEvent(final EventType<LinkedNodePlacementEvent> eventType, final Floor floor,
			final Node node) {
		super(eventType);
		this.floor = Objects.requireNonNull(floor, "Le Floor ne peut pas être null");
		this.node = Objects.requireNonNull(node, "Le Node ne peut pas être null");
	}

	public Floor getFloor() {
		return this.floor;
	}

	public Node getNode() {
		return this.node;
	}
}