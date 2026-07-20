package org.targol.resoplan.ui.panels.floornetwork.decorators;

import org.targol.resoplan.ui.panels.floornetwork.properties.MetaNodeColumnPanel;
import org.targol.resoplan.ui.utils.ThemesHelper.Theme;
import org.targol.resoplan.utils.PreferencesHelper;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MetricVerticalRulerForColumnCanvas extends Canvas {

	public static final double TOP_BOTTOM_MARGIN = 50.0d;

	private Color backgroundColor = Color.DARKGRAY;
	private Color strokeColor = Color.BLACK;

	private final double hsp;
	private final MetaNodeColumnPanel mainPane;
	private boolean repainting = false;

	public MetricVerticalRulerForColumnCanvas(final MetaNodeColumnPanel mainPane, final double hsp) {
		this.mainPane = mainPane;
		this.hsp = hsp;
		setWidth(50);
		setHeight(mainPane.getHeight());
		mainPane.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null && newVal.doubleValue() > 0) {
				setHeight(mainPane.getAvailableHeight());
				repaint();
			}
		});
	}

	public void repaint() {
		if (this.repainting) {
			return;
		}
		this.repainting = true;
		updateColorsFromTheme();
		final double height = getHeight();
		setWidth(50);
		setHeight(height);
		double maxY = height - TOP_BOTTOM_MARGIN;
		final GraphicsContext gc = getGraphicsContext2D();
		gc.setFill(this.backgroundColor);
		gc.fillRect(0, 0, 50, height);
		// "Sol" et "plafond"
		Color tempColor = new Color(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue(),
				0.5);
		gc.setFill(tempColor);
		gc.fillRect(0, 0, 50, TOP_BOTTOM_MARGIN);
		gc.fillRect(0, maxY, 50, TOP_BOTTOM_MARGIN);

		gc.setStroke(this.strokeColor);
		gc.setFill(this.strokeColor);
		gc.setFont(Font.font(9));
		gc.setLineWidth(1);
		gc.strokeLine(2, TOP_BOTTOM_MARGIN, 2, maxY);

		double available = height - TOP_BOTTOM_MARGIN * 2;
		double pxPerMeter = available / this.hsp;

		double cm = 0;
		for (double posY = maxY; posY > TOP_BOTTOM_MARGIN; posY -= pxPerMeter * 25) {
			gc.strokeLine(1, posY, 15, posY);
			String label = String.format("%.2fm", cm / 100.0); //$NON-NLS-1$
			gc.fillText(label, 3, posY - 6);
			cm += 25;
		}
		gc.strokeLine(1, TOP_BOTTOM_MARGIN, 15, TOP_BOTTOM_MARGIN);
		String label = String.format("%.2fm", this.hsp / 100.0); //$NON-NLS-1$
		gc.fillText(label, 3, TOP_BOTTOM_MARGIN - 6);
		this.repainting = false;
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
