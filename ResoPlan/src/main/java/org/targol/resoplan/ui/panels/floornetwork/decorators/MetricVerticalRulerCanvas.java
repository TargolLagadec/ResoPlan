package org.targol.resoplan.ui.panels.floornetwork.decorators;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class MetricVerticalRulerCanvas extends AbstractMetricRulerCanvas {

	@Override
	public void repaint(final ScrollPane scrollPane, final Pane mainPane) {
		updateColorsFromTheme();
		setWidth(20);
		final GraphicsContext gc = getGraphicsContext2D();
		final double height = getHeight();
		gc.setFill(this.backgroundColor);
		gc.fillRect(0, 0, 20, height);

		final double scale = mainPane.getScaleY();
		final double mainStepCm = MetricGridCanvas.calculateDynamicStep(scale);
		final double mainStepPx = mainStepCm * scale;

		final double contentHeight = mainPane.getBoundsInParent().getHeight();
		final double viewportHeight = scrollPane.getViewportBounds().getHeight();
		final double vValue = scrollPane.getVvalue();
		final double vOffset = (contentHeight - viewportHeight) * vValue;
		final double firstTickCm = Math.floor(vOffset / mainStepPx) * mainStepCm;
		gc.setStroke(this.strokeColor);
		gc.setFill(this.strokeColor);
		gc.setFont(Font.font(9));
		gc.setLineWidth(1);

		for (double cm = firstTickCm;; cm += mainStepCm) {
			final double px = cm * scale - vOffset;
			if (px > height) {
				break;
			}
			if (px >= 0) {
				gc.strokeLine(0, px, 15, px);
				final String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
				gc.fillText(label, 2, px - 6);
				final double subPx = px - mainStepPx / 2;
				if (subPx > 0 && subPx < height) {
					gc.strokeLine(0, subPx, 8, subPx);
				}
			}
		}
	}

	@Override
	public void clear() {
		setWidth(0);
	}
}
