package org.targol.resoplan.ui.panels.floornetwork.layers;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class AbstractGraphicalNode extends StackPane {

	public static final int NODE_SIZE_IN_CM = 20;

	protected static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);
	protected static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	protected static final int SCALE = SVC_PROJ.getOpenedProject().getPlansScale();

	protected AbstractNode node;
	protected final Color defaultColor;

	public AbstractGraphicalNode(final AbstractNode node, Color defaultColor) {
		this.node = node;
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
		setTranslateX(node.getPosX() - nodeSize / 2);
		setTranslateY(node.getPosY() - nodeSize / 2);
		initEvents();
	}

	public static double getNodeSize() {
		return SCALE * GraphicalNode.NODE_SIZE_IN_CM / 100;
	}

	public AbstractNode getNode() {
		return this.node;
	}

	protected abstract AbstractNode saveNodeAfterDragnDrop();

	protected abstract ContextMenu createContextMenu();

	protected abstract ImageView getImageView();

	private void initEvents() {
		setOnContextMenuRequested(evt -> {
			final ContextMenu menu = createContextMenu();
			if (menu != null) {
				menu.show(this, evt.getScreenX(), evt.getScreenY());
			}
			evt.consume();
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
				final double newX = evt.getSceneX() + dragDelta.x;
				final double newY = evt.getSceneY() + dragDelta.y;

				setTranslateX(newX);
				setTranslateY(newY);

				this.node.setPosX((int) (newX + halfSize));
				this.node.setPosY((int) (newY + halfSize));
				evt.consume();
			}
		});

		setOnMouseReleased(evt -> {
			this.node = saveNodeAfterDragnDrop();
			evt.consume();
		});
	}

	// Classe interne utilitaire pour le drag
	private static class Delta {
		double x, y;
	}
}