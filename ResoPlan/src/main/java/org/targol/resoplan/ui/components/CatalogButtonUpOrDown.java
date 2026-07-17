package org.targol.resoplan.ui.components;

import java.util.function.Supplier;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.ThemesHelper;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.RefreshFloorLayerEvent;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CatalogButtonUpOrDown extends ToggleButton {

	private static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);
	private static final double size = 30.0d;

	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);

	private final NodeModel model;
	private final NodeCross nodeCross;

	public CatalogButtonUpOrDown(final NodeModel model, final NodeCross nodeCross,
			final Supplier<GenericActionEvent> eventSupplier) {
		this.model = model;
		this.nodeCross = nodeCross;
		setPrefHeight(size);
		setPrefWidth(size);
		setMinHeight(size);
		setMinWidth(size);
		setMaxHeight(size);
		setMaxWidth(size);
		final String desc = model.getDescription();
		setTooltip(new Tooltip(desc));
		UiEventBus.register(this, ThemeChangeEvent.THEME_CHANGE, (event) -> updateAppearance());
		UiEventBus.register(this, RefreshFloorLayerEvent.REFRESH_ANY, (event) -> setButtonAvialiability(event));

		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		setOnAction(e -> UiEventBus.send(eventSupplier.get()));
		updateAppearance();
	}

	private void setButtonAvialiability(final RefreshFloorLayerEvent event) {
		final Floor floor = event.getFloor();
		if (NodeCross.GOES_DOWN.equals(this.nodeCross) && SVC_PROJ.isBottomestFloor(floor)) {
			setDisable(true);
		}
		if (NodeCross.GOES_UP.equals(this.nodeCross) && SVC_PROJ.isTopmostFloor(floor)) {
			setDisable(true);
		}
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
		final ImageView view = new ImageView(ThemesHelper.getCatalogIcon(imgName, true));
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
