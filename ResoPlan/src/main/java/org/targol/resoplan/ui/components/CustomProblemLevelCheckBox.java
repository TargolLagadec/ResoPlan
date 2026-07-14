package org.targol.resoplan.ui.components;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.problems.Severity;
import org.targol.resoplan.ui.utils.ThemesManager;
import org.targol.resoplan.ui.utils.events.ThemeChangeEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public class CustomProblemLevelCheckBox extends CheckBox {

	private final ObjectProperty<Severity> type = new SimpleObjectProperty<>(null);
	private final DoubleProperty imgWidth = new SimpleDoubleProperty(20.0d);

	public CustomProblemLevelCheckBox(final Severity type) {
		this.type.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		this.imgWidth.addListener((obs, oldValue, newValue) -> {
			updateAppearance();
		});
		UiEventBus.register(this, ThemeChangeEvent.THEME_CHANGE, (event) -> updateAppearance());

		setType(type);
	}

	public Severity getType() {
		return this.type.get();
	}

	public void setType(final Severity type) {
		this.type.set(type);
	}

	public ObjectProperty<Severity> typeProperty() {
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
		final Severity buttonType = getType();
		if (buttonType == null) {
			return;
		}
		final ImageView view = new ImageView(ThemesManager.getInstance().getIcon(buttonType.getKey()));
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(this.imgWidth.get());
		setGraphic(view);
		setTooltip(new Tooltip(Messages.getString("CustomProblemLevelCheckBox.".concat(buttonType.getKey())))); //$NON-NLS-1$
	}
}
