package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.IThemeChangeListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CustomButton extends Button implements IThemeChangeListener {
	private static final double size = 30.0d;
	private final ObjectProperty<String> type = new SimpleObjectProperty<>(null);
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);

	public CustomButton(final String type, final Supplier<GenericActionEvent> eventSupplier) {
		this();
		setType(type);
		this.setOnAction(e -> UiEventBus.send(eventSupplier.get()));
	}

	public CustomButton() {
		this.setPrefHeight(size);
		this.setPrefWidth(size);
		this.setMinHeight(size);
		this.setMinWidth(size);
		this.setMaxHeight(size);
		this.setMaxWidth(size);
		this.type.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
	}

	@Override
	public void themeChanged(final Theme newTheme) {
		updateAppearance();
	}

	public String getType() {
		return this.type.get();
	}

	public void setType(final String type) {
		this.type.set(type);
	}

	public ObjectProperty<String> typeProperty() {
		return this.type;
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
		final String buttonType = getType();
		if (buttonType == null) {
			return;
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getIcon(this.type.get()));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		setGraphic(view);
		setTooltip(new Tooltip(Messages.getString("CustomButton.".concat(buttonType)))); //$NON-NLS-1$
	}
}
