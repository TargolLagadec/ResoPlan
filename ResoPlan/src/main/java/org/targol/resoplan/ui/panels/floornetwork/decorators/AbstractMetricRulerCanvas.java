package org.targol.resoplan.ui.panels.floornetwork.decorators;

import org.targol.resoplan.ui.utils.ThemesHelper.Theme;
import org.targol.resoplan.utils.PreferencesHelper;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class AbstractMetricRulerCanvas extends Canvas {
	protected Color backgroundColor = Color.DARKGRAY;
	protected Color strokeColor = Color.BLACK;

	public abstract void repaint(final ScrollPane scrollPane, final Pane mainPane);

	public abstract void clear();

	protected void updateColorsFromTheme() {
		if (getScene() == null) {
			return;
		}
		final Theme theme = PreferencesHelper.getCurrentTheme();
		this.strokeColor = theme.getImagesMainColor();
		this.backgroundColor = theme.getBackColor();
	}
}
