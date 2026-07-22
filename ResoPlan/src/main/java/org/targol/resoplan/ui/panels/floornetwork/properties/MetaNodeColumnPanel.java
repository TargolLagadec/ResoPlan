package org.targol.resoplan.ui.panels.floornetwork.properties;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.panels.floornetwork.decorators.MetricVerticalRulerForColumnCanvas;
import org.targol.resoplan.ui.utils.events.NodePropertiesAskedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MetaNodeColumnPanel extends BorderPane {

	private static final ProjectsService SVC_PROJECTS = SpringContextHelper.getBean(ProjectsService.class);
	private final MetaNode node;
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$
	private final NumberFormat numberFormat = NumberFormat.getInstance();

	@FXML
	private TitledPane titlePane;
	@FXML
	private TextField posXTextField;
	@FXML
	private TextField posYTextField;

	private StackPane leftPane;
	private ColumnNodesPanel nodesPanel;
	private MetricVerticalRulerForColumnCanvas ruler;

	public MetaNodeColumnPanel(final MetaNode node) {
		this.node = SVC_NODES.getfullMetaNodeWithChidrenNodes(node).get();
		this.numberFormat.setMaximumFractionDigits(2);

		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/MetaNodeColumnPanel.fxml"), //$NON-NLS-1$
				this.bundle);
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de charger le FXML de MetaNodeColumnPanel", e); //$NON-NLS-1$
		}
		this.leftPane = new StackPane();
		this.ruler = new MetricVerticalRulerForColumnCanvas(this, SVC_PROJECTS.getOpenedProject().getHsp());
		this.nodesPanel = new ColumnNodesPanel(this, node);
		this.leftPane.getChildren().add(this.ruler);
		this.leftPane.getChildren().add(this.nodesPanel);

		setLeft(this.leftPane);
		setMaxWidth(400);
		setPrefWidth(200);
		UiEventBus.register(this, NodePropertiesAskedEvent.SUB_NODE_PROPS, evt -> displayProperties(evt));
	}

	private void displayProperties(NodePropertiesAskedEvent evt) {
		setCenter(new NodePropertiesPanel((Node) evt.getNode()));
	}

	@FXML
	private void initialize() {
		this.posXTextField.setText(this.numberFormat.format(this.node.getPosX()));
		this.posYTextField.setText(this.numberFormat.format(this.node.getPosY()));
	}

	public double getAvailableHeight() {
		return getHeight() - this.titlePane.getHeight();
	}

}