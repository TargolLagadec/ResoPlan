package org.targol.resoplan.ui.panels.floornetwork.properties;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Hook;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.ui.utils.events.NodeMoveEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NodePropertiesPanel extends GridPane {

	@FXML
	private TextField modelTextField;
	@FXML
	private TextField posXTextField;
	@FXML
	private TextField posYTextField;
	@FXML
	private TextField posZTextField;
	@FXML
	private TextField floorTextField;
	@FXML
	private TextArea layersTextField;
	@FXML
	private TextField nodeCrossTextField;
	@FXML
	private TextArea hooksTextField;
	@FXML
	private Button saveButton;
	@FXML
	private Button deleteButton;

	private Node node;
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$
	private final NumberFormat numberFormat = NumberFormat.getInstance();

	public NodePropertiesPanel(Node node) {
		this.node = SVC_NODES.getfullNodeWithHooks(node).get();
		this.numberFormat.setMaximumFractionDigits(2);
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/panels/NodePropertiesPanel.fxml"), //$NON-NLS-1$
				this.bundle);
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (final IOException e) {
			throw new RuntimeException("Impossible de charger le FXML de FloorPropertiesPanel", e); //$NON-NLS-1$
		}
		UiEventBus.register(this, NodeMoveEvent.NODE_MOVE, (event) -> updateNode(event));
	}

	private void updateNode(NodeMoveEvent event) {
		AbstractNode node = event.getNode();
		if (node instanceof Node) {
			this.node = SVC_NODES.getfullNodeWithHooks((Node) node).get();
		}
		initialize();
	}

	@FXML
	public void initialize() {
		this.modelTextField.setText(this.node.getModel().getName());
		this.posXTextField.setText(this.numberFormat.format(this.node.getPosX()));
		this.posYTextField.setText(this.numberFormat.format(this.node.getPosY()));
		this.posZTextField.setText(this.numberFormat.format(this.node.getPosZ()));
		this.posZTextField.setDisable(!NodeCross.NONE.equals(this.node.getNodeCross()));
		this.layersTextField.setText(buildLayersLabel());
		this.nodeCrossTextField.setText(this.node.getNodeCross().getLabel());
		this.hooksTextField.setText(buildHooksLabel());
	}

	private String buildLayersLabel() {
		String ret = ""; //$NON-NLS-1$
		boolean first = true;
		for (LayerType layer : this.node.getActiveLayers()) {
			if (first) {
				first = false;
			} else {
				ret = ret.concat("\n"); //$NON-NLS-1$
			}
			ret = ret.concat(layer.getLabel());
		}
		return ret;
	}

	private String buildHooksLabel() {
		String ret = ""; //$NON-NLS-1$
		boolean first = true;
		for (Hook hook : this.node.getHooks()) {
			if (first) {
				first = false;
			} else {
				ret = ret.concat("\n"); //$NON-NLS-1$
			}
			ret = ret.concat(hook.getHookType().getLabel());
			if (hook.isLinked()) {
				ret = ret.concat(" ").concat(Messages.getString("NodePropertiesPanel.hooks.linked")); //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				ret = ret.concat(" ").concat(Messages.getString("NodePropertiesPanel.hooks.unlinked")); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return ret;
	}
}
