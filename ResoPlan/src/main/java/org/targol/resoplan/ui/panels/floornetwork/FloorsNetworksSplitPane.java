package org.targol.resoplan.ui.panels.floornetwork;

import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.ui.utils.events.NodePropertiesAskedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;

public class FloorsNetworksSplitPane extends SplitPane {

	private final Project proj;
	private Region propertiesPanel;
	private final FloorsNetworksTab mainTabPane;

	public FloorsNetworksSplitPane(final Project proj) {
		this.proj = proj;
		this.setOrientation(Orientation.HORIZONTAL);
		this.setDividerPositions(0.2);
		this.propertiesPanel = new Label("propriétés");
		this.mainTabPane = new FloorsNetworksTab(proj);
		this.getItems().addAll(this.propertiesPanel, this.mainTabPane);
		UiEventBus.register(NodePropertiesAskedEvent.NODE_PROPS_ANY, (event) -> changeProperties(event));
	}

	private void changeProperties(final NodePropertiesAskedEvent event) {
		final AbstractNode node = event.getNode();
		Region newPanel;
		if (node == null) {
			System.out.println("Demande proriétés : null");
			newPanel = new Label("propriétés");
		} else if (node instanceof final Node realNode) {
			System.out.println("Demande proriétés : " + realNode.getModel().getName());
			newPanel = new Label(realNode.getModel().getName());
		} else {
			System.out.println("Demande proriétés : Colonne");
			newPanel = new Label("Colonne");
		}
		this.getItems().set(0, newPanel);
		this.propertiesPanel = newPanel;
	}

}
