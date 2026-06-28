package org.targol.resoplan.ui.panels.floornetwork.layers;

import java.util.function.Consumer;

import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GraphicalNode extends StackPane {

	private static final int NODE_SIZE_IN_CM = 20;
	private static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	private static final int SCALE = SVC_PROJ.getOpenedProject().getPlansScale();

	private final Node node;
	private final Color defaultColor = Color.CHOCOLATE;

	public GraphicalNode(final Node node, final Consumer<MouseEvent> onMergeRequested) {
		this.node = node;
		final double nodeSize = getNodeSize();
		setPrefSize(nodeSize, nodeSize);
		final Rectangle rect = new Rectangle(nodeSize, nodeSize, Color.DARKGRAY);
		rect.setArcHeight(nodeSize / 3);
		rect.setArcWidth(nodeSize / 3);
		this.getChildren().add(rect);
		final Image img = GuiUtils.getCatalogIcon(this.node.getModel().getImgName(), this.defaultColor);
		final ImageView view = new ImageView(img);
		view.setPreserveRatio(true);
		if (NodeCross.GOES_UP.equals(node.getNodeCross())) {
			view.setScaleY(-1);
		}
		view.fitWidthProperty().set(nodeSize);
		this.getChildren().add(view);
		this.setTranslateX(node.getPosX() - nodeSize / 2);
		this.setTranslateY(node.getPosY() - nodeSize / 2);
		initEvents(onMergeRequested);
	}

	public static double getNodeSize() {
		return SCALE * NODE_SIZE_IN_CM / 100;
	}

	private void initEvents(final Consumer<MouseEvent> onMergeRequested) {
		this.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				event.consume();
				// On notifie le calque que ce nœud précis a reçu un clic de souris
				onMergeRequested.accept(event);
			}
		});

		this.setOnContextMenuRequested(evt -> {
			final ContextMenu menu = createContextMenu();
			if (menu != null) {
				menu.show(this, evt.getScreenX(), evt.getScreenY());
			}
			evt.consume();
		});

		final Delta dragDelta = new Delta();
		final double halfSize = getNodeSize() / 2;

		this.setOnMousePressed(evt -> {
			if (evt.isPrimaryButtonDown()) {
				// On stocke la distance entre le coin actuel du StackPane et le clic souris
				dragDelta.x = this.getTranslateX() - evt.getSceneX();
				dragDelta.y = this.getTranslateY() - evt.getSceneY();
				evt.consume(); // Évite que le parent (le Pane) ne reçoive le clic
			}
		});

		this.setOnMouseDragged(evt -> {
			if (evt.isPrimaryButtonDown()) {
				final double newX = evt.getSceneX() + dragDelta.x;
				final double newY = evt.getSceneY() + dragDelta.y;

				this.setTranslateX(newX);
				this.setTranslateY(newY);

				this.node.setPosX((int) (newX + halfSize));
				this.node.setPosY((int) (newY + halfSize));
				SVC_NODES.save(this.node);
				evt.consume();
			}
		});
	}

	private ContextMenu createContextMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getNode() {
		return this.node;
	}

	// Classe interne utilitaire pour le drag
	private static class Delta {
		double x, y;
	}
}