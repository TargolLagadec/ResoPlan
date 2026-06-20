package org.targol.resoplan.ui.panels;

import org.targol.resoplan.ui.components.CustomLayerCheckBox;
import org.targol.resoplan.ui.components.LayerCheckType;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

public class LayeredTab extends Tab {

	private final CustomLayerCheckBox checkElec = new CustomLayerCheckBox(LayerCheckType.ELEC);
	private final CustomLayerCheckBox checkAlim = new CustomLayerCheckBox(LayerCheckType.EAU_ALIM);
	private final CustomLayerCheckBox checkEvac = new CustomLayerCheckBox(LayerCheckType.EAU_EVAC);
	private final CustomLayerCheckBox checkNet = new CustomLayerCheckBox(LayerCheckType.NET);

	public LayeredTab(final String title) {
		final Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-font-weight: bold;"); //$NON-NLS-1$
		final HBox tabHeader = new HBox(20);
		tabHeader.setAlignment(Pos.CENTER);
		final HBox layersBox = new HBox(5);
		layersBox.getChildren().addAll(this.checkElec, this.checkAlim, this.checkEvac, this.checkNet);
		tabHeader.getChildren().addAll(titleLabel, layersBox);
		tabHeader.setPrefHeight(100);
		tabHeader.setPrefWidth(250);
		this.setGraphic(tabHeader);
		this.setText("");
		PreferencesManager.getInstance().addThemeChangeListener(this.checkElec);
		PreferencesManager.getInstance().addThemeChangeListener(this.checkAlim);
		PreferencesManager.getInstance().addThemeChangeListener(this.checkEvac);
		PreferencesManager.getInstance().addThemeChangeListener(this.checkNet);

		this.checkElec.selectedProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Calque 1 visible : " + newVal);
		});
	}

	public CheckBox getCheckLayer1() {
		return this.checkElec;
	}
}