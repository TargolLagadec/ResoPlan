package org.targol.resoplan.ui.panels.floornetwork;

import java.io.File;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.ui.components.CustomLayerRadio;
import org.targol.resoplan.ui.panels.floornetwork.decorators.MetricGridCanvas;
import org.targol.resoplan.ui.panels.floornetwork.decorators.MetricHorizontalRulerCanvas;
import org.targol.resoplan.ui.panels.floornetwork.decorators.MetricVerticalRulerCanvas;
import org.targol.resoplan.ui.panels.floornetwork.layers.evac.EvacuationsLayer;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.ChangeLayerEvent;
import org.targol.resoplan.ui.utils.events.GenericActionEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.PreferencesHelper;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LayeredFloorTab extends Tab {

	private static final double SCALE_FACTOR = 0.1;
	private static final FloorsService SVC_FLOORS = SpringContextHelper.getBean(FloorsService.class);

	// parent
	private final FloorsNetworksTab parentController;
	// Custom checkBoxes in tab header to display or not different networks layers
	private final CustomLayerRadio radioElec = new CustomLayerRadio(LayerType.ELEC);
	private final CustomLayerRadio radioAlim = new CustomLayerRadio(LayerType.WATER_ALIM);
	private final CustomLayerRadio radioEvac = new CustomLayerRadio(LayerType.WATER_EVAC);
	private final CustomLayerRadio radioNet = new CustomLayerRadio(LayerType.NET);
	// content
	private ToggleGroup headerToggleGroup;
	private ScrollPane centerScrollPane;
	private Pane mainNetworkPane;
	private MetricGridCanvas grid;
	private MetricHorizontalRulerCanvas horizRuler;
	private MetricVerticalRulerCanvas verticRuler;
	private EvacuationsLayer evacLayer;

	private final Project project;
	private final Floor floor;

	public LayeredFloorTab(final FloorsNetworksTab parentController, final Project project, final Floor floor,
			final boolean active) {
		this.parentController = parentController;
		this.project = project;
		// Attention, on remplace le floor lazy loadé avec celui contenant ses noeuds !
		this.floor = SVC_FLOORS.reloadWithNodes(floor).get();
		initTabHeader();
		initContent();
		setdisableHeaderRadioButtons(!active);
	}

	private void initContent() {
		this.centerScrollPane = new ScrollPane();

		this.horizRuler = new MetricHorizontalRulerCanvas();
		this.verticRuler = new MetricVerticalRulerCanvas();
		GridPane layoutGrid = new GridPane();
		layoutGrid.add(this.horizRuler, 1, 0);
		// Règle gauche (index 0,1)
		layoutGrid.add(this.verticRuler, 0, 1);
		// Plan au centre (index 1,1)
		layoutGrid.add(this.centerScrollPane, 1, 1);
		GridPane.setHgrow(this.centerScrollPane, Priority.ALWAYS);
		GridPane.setVgrow(this.centerScrollPane, Priority.ALWAYS);
		setContent(layoutGrid);
		this.horizRuler.widthProperty().bind(this.centerScrollPane.widthProperty());
		this.horizRuler.setHeight(20);
		this.verticRuler.heightProperty().bind(this.centerScrollPane.heightProperty());
		this.verticRuler.setWidth(20);

		final Bounds projectBounds = GuiUtils.calculateUniversalProjectBounds(this.project);

		this.grid = new MetricGridCanvas();
		this.grid.setWidth(projectBounds.getWidth());
		this.grid.setHeight(projectBounds.getHeight());
		this.grid.mouseTransparentProperty().set(true);

		this.mainNetworkPane = new StackPane();
		this.mainNetworkPane.setPrefSize(projectBounds.getWidth(), projectBounds.getHeight());
		this.mainNetworkPane.setMinSize(projectBounds.getWidth(), projectBounds.getHeight());
		this.mainNetworkPane.setMaxSize(projectBounds.getWidth(), projectBounds.getHeight());

		final Group zoomGroup = new Group(this.mainNetworkPane);
		this.centerScrollPane.setContent(zoomGroup);
		this.centerScrollPane.addEventFilter(ScrollEvent.SCROLL, event -> onScrollEvent(event));

		final ImageView layerImageView = new ImageView();
		final File imgFile = new File(this.floor.getImgPath());
		Image img = new Image(imgFile.toURI().toString());
		if (this.floor.isVirtual()) {
			img = GuiUtils.changeColorInImage(img, Color.BLACK, Color.BURLYWOOD);
		}
		layerImageView.setImage(img);
		layerImageView.setPickOnBounds(true);
		layerImageView.setPreserveRatio(true);
		layerImageView.setScaleX(this.floor.getZoomFactor());
		layerImageView.setScaleY(this.floor.getZoomFactor());
		layerImageView.setTranslateX(this.floor.getShiftX());
		layerImageView.setTranslateY(this.floor.getShiftY());

		this.mainNetworkPane.getChildren().add(layerImageView);

		// Ajout du calque des évacuations
		this.evacLayer = new EvacuationsLayer(this.project, this.floor);
		this.mainNetworkPane.getChildren().add(this.evacLayer);
		this.evacLayer.setPrefSize(projectBounds.getWidth(), projectBounds.getHeight());
		this.evacLayer.setMinSize(projectBounds.getWidth(), projectBounds.getHeight());
		this.evacLayer.setMaxSize(projectBounds.getWidth(), projectBounds.getHeight());
		this.evacLayer.visibleProperty().bind(this.radioEvac.selectedProperty());
		// TODO À terme, rajouter les autres calques.

		this.mainNetworkPane.getChildren().add(this.grid);
		setupRepaintListeners();
		triggerRepaint();
		UiEventBus.register(this.parentController, ChangeLayerEvent.CHANGE_LAYER, evt -> layerChanged(evt));
		UiEventBus.register(this.parentController, GenericActionEvent.PREF_CHANGE, evt -> triggerRepaint());
	}

	private void setupRepaintListeners() {
		// Écoute du Zoom
		this.mainNetworkPane.scaleXProperty().addListener((obs, old, newVal) -> triggerRepaint());

		// Écoute du Déplacement (Scroll)
		this.centerScrollPane.hvalueProperty().addListener((obs, old, newVal) -> repaintRulers());
		this.centerScrollPane.vvalueProperty().addListener((obs, old, newVal) -> repaintRulers());

		// Écoute du redimensionnement des règles
		this.horizRuler.widthProperty().addListener((obs, old, newVal) -> repaintRulers());
		this.verticRuler.heightProperty().addListener((obs, old, newVal) -> repaintRulers());
	}

	private void triggerRepaint() {
		boolean gridActive = PreferencesHelper.getBoolPreference(PreferencesHelper.PREF_SHOW_GRID);
		if (gridActive) {
			this.grid.repaint(this.centerScrollPane, this.mainNetworkPane);
		} else {
			this.grid.clear();
		}
		boolean rulersActive = PreferencesHelper.getBoolPreference(PreferencesHelper.PREF_SHOW_RULERS);
		if (rulersActive) {
			repaintRulers();
		} else {
			clearRulers();
		}
	}

	private void repaintRulers() {
		if (!PreferencesHelper.getBoolPreference(PreferencesHelper.PREF_SHOW_RULERS)) {
			return;
		}
		this.horizRuler.repaint(this.centerScrollPane, this.mainNetworkPane);
		this.verticRuler.repaint(this.centerScrollPane, this.mainNetworkPane);
	}

	private void clearRulers() {
		this.horizRuler.clear();
		this.verticRuler.clear();
	}

	private void onScrollEvent(final ScrollEvent event) {
		if (event.isControlDown()) {
			event.consume();
			final double deltaY = event.getDeltaY();
			if (Math.abs(deltaY) < 0.01) {
				return;
			}
			final int direction = deltaY > 0 ? 1 : -1;
			final double factor = direction > 0 ? 1 + SCALE_FACTOR : 1 - SCALE_FACTOR;
			final double nextScale = this.mainNetworkPane.getScaleX() * factor;
			if (nextScale < 0.1 || nextScale > 10.0) {
				return;
			}
			final Point2D mouseInContent = this.centerScrollPane.getContent().sceneToLocal(event.getSceneX(),
					event.getSceneY());
			this.parentController.syncZoom(factor, mouseInContent.getX(), mouseInContent.getY());
		}

	}

	public ScrollPane getCenterScrollPane() {
		return this.centerScrollPane;
	}

	public Pane getMainNetworkPane() {
		return this.mainNetworkPane;
	}

	private void initTabHeader() {
		Label titleLabel;
		if (this.floor.getNumber() == -1 && this.floor.isVirtual()) {
			final String title = Messages.getString("Dialog.project.generate.basement.label"); //$NON-NLS-1$
			titleLabel = new Label(title);
			titleLabel.setStyle("-fx-font-weight: bold; -fx-font-style: italic;"); //$NON-NLS-1$
		} else if (this.floor.isVirtual()) {
			final String title = Messages.getString("Dialog.project.generate.attic.label"); //$NON-NLS-1$
			titleLabel = new Label(title);
			titleLabel.setStyle("-fx-font-weight: bold; -fx-font-style: italic;"); //$NON-NLS-1$
		} else {
			final String title = Messages.getString("ProjectPane.floorname", this.floor.getNumber()); //$NON-NLS-1$
			titleLabel = new Label(title);
			titleLabel.setStyle("-fx-font-weight: bold;"); //$NON-NLS-1$
		}
		final HBox tabHeader = new HBox(20);
		tabHeader.setAlignment(Pos.CENTER);
		initLayerRadioButtons();
		final HBox layersBox = new HBox(5);
		layersBox.getChildren().addAll(this.radioElec, this.radioAlim, this.radioEvac, this.radioNet);
		tabHeader.getChildren().addAll(titleLabel, layersBox);
		tabHeader.setPrefHeight(100);
		tabHeader.setPrefWidth(350);
		tabHeader.setMinWidth(350);
		setGraphic(tabHeader);
		setText(""); //$NON-NLS-1$
	}

	private void initLayerRadioButtons() {
		this.headerToggleGroup = new ToggleGroup();
		this.radioElec.setToggleGroup(this.headerToggleGroup);
		this.radioAlim.setToggleGroup(this.headerToggleGroup);
		this.radioEvac.setToggleGroup(this.headerToggleGroup);
		this.radioNet.setToggleGroup(this.headerToggleGroup);
		this.headerToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				final CustomLayerRadio rb = (CustomLayerRadio) newVal;
				UiEventBus.send(ChangeLayerEvent.of(rb.getType()));
			}
		});
	}

	private void layerChanged(ChangeLayerEvent event) {
		LayerType newLayer = event.getLayer();
		if (newLayer == null) {
			this.headerToggleGroup.selectToggle(null);
			return;
		}
		final CustomLayerRadio targetRadio = switch (newLayer) {
		case ELEC -> this.radioElec;
		case WATER_ALIM -> this.radioAlim;
		case WATER_EVAC -> this.radioEvac;
		case NET -> this.radioNet;
		};
		if (this.headerToggleGroup.getSelectedToggle() != targetRadio) {
			this.headerToggleGroup.selectToggle(targetRadio);
		}
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