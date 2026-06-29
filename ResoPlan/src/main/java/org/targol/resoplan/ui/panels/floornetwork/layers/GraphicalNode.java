package org.targol.resoplan.ui.panels.floornetwork.layers;

import java.util.function.Consumer;

import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.ui.utils.GuiUtils;

import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GraphicalNode extends AbstractGraphicalNode {

	public GraphicalNode(final Node node, final Color defaultColor, final Consumer<MouseEvent> onMergeRequested) {
		super(node, defaultColor);
		initEvents(onMergeRequested);
	}

	private void initEvents(final Consumer<MouseEvent> onMergeRequested) {
		setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY) {
				event.consume();
				// On notifie le calque que ce nœud précis a reçu un clic de souris
				onMergeRequested.accept(event);
			}
		});
	}

	@Override
	public Node getNode() {
		return (Node) this.node;
	}

	@Override
	protected ContextMenu createContextMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ImageView getImageView() {
		final Node realNode = (Node) this.node;
		final Image img = GuiUtils.getCatalogIcon(realNode.getModel().getImgName(), this.defaultColor);
		final ImageView view = new ImageView(img);
		if (NodeCross.GOES_UP.equals(realNode.getNodeCross())) {
			view.setScaleY(-1);
		}
		return view;
	}
}