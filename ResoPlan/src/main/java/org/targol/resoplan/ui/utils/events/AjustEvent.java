package org.targol.resoplan.ui.utils.events;

import org.targol.resoplan.model.Floor;

import javafx.event.EventType;
import javafx.scene.paint.Color;

public class AjustEvent extends GenericActionEvent {
	private static final long serialVersionUID = 1L;

	public static final EventType<AjustEvent> AJUST_ANY = new EventType<>(GenericActionEvent.ANY, "AJUST_ANY");

	public static final EventType<AjustEvent> FLOOR_UPDATED = new EventType<>(AJUST_ANY, "FLOOR_UPDATED");
	public static final EventType<AjustEvent> VISUAL_CHANGED = new EventType<>(AJUST_ANY, "VISUAL_CHANGED");
	public static final EventType<AjustEvent> SCALE_LINE_DEFINED = new EventType<>(AJUST_ANY, "SCALE_LINE_DEFINED");
	public static final EventType<AjustEvent> SCALE_LINE_START_REQUIRED = new EventType<>(AJUST_ANY,
			"SCALE_LINE_START_REQUIRED");

	private final Floor floor;
	private final double opacity;
	private final boolean visible;
	private final Color paintColor;
	private final double pixelLength;

	private AjustEvent(final EventType<AjustEvent> type, final Floor floor, final double opacity, final boolean visible,
			final Color paintColor, final double pixelLength) {
		super(type);
		this.floor = floor;
		this.opacity = opacity;
		this.visible = visible;
		this.paintColor = paintColor;
		this.pixelLength = pixelLength;
	}

	public static AjustEvent fireFloorUpdated(final Floor floor) {
		return new AjustEvent(FLOOR_UPDATED, floor, 1.0, true, null, 0.0);
	}

	public static AjustEvent fireVisualChanged(final Floor floor, final double opacity, final boolean visible,
			final Color paintColor) {
		return new AjustEvent(VISUAL_CHANGED, floor, opacity, visible, paintColor, 0.0);
	}

	public static AjustEvent fireScaleLineDefined(final double pixelLength) {
		return new AjustEvent(SCALE_LINE_DEFINED, null, 1.0, true, null, pixelLength);
	}

	public static AjustEvent fireScaleLineStartRequired() {
		return new AjustEvent(SCALE_LINE_START_REQUIRED, null, 1.0, true, null, 0.0);
	}

	public double getPixelLength() {
		return this.pixelLength;
	}

	public Floor getFloor() {
		return this.floor;
	}

	public double getOpacity() {
		return this.opacity;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public Color getPaintColor() {
		return this.paintColor;
	}
}