package org.targol.resoplan.ui.panels.floornetwork;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MetricGridCanvas extends Canvas {

	public void repaint(ScrollPane scrollPane, Pane mainPane) {
		gc.clearRect(0, 0, this.width, this.height);

		double scale = viewContext.getScale();
		double stepCm = calculateDynamicStep(scale);
		double stepPx = stepCm * scale;
		double startX = viewContext.getTranslateX() % stepPx;
		double startY = viewContext.getTranslateY() % stepPx;

		gc.setStroke(Color.LIGHTGRAY);
		gc.setLineWidth(1.0);
		for (double x = startX; x < this.width; x += stepPx) {
			gc.strokeLine(x, 0, x, this.height);
		}
		for (double y = startY; y < this.height; y += stepPx) {
			gc.strokeLine(0, y, this.width, y);
		}
	}

	public static double calculateDynamicStep(double currentScale) {
		// On veut qu'une graduation principale fasse au moins 50 pixels à l'écran
		double minPixelSpacing = 50.0;

		// Liste des pas réels (en cm) qui nous intéressent (1cm, 2cm, 5cm, 10cm, 50cm, 1m, 2m, 5m...)
		double[] allowedSteps = { 1, 2, 5, 10, 20, 50, 100, 200, 500 };

		for (double step : allowedSteps) {
			// Combien de pixels fait ce pas avec le zoom actuel ?
			double pixelSize = step * currentScale;
			if (pixelSize >= minPixelSpacing) {
				return step; // C'est le premier pas lisible, on le choisit !
			}
		}
		return 500; // Pas par défaut (5 mètres) si on est extrêmement dézoomé
	}

}
