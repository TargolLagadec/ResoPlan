package org.targol.resoplan.ui.utils.events;

import java.util.Objects;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;

import javafx.event.EventType;

public class RefreshFloorLayerEvent extends GenericActionEvent {

	private static final long serialVersionUID = -237107763542098637L;
	public static final EventType<RefreshFloorLayerEvent> REFRESH_ANY = new EventType<>(GenericActionEvent.ANY,
			"REFRESH_ANY"); //$NON-NLS-1$
	public static final EventType<RefreshFloorLayerEvent> ELEC = new EventType<>(REFRESH_ANY, "REFRESH_ELEC"); //$NON-NLS-1$
	public static final EventType<RefreshFloorLayerEvent> WATER_EVAC = new EventType<>(REFRESH_ANY, "REFRESH_EVAC"); //$NON-NLS-1$
	public static final EventType<RefreshFloorLayerEvent> WATER_ALIM = new EventType<>(REFRESH_ANY, "REFRESH_ALIM"); //$NON-NLS-1$
	public static final EventType<RefreshFloorLayerEvent> NET = new EventType<>(REFRESH_ANY, "REFRESH_NET"); //$NON-NLS-1$
	public static final EventType<RefreshFloorLayerEvent> CHANGE_FLOOR = new EventType<>(REFRESH_ANY, "CHANGE_FLOOR"); //$NON-NLS-1$

	public static RefreshFloorLayerEvent of(final LayerType layer, final Floor floor) {
		return switch (layer) {
		case ELEC -> new RefreshFloorLayerEvent(ELEC, floor);
		case WATER_EVAC -> new RefreshFloorLayerEvent(WATER_EVAC, floor);
		case WATER_ALIM -> new RefreshFloorLayerEvent(WATER_ALIM, floor);
		case NET -> new RefreshFloorLayerEvent(NET, floor);
		};
	}

	public static RefreshFloorLayerEvent floorChanged(final Floor floor) {
		return new RefreshFloorLayerEvent(CHANGE_FLOOR, floor);
	}

	private final Floor floor;

	private RefreshFloorLayerEvent(final EventType<RefreshFloorLayerEvent> eventType, final Floor floor) {
		super(eventType);
		this.floor = Objects.requireNonNull(floor, "Le Floor ne peut pas être null");
	}

	public Floor getFloor() {
		return this.floor;
	}
}