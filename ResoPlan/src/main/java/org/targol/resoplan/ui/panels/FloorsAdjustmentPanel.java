package org.targol.resoplan.ui.panels;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FloorsAdjustmentPanel extends BorderPane {

	@FXML
	private Accordion accordion;
	@FXML
	private ScrollPane centerScrollPane;
	@FXML
	private Group imageContainer;
	@FXML
	private StackPane stackPaneCalques;

	private final Project proj;
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$

	public FloorsAdjustmentPanel(final Project proj) {
		this.proj = proj;
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/FloorsAdjustmentPanel.fxml"), //$NON-NLS-1$
				this.bundle);
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de charger le FXML de FloorPropertiesPanel", e); //$NON-NLS-1$
		}
	}

	@FXML
	private void initialize() {
		final List<Floor> sortedFloors = this.proj.getFloors().stream()
				.sorted(Comparator.comparingInt(Floor::getNumber)).toList();
		for (final Floor floor : sortedFloors) {
			final TitledPane titPane = new TitledPane();
			titPane.setText(Messages.getString("ProjectPane.floorname", floor.getNumber())); //$NON-NLS-1$
			final FloorPropertiesPanel propPanel = new FloorPropertiesPanel(floor);
			titPane.setContent(propPanel);
			this.accordion.getPanes().add(titPane);
			if (floor.getNumber() == 0) {
				this.accordion.setExpandedPane(titPane);
			}
			final ImageView layerImageView = new ImageView();
			layerImageView.setPickOnBounds(true);
			layerImageView.setPreserveRatio(true);
			layerImageView.setVisible(propPanel.getVisibleCheck().isSelected());
			layerImageView.imageProperty().bind(propPanel.imageProperty());
			layerImageView.visibleProperty().bind(propPanel.getVisibleCheck().selectedProperty());
			layerImageView.opacityProperty().bind(propPanel.getTransparencySlider().valueProperty());
			layerImageView.scaleXProperty().bind(propPanel.zoomProperty());
			layerImageView.scaleYProperty().bind(propPanel.zoomProperty());
			layerImageView.translateXProperty().bind(propPanel.shiftXProperty());
			layerImageView.translateYProperty().bind(propPanel.shiftYProperty());
			layerImageView.visibleProperty().bind(propPanel.getVisibleCheck().selectedProperty());
			this.stackPaneCalques.getChildren().add(layerImageView);
		}
	}
}
