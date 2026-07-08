package org.targol.resoplan.ui.panels.floornetwork;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MetricVerticalRulerCanvas extends Canvas {

	public MetricVerticalRulerCanvas() {
		setWidth(20);
	}

	public void repaint(ScrollPane scrollPane, Pane mainPane) {
		GraphicsContext gc = getGraphicsContext2D();
		double height = getHeight();
		gc.setFill(Color.web("#F0F0F0"));
		gc.fillRect(0, 0, 20, height);

		double scale = mainPane.getScaleY();
		double mainStepCm = MetricGridCanvas.calculateDynamicStep(scale);
		double mainStepPx = mainStepCm * scale;

		double contentHeight = mainPane.getBoundsInParent().getHeight();
		double viewportHeight = scrollPane.getViewportBounds().getHeight();
		double vValue = scrollPane.getVvalue();
		double hOffset = (contentHeight - viewportHeight) * vValue;
		double firstTickCm = Math.ceil(hOffset / mainStepPx) * mainStepCm;
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.DARKGRAY);
		gc.setFont(Font.font(9));

		for (double cm = firstTickCm;; cm += mainStepCm) {
			double px = cm * scale - hOffset;
			if (px > height) {
				break;
			}
			// Grand trait de graduation
			gc.strokeLine(0, px, 15, px);
			String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
			gc.fillText(label, 12, px + 3);
			double subPx = px - mainStepPx / 2;
			if (subPx > 0 && subPx < height) {
				gc.strokeLine(0, subPx, 8, subPx);
			}
		}
	}
}
