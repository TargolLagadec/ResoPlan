package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.LayerType;

import javafx.event.EventType;

public class NodePropertiesAskedEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<NodePropertiesAskedEvent> NODE_PROPS_ANY = new EventType<>(GenericActionEvent.ANY,
			"NODE_PROPS_ANY"); //$NON-NLS-1$
	public static final EventType<NodePropertiesAskedEvent> ELEC = new EventType<>(NODE_PROPS_ANY, "NODE_PROPS_ELEC"); //$NON-NLS-1$
	public static final EventType<NodePropertiesAskedEvent> WATER_EVAC = new EventType<>(NODE_PROPS_ANY,
			"NODE_PROPS_EVAC"); //$NON-NLS-1$
	public static final EventType<NodePropertiesAskedEvent> WATER_ALIM = new EventType<>(NODE_PROPS_ANY,
			"NODE_PROPS_ALIM"); //$NON-NLS-1$
	public static final EventType<NodePropertiesAskedEvent> NET = new EventType<>(NODE_PROPS_ANY, "NODE_PROPS_NET"); //$NON-NLS-1$

	public static NodePropertiesAskedEvent of(final LayerType layer, final AbstractNode node) {
		return switch (layer) {
		case ELEC -> new NodePropertiesAskedEvent(ELEC, node);
		case WATER_EVAC -> new NodePropertiesAskedEvent(WATER_EVAC, node);
		case WATER_ALIM -> new NodePropertiesAskedEvent(WATER_ALIM, node);
		case NET -> new NodePropertiesAskedEvent(NET, node);
		};
	}

	private final AbstractNode node;

	private NodePropertiesAskedEvent(final EventType<NodePropertiesAskedEvent> eventType, final AbstractNode node) {
		super(eventType);
		this.node = node;
	}

	public AbstractNode getNode() {
		return this.node;
	}

}