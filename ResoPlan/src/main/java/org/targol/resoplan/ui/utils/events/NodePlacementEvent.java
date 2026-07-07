package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;

import javafx.event.EventType;

public class NodePlacementEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<NodePlacementEvent> PLACEMENT_ANY = new EventType<>(GenericActionEvent.ANY,
			"NODE_PLACEMENT_ANY"); //$NON-NLS-1$
	public static final EventType<NodePlacementEvent> ELEC = new EventType<>(PLACEMENT_ANY, "NODE_ELEC"); //$NON-NLS-1$
	public static final EventType<NodePlacementEvent> WATER_EVAC = new EventType<>(PLACEMENT_ANY, "NODE_EVAC"); //$NON-NLS-1$
	public static final EventType<NodePlacementEvent> WATER_ALIM = new EventType<>(PLACEMENT_ANY, "NODE_ALIM"); //$NON-NLS-1$
	public static final EventType<NodePlacementEvent> NET = new EventType<>(PLACEMENT_ANY, "NODE_NET"); //$NON-NLS-1$

	public static NodePlacementEvent of(final LayerType layer, final Floor floor, final NodeModel model) {
		return switch (layer) {
		case ELEC -> new NodePlacementEvent(ELEC, floor, model, null);
		case WATER_EVAC -> new NodePlacementEvent(WATER_EVAC, floor, model, null);
		case WATER_ALIM -> new NodePlacementEvent(WATER_ALIM, floor, model, null);
		case NET -> new NodePlacementEvent(NET, floor, model, null);
		};
	}

	public static NodePlacementEvent of(final LayerType layer, final Floor floor, final NodeModel model,
			final NodeCross nodeCross) {
		return switch (layer) {
		case ELEC -> new NodePlacementEvent(ELEC, floor, model, nodeCross);
		case WATER_EVAC -> new NodePlacementEvent(WATER_EVAC, floor, model, nodeCross);
		case WATER_ALIM -> new NodePlacementEvent(WATER_ALIM, floor, model, nodeCross);
		case NET -> new NodePlacementEvent(NET, floor, model, nodeCross);
		};
	}

	private final Floor floor;
	private final NodeModel model;
	private final NodeCross nodeCross;

	private NodePlacementEvent(final EventType<NodePlacementEvent> eventType, final Floor floor, final NodeModel model,
			final NodeCross nodeCross) {
		super(eventType);
		this.floor = Objects.requireNonNull(floor, "Le Floor ne peut pas être null");
		this.model = model;
		this.nodeCross = nodeCross;
	}

	public Floor getFloor() {
		return this.floor;
	}

	public NodeModel getModel() {
		return this.model;
	}

	public NodeCross getNodeCross() {
		return this.nodeCross;
	}
}