package org.targol.resoplan.ui.panels.floornetwork;

import java.io.File;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.enums.LayerType;
import org.targol.resoplan.ui.components.CustomLayerRadio;
import org.targol.resoplan.ui.panels.floornetwork.layers.evac.EvacuationsCanvas;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.PreferencesManager;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class LayeredFloorTab extends Tab {

	// Custom checkBoxes in tab header to display or not different networks layers
	private final CustomLayerRadio radioElec = new CustomLayerRadio(LayerType.ELEC);
	private final CustomLayerRadio radioAlim = new CustomLayerRadio(LayerType.WATER_ALIM);
	private final CustomLayerRadio radioEvac = new CustomLayerRadio(LayerType.WATER_EVAC);
	private final CustomLayerRadio radioNet = new CustomLayerRadio(LayerType.NET);
	private ToggleGroup headerToggleGroup;
	private ScrollPane centerScrollPane;
	private Group imageContainer;
	private StackPane stackPaneCalques;
	// Layers
	private EvacuationsCanvas evacCanvas;

	private final Floor floor;

	public LayeredFloorTab(final Floor floor, final boolean active) {
		this.floor = floor;
		initTabHeader();
		initContent();
		setdisableHeaderRadioButtons(!active);
	}

	private void initContent() {
		this.centerScrollPane = new ScrollPane();
		this.setContent(this.centerScrollPane);
		this.imageContainer = new Group();
		this.stackPaneCalques = new StackPane();
		this.imageContainer.getChildren().add(this.stackPaneCalques);
		this.centerScrollPane.setContent(this.imageContainer);

		if (this.floor.getImgPath() == null) {
			// Should never happen, we check that before opening this panel
			GuiUtils.errorAlert(Messages.getString("LayeredFloorTab.floorWithNoImage")); //$NON-NLS-1$
		} else {
			final ImageView layerImageView = new ImageView();
			final File imgFile = new File(this.floor.getImgPath());
			final Image img = new Image(imgFile.toURI().toString());
			layerImageView.setImage(img);
			layerImageView.setPickOnBounds(true);
			layerImageView.setPreserveRatio(true);
			this.stackPaneCalques.getChildren().add(layerImageView);
			this.evacCanvas = new EvacuationsCanvas(this.floor, img.getWidth(), img.getHeight());
			this.stackPaneCalques.getChildren().add(this.evacCanvas);

			this.stackPaneCalques.setScaleX(this.floor.getZoomFactor());
			this.stackPaneCalques.setScaleY(this.floor.getZoomFactor());
			this.stackPaneCalques.setTranslateX(this.floor.getShiftX());
			this.stackPaneCalques.setTranslateY(this.floor.getShiftY());
			this.evacCanvas.visibleProperty().bind(this.radioEvac.selectedProperty());
		}
	}

	private void initTabHeader() {
		final Label titleLabel = new Label(Messages.getString("ProjectPane.floorname", this.floor.getNumber())); //$NON-NLS-1$
		titleLabel.setStyle("-fx-font-weight: bold;"); //$NON-NLS-1$
		final HBox tabHeader = new HBox(20);
		tabHeader.setAlignment(Pos.CENTER);
		initLayerRadioButtons();
		final HBox layersBox = new HBox(5);
		layersBox.getChildren().addAll(this.radioElec, this.radioAlim, this.radioEvac, this.radioNet);
		tabHeader.getChildren().addAll(titleLabel, layersBox);
		tabHeader.setPrefHeight(100);
		tabHeader.setPrefWidth(250);
		this.setGraphic(tabHeader);
		this.setText(""); //$NON-NLS-1$
	}

	private void initLayerRadioButtons() {
		this.headerToggleGroup = new ToggleGroup();
		this.radioElec.setToggleGroup(this.headerToggleGroup);
		this.radioAlim.setToggleGroup(this.headerToggleGroup);
		this.radioEvac.setToggleGroup(this.headerToggleGroup);
		this.radioNet.setToggleGroup(this.headerToggleGroup);
		final AppStateManager state = AppStateManager.getInstance();
		this.headerToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			final CustomLayerRadio rb = (CustomLayerRadio) newVal;
			state.setActiveNetworkLayer(rb.getType());
		});

		PreferencesManager.getInstance().addThemeChangeListener(this.radioElec);
		PreferencesManager.getInstance().addThemeChangeListener(this.radioAlim);
		PreferencesManager.getInstance().addThemeChangeListener(this.radioEvac);
		PreferencesManager.getInstance().addThemeChangeListener(this.radioNet);
	}

	public void enableHeaderRadioButtons() {
		setdisableHeaderRadioButtons(false);
	}

	public void disableHeaderRadioButtons() {
		setdisableHeaderRadioButtons(true);
	}

	private void setdisableHeaderRadioButtons(final boolean disable) {
		this.radioElec.setDisable(disable);
		this.radioAlim.setDisable(disable);
		this.radioEvac.setDisable(disable);
		this.radioNet.setDisable(disable);
	}

	public LayerType getCurrentLayer() {
		final CustomLayerRadio rb = (CustomLayerRadio) this.headerToggleGroup.getSelectedToggle();
		if (rb != null) {
			return rb.getType();
		}
		return null;
	}

	public Floor getFloor() {
		return this.floor;
	}
}