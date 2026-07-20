package org.targol.resoplan.ui.panels.floornetwork.layers;

import java.util.function.Consumer;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.ui.utils.GuiUtils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GraphicalNode extends AbstractGraphicalNode {

	public GraphicalNode(final Node node, final LayerType layer, final Color defaultColor,
			final Consumer<MouseEvent> onMergeRequested) {
		super(node, layer, defaultColor, onMergeRequested);
	}

	@Override
	public Node getNode() {
		return (Node) this.node;
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