package org.targol.resoplan.ui.components;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.utils.IThemeChangeListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CustomLayerRadio extends RadioButton implements IThemeChangeListener {

	private final ObjectProperty<LayerType> type = new SimpleObjectProperty<>(null);
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(25.0d);

	public CustomLayerRadio(final LayerType type) {
		this.type.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		setType(type);
	}

	public LayerType getType() {
		return this.type.get();
	}

	public void setType(final LayerType type) {
		this.type.set(type);
	}

	public ObjectProperty<LayerType> typeProperty() {
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
		final LayerType buttonType = getType();
		if (buttonType == null) {
			return;
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getIcon(buttonType.getKey()));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		setGraphic(view);
		setTooltip(new Tooltip(Messages.getString("LayerCheck.".concat(buttonType.getKey())))); //$NON-NLS-1$
	}

	@Override
	public void themeChanged(final Theme newTheme) {
		updateAppearance();
	}
}
