package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.NodeModel;

import javafx.event.EventType;

public class NodePlacementEvent extends GenericActionEvent {

	public static final EventType<NodePlacementEvent> PLACEMENT_ANY = new EventType<>(GenericActionEvent.ANY,
			"NODE_PLACEMENT_ANY");
	public static final EventType<NodePlacementEvent> ELEC = new EventType<>(PLACEMENT_ANY, "NODE_ELEC");
	public static final EventType<NodePlacementEvent> WATER_EVAC = new EventType<>(PLACEMENT_ANY, "NODE_EVAC");
	public static final EventType<NodePlacementEvent> WATER_ALIM = new EventType<>(PLACEMENT_ANY, "NODE_ALIM");
	public static final EventType<NodePlacementEvent> NET = new EventType<>(PLACEMENT_ANY, "NODE_NET");

	public static NodePlacementEvent of(final LayerType layer, final NodeModel model) {
		return switch (layer) {
		case ELEC -> new NodePlacementEvent(ELEC, model);
		case WATER_EVAC -> new NodePlacementEvent(WATER_EVAC, model);
		case WATER_ALIM -> new NodePlacementEvent(WATER_ALIM, model);
		case NET -> new NodePlacementEvent(NET, model);
		};
	}

	private final NodeModel model;

	public NodePlacementEvent(final EventType<NodePlacementEvent> eventType, final NodeModel model) {
		super(eventType);
		this.model = Objects.requireNonNull(model, "Le NodeModel ne peut pas être null");
	}

	public NodeModel getModel() {
		return this.model;
	}
}