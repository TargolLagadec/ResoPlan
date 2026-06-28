package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.IThemeChangeListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CatalogButtonUpOrDown extends ToggleButton implements IThemeChangeListener {
	private static final double size = 30.0d;
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);
	private final NodeModel model;
	private final NodeCross nodeCross;

	public CatalogButtonUpOrDown(final NodeModel model, final NodeCross nodeCross,
			final Supplier<GenericActionEvent> eventSupplier) {
		this.model = model;
		this.nodeCross = nodeCross;
		this.setPrefHeight(size);
		this.setPrefWidth(size);
		this.setMinHeight(size);
		this.setMinWidth(size);
		this.setMaxHeight(size);
		this.setMaxWidth(size);
		final String desc = model.getDescription();
		this.setTooltip(new Tooltip(desc));

		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.setOnAction(e -> UiEventBus.send(eventSupplier.get()));
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
		String imgName = this.model.getImgName();
		if (imgName == null || imgName.isBlank()) {
			imgName = "unknown";
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getCatalogIcon(imgName, true));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		// Par défaut, les images sont en descente, on fait un miroir vertical pour la
		// montée
		if (NodeCross.GOES_UP.equals(this.nodeCross)) {
			view.setScaleY(-1);
		}
		setGraphic(view);
		setTooltip(new Tooltip(this.model.getDescription()));
	}
}
