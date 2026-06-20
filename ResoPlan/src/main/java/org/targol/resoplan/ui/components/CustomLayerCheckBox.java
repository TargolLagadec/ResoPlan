package org.targol.resoplan.ui.components;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.ThemesManager.Theme;
import org.targol.resoplan.utils.IThemeChangeListener;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CustomLayerCheckBox extends CheckBox implements IThemeChangeListener {

	private final ObjectProperty<String> type = new SimpleObjectProperty<>(null);
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(16.0d);

	public CustomLayerCheckBox(final LayerCheckType type) {
		this();
		setType(type.getKey());
	}

	public CustomLayerCheckBox() {
		this.type.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
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
		setTooltip(new Tooltip(Messages.getString("LayerCheck.".concat(buttonType)))); //$NON-NLS-1$
	}

	@Override
	public void themeChanged(final Theme newTheme) {
		updateAppearance();
	}
}
