package org.targol.resoplan.ui.panels.floornetwork;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.ui.utils.AppStateManager;
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

	private final Project proj;
	private static final FloorsService SVC_FLOORS = SpringContextHelper.getBean(FloorsService.class);

	public FloorsNetworksSplitPane(final Project proj) {
		this.proj = proj;
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
			System.out.println("Demande proriétés : null");
			newPanel = new Label("propriétés");
		} else {
			final Floor floor = SVC_FLOORS
					.findByIdWithNodes(AppStateManager.getInstance().nodeIdToFloorId().get().get(node.getId())).get();
			if (node instanceof final Node realNode) {
				System.out.println("Demande proriétés : " + realNode.getModel().getName());
				newPanel = new NodePropertiesPanel(floor, realNode);
			} else {
				System.out.println("Demande propriétés : Colonne");
				newPanel = new Label("Colonne");
			}
		}
		getItems().set(0, newPanel);
		this.propertiesPanel = newPanel;
	}

}
