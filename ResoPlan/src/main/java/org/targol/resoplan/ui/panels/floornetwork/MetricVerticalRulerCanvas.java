package org.targol.resoplan.ui.panels.floornetwork;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MetricVerticalRulerCanvas extends Canvas {

	public void repaint(ScrollPane scrollPane, Pane mainPane) {
		setWidth(20);
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
		double vOffset = (contentHeight - viewportHeight) * vValue;
		double firstTickCm = Math.floor(vOffset / mainStepPx) * mainStepCm;
		gc.setStroke(Color.BLACK);
		gc.setFill(Color.DARKGRAY);
		gc.setFont(Font.font(9));
		gc.setLineWidth(2);

		for (double cm = firstTickCm;; cm += mainStepCm) {
			double px = cm * scale - vOffset;
			if (px > height) {
				break;
			}
			if (px >= 0) {
				gc.strokeLine(0, px, 15, px);
				String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
				gc.fillText(label, 2, px - 6);
				double subPx = px - mainStepPx / 2;
				if (subPx > 0 && subPx < height) {
					gc.strokeLine(0, subPx, 8, subPx);
				}
			}
		}
	}

	public void clear() {
		setWidth(0);
//		GraphicsContext gc = getGraphicsContext2D();
//		double height = getHeight();
//		gc.setFill(Color.web("#F0F0F0"));
//		gc.fillRect(0, 0, 20, height);
//		setWidth(00);
	}

}
