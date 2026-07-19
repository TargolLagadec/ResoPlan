package org.targol.resoplan.ui.panels.floornetwork.decorators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class MetricHorizontalRulerCanvas extends AbstractMetricRulerCanvas {

	@Override
	public void repaint(final ScrollPane scrollPane, final Pane mainPane) {
		updateColorsFromTheme();
		setHeight(20);
		final GraphicsContext gc = getGraphicsContext2D();
		final double width = getWidth();
		gc.setFill(this.backgroundColor);
		gc.fillRect(0, 0, width, 20);

		final double scale = mainPane.getScaleX();
		final double mainStepCm = MetricGridCanvas.calculateDynamicStep(scale);
		final double mainStepPx = mainStepCm * scale;

		final double contentWidth = mainPane.getBoundsInParent().getWidth();
		final double viewportWidth = scrollPane.getViewportBounds().getWidth();
		final double hValue = scrollPane.getHvalue();
		double hOffset = (contentWidth - viewportWidth) * hValue;
		if (hOffset < 0) {
			hOffset = 0;
		}
		final double firstTickCm = Math.floor(hOffset / mainStepPx) * mainStepCm;
		gc.setStroke(this.strokeColor);
		gc.setFill(this.strokeColor);
		gc.setFont(Font.font(9));
		gc.setLineWidth(2);

		for (double cm = firstTickCm;; cm += mainStepCm) {
			final double px = cm * scale - hOffset;
			if (px > width) {
				break;
			}
			if (px >= 0) {
				gc.strokeLine(px, 0, px, 15);
				final String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
				gc.fillText(label, px + 3, 12);
				final double subPx = px - mainStepPx / 2;
				if (subPx > 0 && subPx < width) {
					gc.strokeLine(subPx, 0, subPx, 8);
				}
			}
		}
	}

	@Override
	public void clear() {
		setHeight(0);
	}
}
