package org.targol.resoplan.ui.utils.events;

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

	public static NodePlacementEvent of(final LayerType layer, final NodeModel model) {
		return switch (layer) {
		case ELEC -> new NodePlacementEvent(ELEC, model, null);
		case WATER_EVAC -> new NodePlacementEvent(WATER_EVAC, model, null);
		case WATER_ALIM -> new NodePlacementEvent(WATER_ALIM, model, null);
		case NET -> new NodePlacementEvent(NET, model, null);
		};
	}

	public static NodePlacementEvent of(final LayerType layer, final NodeModel model, final NodeCross nodeCross) {
		return switch (layer) {
		case ELEC -> new NodePlacementEvent(ELEC, model, nodeCross);
		case WATER_EVAC -> new NodePlacementEvent(WATER_EVAC, model, nodeCross);
		case WATER_ALIM -> new NodePlacementEvent(WATER_ALIM, model, nodeCross);
		case NET -> new NodePlacementEvent(NET, model, nodeCross);
		};
	}

	private final NodeModel model;
	private final NodeCross nodeCross;

	private NodePlacementEvent(final EventType<NodePlacementEvent> eventType, final NodeModel model,
			final NodeCross nodeCross) {
		super(eventType);
		this.model = model;
		this.nodeCross = nodeCross;
	}

	public NodeModel getModel() {
		return this.model;
	}

	public NodeCross getNodeCross() {
		return this.nodeCross;
	}
}