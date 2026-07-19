package org.targol.resoplan.ui.panels.floornetwork.properties;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MetaNodeColumnPanelController extends BorderPane {

	private final Floor floor;
	private final MetaNode node;
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n.messages", Locale.getDefault()); //$NON-NLS-1$
	private final NumberFormat numberFormat = NumberFormat.getInstance();

	@FXML
	private TextField posXTextField;
	@FXML
	private TextField posYTextField;

	public MetaNodeColumnPanelController(final Floor floor, final MetaNode node) {
		this.floor = floor;
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

	}

	@FXML
	private void initialize() {
		this.posXTextField.setText(this.numberFormat.format(this.node.getPosX()));
		this.posYTextField.setText(this.numberFormat.format(this.node.getPosY()));
	}

}