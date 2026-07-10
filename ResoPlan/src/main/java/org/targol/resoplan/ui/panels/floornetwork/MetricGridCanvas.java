package org.targol.resoplan.ui.panels.floornetwork;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MetricGridCanvas extends Canvas {

	public void repaint(ScrollPane scrollPane, Pane mainPane) {
		GraphicsContext gc = getGraphicsContext2D();
		double width = getWidth();
		double height = getWidth();
		gc.clearRect(0, 0, width, height);

		double scale = mainPane.getScaleX();
		double stepCm = calculateDynamicStep(scale);
		double stepPx = stepCm * scale;
		double startX = scrollPane.getTranslateX() % stepPx;
		double startY = scrollPane.getTranslateY() % stepPx;

		gc.setStroke(Color.GRAY);
		gc.setLineWidth(0.5);
		gc.setLineDashes(1, 5);
		for (double x = startX; x < width; x += stepPx) {
			gc.strokeLine(x, 0, x, height);
		}
		for (double y = startY; y < height; y += stepPx) {
			gc.strokeLine(0, y, width, y);
		}
	}

	public static double calculateDynamicStep(double currentScale) {
		double minPixelSpacing = 50.0;

		// Liste des pas réels (en cm) qui nous intéressent (1cm, 2cm, 5cm, 10cm, 50cm, 1m, 2m, 5m...)
		double[] allowedSteps = { 1, 2, 5, 10, 20, 50, 100, 200, 500 };

		for (double step : allowedSteps) {
			double pixelSize = step * currentScale;
			if (pixelSize >= minPixelSpacing) {
				return step;
			}
		}
		return 500;
	}

}
