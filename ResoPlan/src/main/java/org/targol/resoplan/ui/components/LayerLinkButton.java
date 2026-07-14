package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class LayerLinkButton extends ToggleButton {
	private static final double size = 30.0d;
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);
	final HookType hookType;

	public LayerLinkButton(final HookType hookType, final Supplier<GenericActionEvent> eventSupplier) {
		this.hookType = hookType;
		setPrefHeight(size);
		setPrefWidth(size);
		setMinHeight(size);
		setMinWidth(size);
		setMaxHeight(size);
		setMaxWidth(size);
		final String desc = hookType.getDescription();
		setTooltip(new Tooltip(desc));
		UiEventBus.register(this, ThemeChangeEvent.THEME_CHANGE, (event) -> updateAppearance());

		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
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
		final ImageView view = new ImageView(ThemesManager.getInstance().getIcon(this.hookType.getHookKey()));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		setGraphic(view);
		setTooltip(new Tooltip(this.hookType.getDescription()));
	}
}
