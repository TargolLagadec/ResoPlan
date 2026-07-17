package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.model.AbstractNode;

import javafx.event.EventType;

public class NodeMoveEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<NodeMoveEvent> NODE_MOVE = new EventType<>(GenericActionEvent.ANY, "NODE_MOVE"); //$NON-NLS-1$

	public static NodeMoveEvent of(final AbstractNode node) {
		return new NodeMoveEvent(node);
	}

	private final AbstractNode node;

	private NodeMoveEvent(AbstractNode node) {
		super(NODE_MOVE);
		this.node = node;
	}

	public AbstractNode getNode() {
		return this.node;
	}
}