package org.targol.resoplan.ui.panels.floornetwork.layers;

import java.util.Set;
import java.util.function.Consumer;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.NodePlacementEvent;
import org.targol.resoplan.ui.utils.events.NodePropertiesAskedEvent;
import org.targol.resoplan.ui.utils.events.RefreshFloorLayerEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class AbstractGraphicalNode extends StackPane {

	public static final int NODE_SIZE_IN_CM = 10;

	protected static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);
	protected static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	protected static final int SCALE = SVC_PROJ.getOpenedProject().getPlansScale();

	protected final Project project;
	private Floor floor;
	protected AbstractNode node;
	protected final LayerType layer;
	protected final Color defaultColor;
	private double dragEndX;
	private double dragEndY;
	boolean dragging = false;

	public AbstractGraphicalNode(final Project project, final Floor floor, final AbstractNode node,
			final LayerType layer, final Color defaultColor, final Consumer<MouseEvent> onMergeRequested) {
		this.project = project;
		this.floor = floor;
		this.node = node;
		this.layer = layer;
		this.defaultColor = defaultColor;
		final double nodeSize = getNodeSize();
		setPrefSize(nodeSize, nodeSize);
		final Rectangle rect = new Rectangle(nodeSize, nodeSize, Color.DARKGRAY);
		rect.setArcHeight(nodeSize / 3);
		rect.setArcWidth(nodeSize / 3);
		getChildren().add(rect);
		final ImageView view = getImageView();
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(nodeSize);
		getChildren().add(view);
		setTranslateX(GuiUtils.centimetresTopixels(this.project, node.getPosX()) - nodeSize / 2);
		setTranslateY(GuiUtils.centimetresTopixels(this.project, node.getPosY()) - nodeSize / 2);
		initEvents(onMergeRequested);
	}

	public static double getNodeSize() {
		return SCALE * AbstractGraphicalNode.NODE_SIZE_IN_CM / 100;
	}

	public AbstractNode getNode() {
		return this.node;
	}

	protected abstract ImageView getImageView();

	private void initEvents(final Consumer<MouseEvent> onMergeRequested) {
		setOnContextMenuRequested(evt -> {
			evt.consume();
			UiEventBus.send(NodePropertiesAskedEvent.of(this.layer, this.node));
			UiEventBus.send(NodePlacementEvent.of(this.layer, this.floor, null));
		});
		final Delta dragDelta = new Delta();
		final double halfSize = getNodeSize() / 2;

		setOnMousePressed(evt -> {
			if (evt.isPrimaryButtonDown()) {
				// On stocke la distance entre le coin actuel du StackPane et le clic souris
				dragDelta.x = getTranslateX() - evt.getSceneX();
				dragDelta.y = getTranslateY() - evt.getSceneY();
				evt.consume(); // Évite que le parent (le Pane) ne reçoive le clic
			}
		});

		setOnMouseDragged(evt -> {
			if (evt.isPrimaryButtonDown()) {
				this.dragging = true;
				final double newX = evt.getSceneX() + dragDelta.x;
				final double newY = evt.getSceneY() + dragDelta.y;
				setTranslateX(newX);
				setTranslateY(newY);
				this.dragEndX = newX + halfSize;
				this.dragEndY = newY + halfSize;
				evt.consume();
			}
		});

		setOnMouseReleased(evt -> {
			if (this.dragging) {
				System.err.println("À la fin du drag : position écran = (" + this.dragEndX + "," + this.dragEndY + ")");

				final Set<Floor> impactedFloors = SVC_NODES.processLazyMove(this.node,
						GuiUtils.pixelToCentimetres(this.project, this.dragEndX),
						GuiUtils.pixelToCentimetres(this.project, this.dragEndY));

				Platform.runLater(() -> {
					for (final Floor floor : impactedFloors) {
						UiEventBus.send(RefreshFloorLayerEvent.of(this.layer, floor));
					}
				});
				this.dragging = false;
			}
			evt.consume();
		});
		setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				event.consume();
				// On notifie le calque que ce nœud précis a reçu un clic de souris
				onMergeRequested.accept(event);
			}
		});
	}

	// Classe interne utilitaire pour le drag
	private static class Delta {
		double x, y;
	}
}