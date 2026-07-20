package org.targol.resoplan.ui.panels.floornetwork;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.ui.panels.floornetwork.properties.MetaNodeColumnPanel;
import org.targol.resoplan.ui.panels.floornetwork.properties.NodePropertiesPanel;
import org.targol.resoplan.ui.utils.events.NodePropertiesAskedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

public class FloorsNetworksSplitPane extends SplitPane {

	private Region propertiesPanel;
	private final FloorsNetworksTab mainTabPane;

	private static final FloorsService SVC_FLOORS = SpringContextHelper.getBean(FloorsService.class);
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);

	public FloorsNetworksSplitPane(final Project proj) {
		setOrientation(Orientation.HORIZONTAL);
		setDividerPositions(0.2);
		this.propertiesPanel = new Label("propriétés");
		this.mainTabPane = new FloorsNetworksTab(proj);
		getItems().addAll(this.propertiesPanel, this.mainTabPane);
		UiEventBus.register(this, NodePropertiesAskedEvent.NODE_PROPS_ANY, (event) -> changeProperties(event));
	}

	private void changeProperties(final NodePropertiesAskedEvent event) {
		final AbstractNode node = event.getNode();
		Region newPanel;
		if (node == null) {
			newPanel = new Label("propriétés");
		} else {
			if (node instanceof final Node realNode) {
				newPanel = new NodePropertiesPanel(realNode);
			} else {
				newPanel = new MetaNodeColumnPanel((MetaNode) node);
			}
		}
		getItems().set(0, newPanel);
		this.propertiesPanel = newPanel;
	}

}
