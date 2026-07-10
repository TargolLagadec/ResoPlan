package org.targol.resoplan.ui.panels.floornetwork;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MetricHorizontalRulerCanvas extends Canvas {

	public MetricHorizontalRulerCanvas() {
		setHeight(20);
	}

	public void repaint(ScrollPane scrollPane, Pane mainPane) {
		GraphicsContext gc = getGraphicsContext2D();
		double width = getWidth();
		gc.setFill(Color.web("#F0F0F0"));
		gc.fillRect(0, 0, width, 20);

		double scale = mainPane.getScaleX();
		double mainStepCm = MetricGridCanvas.calculateDynamicStep(scale);
		double mainStepPx = mainStepCm * scale;

		double contentWidth = mainPane.getBoundsInParent().getWidth();
		double viewportWidth = scrollPane.getViewportBounds().getWidth();
		double hValue = scrollPane.getHvalue();
		double hOffset = (contentWidth - viewportWidth) * hValue;
		if (hOffset < 0) {
			hOffset = 0;
		}
		double firstTickCm = Math.floor(hOffset / mainStepPx) * mainStepCm;
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.DARKGRAY);
		gc.setFont(Font.font(9));
		gc.setLineWidth(2);

		for (double cm = firstTickCm;; cm += mainStepCm) {
			double px = cm * scale - hOffset;
			if (px > width) {
				break;
			}
			if (px >= 0) {
				gc.strokeLine(px, 0, px, 15);
				String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
				gc.fillText(label, px + 3, 12);
				double subPx = px - mainStepPx / 2;
				if (subPx > 0 && subPx < width) {
					gc.strokeLine(subPx, 0, subPx, 8);
				}
			}
		}
	}
}
