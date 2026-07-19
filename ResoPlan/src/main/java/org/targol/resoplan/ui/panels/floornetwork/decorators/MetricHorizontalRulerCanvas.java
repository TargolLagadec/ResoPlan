package org.targol.resoplan.ui.panels.floornetwork.decorators;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class MetricHorizontalRulerCanvas extends Canvas {
	private Color backgroundColor = Color.DARKGRAY;
	private Color strokeColor = Color.BLACK;

	public void repaint(final ScrollPane scrollPane, final Pane mainPane) {
		updateColorsFromCss();
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

	public void clear() {
		setHeight(0);
	}

	private void updateColorsFromCss() {
		if (getScene() == null) {
			return;
		}
		getScene().getStylesheets().get(0);
		// On crée un Label invisible qui va nous servir de sonde CSS
		final Label cssProbe = new Label();
		cssProbe.setManaged(false);
		cssProbe.setVisible(false);
		if (getParent() instanceof final Pane parentPane) {
			parentPane.getChildren().add(cssProbe);
			cssProbe.applyCss();
			final Paint textPaint = cssProbe.getTextFill();
			if (textPaint instanceof Color) {
				this.strokeColor = (Color) textPaint;
			}
			if (cssProbe.getBackground() != null && !cssProbe.getBackground().getFills().isEmpty()) {
				final BackgroundFill fill = cssProbe.getBackground().getFills().get(0);
				if (fill.getFill() instanceof Color) {
					this.backgroundColor = (Color) fill.getFill();
				}
			}
			parentPane.getChildren().remove(cssProbe);
		}
	}
}
