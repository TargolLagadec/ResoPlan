package org.targol.resoplan.ui.panels.floornetwork.decorators;

import org.targol.resoplan.ui.utils.ThemesHelper.Theme;
import org.targol.resoplan.utils.PreferencesHelper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MetricVerticalRulerForColumnCanvas extends Canvas {

	private Color backgroundColor = Color.DARKGRAY;
	private Color strokeColor = Color.BLACK;

	public void repaint(final BorderPane mainPane, final double hsp) {
		updateColorsFromTheme();
		setWidth(50);
		final GraphicsContext gc = getGraphicsContext2D();
		final double height = getHeight();
		gc.setFill(this.backgroundColor);
		gc.fillRect(0, 0, 50, height);

//		double mainStepCm = MetricGridCanvas.calculateDynamicStep(scale);
//		double mainStepPx = mainStepCm * scale;
//
//		double contentHeight = mainPane.getBoundsInParent().getHeight();
//		double firstTickCm = Math.floor(contentHeight / mainStepPx) * mainStepCm;
//		gc.setStroke(Color.BLACK);
//		gc.setFill(Color.DARKGRAY);
//		gc.setFont(Font.font(9));
//		gc.setLineWidth(2);
//
//		for (double cm = firstTickCm;; cm += mainStepCm) {
//			double px = cm * scale - vOffset;
//			if (px > height) {
//				break;
//			}
//			if (px >= 0) {
//				gc.strokeLine(0, px, 15, px);
//				String label = String.format("%.1fm", cm / 100.0); //$NON-NLS-1$
//				gc.fillText(label, 2, px - 6);
//				double subPx = px - mainStepPx / 2;
//				if (subPx > 0 && subPx < height) {
//					gc.strokeLine(0, subPx, 8, subPx);
//				}
//			}
	}

	private double calculateStep() {
		return 0;
	}

	private void updateColorsFromTheme() {
		if (getScene() == null) {
			return;
		}
		final Theme theme = PreferencesHelper.getCurrentTheme();
		this.strokeColor = theme.getImagesMainColor();
		this.backgroundColor = theme.getBackColor();
	}

}
