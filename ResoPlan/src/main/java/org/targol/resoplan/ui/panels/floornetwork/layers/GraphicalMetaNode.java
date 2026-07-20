package org.targol.resoplan.ui.panels.floornetwork.layers;

import java.util.List;
import java.util.function.Consumer;

import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.ui.utils.GuiUtils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GraphicalMetaNode extends AbstractGraphicalNode {

	public GraphicalMetaNode(final MetaNode node, final LayerType layer, final Color defaultColor,
			final Consumer<MouseEvent> onMergeRequested) {
		super(node, layer, defaultColor, onMergeRequested);
	}

	@Override
	public MetaNode getNode() {
		return (MetaNode) this.node;
	}

	public List<Node> getInerNodes() {
		return getNode().getNodes();
	}

	@Override
	protected ImageView getImageView() {
		final Image img = GuiUtils.getCatalogIcon("meta", this.defaultColor); //$NON-NLS-1$
		return new ImageView(img);
	}
}