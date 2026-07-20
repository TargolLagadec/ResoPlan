package org.targol.resoplan.ui.panels.floornetwork.properties;

import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.ProjectsService;
import org.targol.resoplan.ui.panels.floornetwork.decorators.MetricVerticalRulerForColumnCanvas;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.NodePropertiesAskedEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PropertiesGraphicalNode extends StackPane {

	public static final int NODE_SIZE_IN_CM = 10;

	private static final ProjectsService SVC_PROJ = SpringContextHelper.getBean(ProjectsService.class);
	private static final int SCALE = SVC_PROJ.getOpenedProject().getPlansScale();

	private final Project project;
	final MetaNodeColumnPanel parent;
	private Node node;
	private final Color defaultColor;

	public PropertiesGraphicalNode(MetaNodeColumnPanel parent, final Node node, final Color defaultColor) {
		this.parent = parent;
		this.node = node;
		this.defaultColor = defaultColor;
		this.project = SVC_PROJ.getOpenedProject();
		final double nodeSize = getNodeSize();
		setPrefSize(nodeSize, nodeSize);
		final Rectangle rect = new Rectangle(nodeSize, nodeSize, Color.DARKGRAY);
		rect.setArcHeight(nodeSize / 3);
		rect.setArcWidth(nodeSize / 3);
		getChildren().add(rect);
		final ImageView view = getImageView();
		view.setPreserveRatio(true);
		view.fitWidthProperty().set(nodeSize);
		getChildren().add(view);
		setTranslateX(20);
		double available = parent.getAvailableHeight() - MetricVerticalRulerForColumnCanvas.TOP_BOTTOM_MARGIN * 2;
		double pxPerMeter = available / this.project.getHsp();

		double posY = parent.getAvailableHeight() - MetricVerticalRulerForColumnCanvas.TOP_BOTTOM_MARGIN
				- node.getPosZ() * pxPerMeter - getNodeSize() / 2;
		setTranslateY(posY);
		initEvents();
	}

	public static double getNodeSize() {
		return SCALE * PropertiesGraphicalNode.NODE_SIZE_IN_CM / 100;
	}

	public Node getNode() {
		return this.node;
	}

	private ImageView getImageView() {
		final Node realNode = this.node;
		final Image img = GuiUtils.getCatalogIcon(realNode.getModel().getImgName(), this.defaultColor);
		final ImageView view = new ImageView(img);
		if (NodeCross.GOES_UP.equals(realNode.getNodeCross())) {
			view.setScaleY(-1);
		}
		return view;
	}

	private void initEvents() {
		setOnContextMenuRequested(evt -> {
			evt.consume();
			UiEventBus.send(NodePropertiesAskedEvent.showSubNodeProps(this.node));
		});
	}
}