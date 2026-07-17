package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.LayerType;

import javafx.event.EventType;

public class ChangeLayerEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<ChangeLayerEvent> CHANGE_LAYER = new EventType<>(GenericActionEvent.ANY,
			"CHANGE_LAYER"); //$NON-NLS-1$

	public static ChangeLayerEvent of(final LayerType layer) {
		return new ChangeLayerEvent(layer);
	}

	private final LayerType layer;

	private ChangeLayerEvent(LayerType layer) {
		super(CHANGE_LAYER);
		this.layer = Objects.requireNonNull(layer, "Le Layer ne peut pas être null");
	}

	public LayerType getLayer() {
		return this.layer;
	}
}