package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CatalogButton extends ToggleButton {
	private static final double size = 30.0d;
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);
	final NodeModel model;

	public CatalogButton(final NodeModel model, final Supplier<GenericActionEvent> eventSupplier) {
		this.model = model;
		setPrefHeight(size);
		setPrefWidth(size);
		setMinHeight(size);
		setMinWidth(size);
		setMaxHeight(size);
		setMaxWidth(size);
		final String desc = model.getDescription();
		setTooltip(new Tooltip(desc));

		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		UiEventBus.register(this, ThemeChangeEvent.THEME_CHANGE, (event) -> updateAppearance());
		setOnAction(e -> UiEventBus.send(eventSupplier.get()));
		updateAppearance();
	}

	public double getImgWidth() {
		return this.imgWidth.get();
	}

	public void setImgWidth(final double width) {
		this.imgWidth.set(width);
	}

	public DoubleProperty imgWidthProperty() {
		return this.imgWidth;
	}

	private void updateAppearance() {
		String imgName = this.model.getImgName();
		if (imgName == null || imgName.isBlank()) {
			imgName = "unknown"; //$NON-NLS-1$
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getCatalogIcon(imgName, true));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		setGraphic(view);
		setTooltip(new Tooltip(this.model.getDescription()));
	}
}
