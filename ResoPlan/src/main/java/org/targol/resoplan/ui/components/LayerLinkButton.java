package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.utils.IThemeChangeListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class LayerLinkButton extends ToggleButton implements IThemeChangeListener {
	private static final double size = 30.0d;
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);
	final HookType hookType;

	public LayerLinkButton(final HookType hookType, final Supplier<GenericActionEvent> eventSupplier) {
		this.hookType = hookType;
		this.setPrefHeight(size);
		this.setPrefWidth(size);
		this.setMinHeight(size);
		this.setMinWidth(size);
		this.setMaxHeight(size);
		this.setMaxWidth(size);
		final String desc = hookType.getDescription();
		this.setTooltip(new Tooltip(desc));

		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.setOnAction(e -> fireEvent(eventSupplier.get()));
		updateAppearance();
	}

	@Override
	public void themeChanged(final Theme newTheme) {
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
