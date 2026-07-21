package org.targol.resoplan.ui.panels;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.AjustEvent;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.ProjectUpdatedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FloorsAdjustmentPanel extends BorderPane {

	@FXML
	private Accordion accordion;
	@FXML
	private ScrollPane centerScrollPane;
	@FXML
	private Group imageContainer;
	@FXML
	private StackPane stackPaneCalques;

	private final Project project;
	private static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);

	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$
	private final Map<Floor, ImageView> layerImages = new HashMap<>();
	// Scale tracing
	private boolean tracingScale = false;
	private Canvas scaleTracingCanvas;
	private double startX, startY;
	private Color paintColor = Color.BLACK;

	public FloorsAdjustmentPanel(final Project proj) {
		this.project = SVC_PROJ.openProjectWithFloorsAndNodes(proj).get();
		UiEventBus.register(this, AjustEvent.FLOOR_UPDATED, evt -> onFloorChangeEvent(evt));
		UiEventBus.register(this, AjustEvent.VISUAL_CHANGED, evt -> onVisualChangeEvent(evt));
		UiEventBus.register(this, AjustEvent.SCALE_LINE_START_REQUIRED, evt -> onScaleStart(evt));

		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/FloorsAdjustmentPanel.fxml"), //$NON-NLS-1$
				this.bundle);
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de charger le FXML de FloorsAdjustmentPanel", e); //$NON-NLS-1$
		}
	}

	@FXML
	private void initialize() {
		final List<Floor> sortedFloors = this.project.getFloors().stream()
				.sorted(Comparator.comparingInt(Floor::getNumber)).toList();
		for (final Floor floor : sortedFloors) {
			if (!floor.isVirtual()) {
				final TitledPane titPane = new TitledPane();
				titPane.setPrefHeight(200);
				titPane.setMaxHeight(200);
				titPane.setText(Messages.getString("ProjectPane.floorname", floor.getNumber())); //$NON-NLS-1$
				final FloorPropertiesPanel propPanel = new FloorPropertiesPanel(floor);
//				propPanel.setPrefHeight(200);
//				propPanel.setMaxHeight(200);
				titPane.setContent(propPanel);
				this.accordion.getPanes().add(titPane);
				if (floor.getNumber() == 0) {
					this.accordion.setExpandedPane(titPane);
				}
				final ImageView layerImageView = new ImageView();
				layerImageView.setPickOnBounds(true);
				layerImageView.setPreserveRatio(true);
				updateImageView(floor, layerImageView);
				layerImageView.setVisible(true);
				this.layerImages.put(floor, layerImageView);
				this.stackPaneCalques.getChildren().add(layerImageView);
			}
		}
	}

	private void onFloorChangeEvent(final AjustEvent event) {
		final Floor floor = event.getFloor();
		final ImageView imgView = this.layerImages.get(floor);
		updateImageView(floor, imgView);
		this.stackPaneCalques.requestLayout();
	}

	private void onVisualChangeEvent(final AjustEvent event) {
		final Floor floor = event.getFloor();
		final ImageView imgView = this.layerImages.get(floor);
		imgView.setVisible(event.isVisible());
		imgView.setOpacity(event.getOpacity());
		this.paintColor = event.getPaintColor();
		updateImageView(floor, imgView);
		this.stackPaneCalques.requestLayout();
	}

	private void onScaleStart(final AjustEvent evt) {
		double maxHeight = 0.0d;
		double maxWidth = 0.0d;
		for (final ImageView img : this.layerImages.values()) {
			maxHeight = img.getImage().getHeight() > maxHeight ? img.getImage().getHeight() : maxHeight;
			maxWidth = img.getImage().getWidth() > maxWidth ? img.getImage().getWidth() : maxWidth;
		}
		this.scaleTracingCanvas = new Canvas(maxWidth, maxHeight);
		this.stackPaneCalques.getChildren().add(this.scaleTracingCanvas);
		this.scaleTracingCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
		this.scaleTracingCanvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::handleCanvasMove);
	}

	private void handleCanvasClick(final MouseEvent event) {
		final double x = event.getX();
		final double y = event.getY();
		if (!this.tracingScale) {
			this.startX = x;
			this.startY = y;
			this.tracingScale = true;
		} else {
			final int length = (int) Point2D.distance(this.startX, this.startY, x, y);
			this.project.setPlansScale(length);
			final ProjectsService service = SpringContextHelper.getBean(ProjectsService.class);
			service.updateProject(this.project);
			UiEventBus.send(ProjectUpdatedEvent.fireUpdate(this.project));
			UiEventBus.send(ProblemsUpdatedEvent.fireCheck(this.project));

			this.tracingScale = false;
			this.stackPaneCalques.getChildren().remove(this.scaleTracingCanvas);
			this.scaleTracingCanvas = null;
		}
		event.consume();
	}

	private void handleCanvasMove(final MouseEvent event) {
		if (!this.tracingScale) {
			event.consume();
			return;
		}
		final double x = event.getX();
		final double y = event.getY();
		final GraphicsContext gc = this.scaleTracingCanvas.getGraphicsContext2D();
		final Rectangle clear = calculateClearRectangle(x, y);
		gc.clearRect(clear.getX(), clear.getY(), clear.getWidth(), clear.getHeight());
		gc.setStroke(Color.AQUA);
		gc.setLineWidth(5);
		gc.strokeLine(this.startX, this.startY, x, y);
		event.consume();
	}

	private Rectangle calculateClearRectangle(final double curX, final double curY) {
		double x, y, w, h;
		if (curX > this.startX) {
			w = curX - this.startX + 30;
			x = this.startX - 15;
		} else {
			w = this.startX - curX + 30;
			x = curX - 15;
		}
		if (curY > this.startY) {
			h = curY - this.startY + 30;
			y = this.startY - 15;
		} else {
			h = this.startY - curY + 30;
			y = curY - 15;
		}
		return new Rectangle(x, y, w, h);
	}

	private void updateImageView(final Floor floor, final ImageView imgView) {
		final Image img = loadFloorImage(floor);
		imgView.setImage(img);
		// on stocke l'image sans transformations.
		imgView.setUserData(img);
		imgView.setScaleX(floor.getZoomFactor());
		imgView.setScaleY(floor.getZoomFactor());
		imgView.setTranslateX(floor.getShiftX());
		imgView.setTranslateY(floor.getShiftY());
	}

	private Image loadFloorImage(final Floor floor) {
		if (floor == null || floor.getImgPath() == null) {
			return null;
		}
		if (Color.BLACK.equals(this.paintColor)) {
			return new Image(new File(floor.getImgPath()).toURI().toString());
		}
		return GuiUtils.colorizeImage(new Image(new File(floor.getImgPath()).toURI().toString()), this.paintColor);
	}
}
