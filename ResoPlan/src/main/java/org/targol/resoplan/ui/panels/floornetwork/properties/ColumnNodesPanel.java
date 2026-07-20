package org.targol.resoplan.ui.panels.floornetwork.properties;

import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.ui.panels.floornetwork.layers.GraphicalMetaNode;
import org.targol.resoplan.ui.panels.floornetwork.layers.GraphicalNode;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ColumnNodesPanel extends Pane {

	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);

	private final Color drawingColor = Color.CHOCOLATE;

	final MetaNodeColumnPanel parent;
	final MetaNode meta;

	public ColumnNodesPanel(final MetaNodeColumnPanel parent, final MetaNode meta) {
		this.parent = parent;
		this.meta = SVC_NODES.getfullMetaNodeWithChidrenNodes(meta).get();
		setPickOnBounds(true);
		parent.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null && newVal.doubleValue() > 0) {
				setHeight(parent.getAvailableHeight());
				repaint();
			}
		});

		setStyle("-fx-background-color: transparent;"); //$NON-NLS-1$
//		UiEventBus.register(this, NodePlacementEvent.WATER_EVAC, evt -> onNodePlacementEvent(evt));
//		UiEventBus.register(this, LinkTracingEvent.WATER_EVAC, evt -> onLinkTracingEvent(evt));
//		UiEventBus.register(this, LinkedNodePlacementEvent.WATER_EVAC, evt -> onLinkedNodePlacementEvent(evt));
//		UiEventBus.register(this, RefreshFloorLayerEvent.WATER_EVAC, evt -> refresh(evt));
	}

	private void repaint() {
		getChildren().clear();
		for (final Node node : this.meta.getNodes()) {
			drawGraphicalNode(node);
		}
	}

	private void drawGraphicalNode(final Node node) {
		final PropertiesGraphicalNode gn = new PropertiesGraphicalNode(this.parent, node, this.drawingColor);
		getChildren().add(gn);
		requestLayout();
	}

	private void startDrawingLinkFromGraphNode(final GraphicalNode graphicNode) {
		// TODO Auto-generated method stub
	}

	private void startDrawingLinkFromGraphicalMetaNode(final GraphicalMetaNode graphicNode) {
		// TODO Auto-generated method stub
	}

	private void drawPipe(final double pixelStartX, final double pixelStartY, final double pixelEndX,
			final double pixelEndY) {
		// TODO Auto-generated method stub
	}

}
