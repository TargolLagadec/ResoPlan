package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.INodeContainer;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.ui.panels.floornetwork.layers.AbstractGraphicalNode;
import org.targol.resoplan.ui.panels.floornetwork.layers.GraphicalMetaNode;
import org.targol.resoplan.ui.panels.floornetwork.layers.GraphicalNode;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.ui.utils.GuiUtils;
import org.targol.resoplan.ui.utils.events.LinkTracingEvent;
import org.targol.resoplan.ui.utils.events.LinkedNodePlacementEvent;
import org.targol.resoplan.ui.utils.events.NodePlacementEvent;
import org.targol.resoplan.ui.utils.events.ProblemsUpdatedEvent;
import org.targol.resoplan.ui.utils.events.RefreshFloorLayerEvent;
import org.targol.resoplan.ui.utils.events.UiEventBus;
import org.targol.resoplan.utils.SpringContextHelper;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class EvacuationsLayer extends Pane {

	private static final FloorsService SVC_FLOORS = SpringContextHelper.getBean(FloorsService.class);
	private static final NodesService SVC_NODES = SpringContextHelper.getBean(NodesService.class);
	private static final NodeModelsService SVC_NODEMODELS = SpringContextHelper.getBean(NodeModelsService.class);

	private final Color drawingColor = Color.CHOCOLATE;
	private boolean isDrawingTube = false;
	private double startX, startY;

	private NodeModel currentNodeModel;
	private HookType currentHookType;
	private NodeCross direction;
	private final Project project;
	private Floor floor;

	public EvacuationsLayer(final Project project, final Floor floor) {
		this.project = project;
		// Attention, on remplace le floor lazy loadé (ou avec des floors osolètes) avec
		// celui contenant ses noeuds !
		this.floor = SVC_FLOORS.reloadWithNodes(floor).get();
		for (final AbstractNode node : this.floor.getNodes()) {
			if (node.getActiveLayers().contains(LayerType.WATER_EVAC)) {
				drawGraphicalNode(node);
			}
		}
		setPickOnBounds(true);
		setStyle("-fx-background-color: transparent;"); //$NON-NLS-1$
		addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleCanvasClick);
		UiEventBus.register(NodePlacementEvent.WATER_EVAC, evt -> onNodePlacementEvent(evt));
		UiEventBus.register(LinkTracingEvent.WATER_EVAC, evt -> onLinkTracingEvent(evt));
		UiEventBus.register(LinkedNodePlacementEvent.WATER_EVAC, evt -> onLinkedNodePlacementEvent(evt));
		UiEventBus.register(RefreshFloorLayerEvent.WATER_EVAC, evt -> refresh(evt));
	}

	private void refresh(final RefreshFloorLayerEvent evt) {
		final Floor targetFloor = SVC_FLOORS.reloadWithNodes(evt.getFloor()).get();
		if (!targetFloor.equals(this.floor)) {
			evt.consume();
			return;
		}
		this.floor = targetFloor;
		for (final AbstractNode node : this.floor.getNodes()) {
			if (!node.getActiveLayers().contains(LayerType.WATER_EVAC)) {
				continue;
			}
			// On cherche si un composant graphique affiche déjà cet ID
			final AbstractGraphicalNode graphicalNode = getChildren().stream()
					.filter(child -> child instanceof AbstractGraphicalNode).map(child -> (AbstractGraphicalNode) child)
					.filter(gn -> gn.getNode().getId() == node.getId()).findFirst().orElse(null);

			if (graphicalNode != null) {
				final double halfSize = AbstractGraphicalNode.getNodeSize() / 2;
				graphicalNode.setTranslateX(GuiUtils.centimetresTopixels(this.project, node.getPosX()) - halfSize);
				graphicalNode.setTranslateY(GuiUtils.centimetresTopixels(this.project, node.getPosY()) - halfSize);
			} else {
				drawGraphicalNode(node);
			}
		}
		requestLayout();
		evt.consume();
	}

	private void onLinkedNodePlacementEvent(final LinkedNodePlacementEvent evt) {
		final Floor targetFloor = evt.getFloor();
		if (targetFloor.equals(this.floor)) {
			final Node newTool = evt.getNode();
			drawGraphicalNode(newTool);
		}
		evt.consume();
	}

	private void onNodePlacementEvent(final NodePlacementEvent evt) {
		final Floor targetFloor = evt.getFloor();
		if (targetFloor.equals(this.floor)) {
			setCurrentNodeModel(evt.getModel(), evt.getNodeCross());
		}
		evt.consume();
	}

	private void onLinkTracingEvent(final LinkTracingEvent evt) {
		final Floor targetFloor = evt.getFloor();
		if (targetFloor.equals(this.floor)) {
			final HookType newTool = evt.getHook();
			if (newTool != null) {
				System.err
						.println("Dans le listener EvacuationsLayer, on trace un lien de type " + newTool.getHookKey());
				setCurrentHookType(newTool);
			}
		}
		evt.consume();
	}

	private void handleCanvasClick(final MouseEvent event) {
		if (event.getTarget() != this) {
			return;
		}
		final double x = event.getX();
		final double y = event.getY();
		if (this.currentHookType == null && this.currentNodeModel == null) {
			return;
		}
		if (this.currentNodeModel != null) {
			createNewNode(x, y, this.floor, true);
			this.isDrawingTube = false;

		} else if (this.currentHookType != null) {
			if (!this.isDrawingTube) {
				this.startX = x;
				this.startY = y;
				this.isDrawingTube = true;
			} else {
				drawPipe(this.startX, this.startY, x, y);
				this.isDrawingTube = false;
			}
		}
		event.consume();
	}

	private void createNewNode(final double pixelX, final double pixelY, final INodeContainer container,
			final boolean draw) {
		if (this.direction == null || this.direction.equals(NodeCross.NONE)) {
			Node newNode = new Node(this.currentNodeModel);
			newNode.setPosX(GuiUtils.pixelToCentimetres(this.project, pixelX));
			newNode.setPosY(GuiUtils.pixelToCentimetres(this.project, pixelY));
			newNode = (Node) SVC_NODES.save(newNode);
			container.addNode(newNode);
			if (container instanceof Floor) {
				SVC_FLOORS.update(this.floor);
			}
			if (draw) {
				drawGraphicalNode(newNode);
			}
		} else {
			createLinkedNodes(pixelX, pixelY, container, draw);
		}
		UiEventBus.send(ProblemsUpdatedEvent.fireMapRebuild());
	}

	private void createLinkedNodes(final double pixelX, final double pixelY, final INodeContainer container,
			final boolean draw) {
		final int levelOffset = this.direction.equals(NodeCross.GOES_DOWN) ? -1 : 1;
		final NodeCross twinDirection = this.direction.equals(NodeCross.GOES_DOWN) ? NodeCross.GOES_UP
				: NodeCross.GOES_DOWN;

		final Floor targetFloor = getFloorAtLevel(levelOffset);

		if (targetFloor != null) {
			final Node curNode = buildAndSaveDirectionalNode(pixelX, pixelY, this.direction);
			final Node linkedNode = buildAndSaveDirectionalNode(pixelX, pixelY, twinDirection);
			associateNodes(curNode, linkedNode);

			container.addNode(curNode);
			if (container instanceof Floor) {
				SVC_FLOORS.update(this.floor);
			}
			targetFloor.addNode(linkedNode);
			SVC_FLOORS.update(targetFloor);
			if (draw) {
				drawGraphicalNode(curNode);
			}
			// envoi d'un event à l'étage concerné pour qu'il déssine le node lié
			UiEventBus.send(LinkedNodePlacementEvent.of(LayerType.WATER_EVAC, targetFloor, linkedNode));
		}
	}

	private Node buildAndSaveDirectionalNode(final double pixelX, final double pixelY, final NodeCross dir) {
		final Node curNode = new Node(this.currentNodeModel);
		curNode.setPosX(GuiUtils.pixelToCentimetres(this.project, pixelX));
		curNode.setPosY(GuiUtils.pixelToCentimetres(this.project, pixelY));
		if (NodeCross.GOES_DOWN.equals(dir)) {
			curNode.setPosZ(0);
		} else {
			curNode.setPosZ(AppStateManager.getInstance().currentProjectProperty().get().getHsp());
		}
		curNode.setNodeCross(dir);
		return (Node) SVC_NODES.save(curNode);
	}

	private void associateNodes(final Node n1, final Node n2) {
		n1.setLinkedNode(n2);
		SVC_NODES.save(n1);
		n2.setLinkedNode(n1);
		SVC_NODES.save(n2);
	}

	private Floor getFloorAtLevel(final int floorLevel) {
		final Project proj = AppStateManager.getInstance().currentProjectProperty().get();
		final Floor withNoNodes = proj.getFloorByNumber(this.floor.getNumber() + floorLevel).get();
		return SVC_FLOORS.reloadWithNodes(withNoNodes).get();
	}

	private void drawGraphicalNode(final AbstractNode node) {
		if (node instanceof final Node realNode) {
			final GraphicalNode gn = new GraphicalNode(this.project, this.floor, realNode, LayerType.WATER_EVAC,
					this.drawingColor, (mouseEvent) -> onExistingNodeClick(mouseEvent));
			getChildren().add(gn);
			requestLayout();
		} else {
			drawMetaNode((MetaNode) node);
		}
	}

	private void onExistingNodeClick(final MouseEvent event) {

		final AbstractGraphicalNode graphicNode = (AbstractGraphicalNode) event.getSource();
		if (graphicNode instanceof final GraphicalNode realNode) {
			if (this.currentNodeModel != null) {
				if (!SVC_NODES.canPutInSameMetaNode(realNode.getNode(), this.direction)) {
					GuiUtils.errorAlert(Messages.getString("Problem.node.multipleLinkedInMeta")); //$NON-NLS-1$
					this.currentNodeModel = null;
					return;

				}
				mutateToMetaNode(realNode);
			} else if (this.currentHookType != null) {
				startDrawingLinkFromGraphNode(realNode);
			}
		} else {
			final GraphicalMetaNode meta = (GraphicalMetaNode) graphicNode;
			if (this.currentNodeModel != null) {
				if (!SVC_NODES.canPutInSameMetaNode(meta.getNode(), this.direction)) {
					GuiUtils.errorAlert(Messages.getString("Problem.node.multipleLinkedInMeta")); //$NON-NLS-1$
					this.currentNodeModel = null;
					return;
				}
				addNodeToMetaNode(meta);
			} else if (this.currentHookType != null) {
				startDrawingLinkFromGraphicalMetaNode(meta);
			}
		}
	}

	private void addNodeToMetaNode(final GraphicalMetaNode meta) {
		final MetaNode oldNode = SVC_NODES.getfullMetaNodeWithChidrenNodes(meta.getNode()).get();
		final double pixelPosX = GuiUtils.centimetresTopixels(this.project, oldNode.getPosX());
		final double pixelPosY = GuiUtils.centimetresTopixels(this.project, oldNode.getPosY());
		createNewNode(pixelPosX, pixelPosY, oldNode, false);
		SVC_NODES.save(oldNode);
		this.currentNodeModel = null;
	}

	private void mutateToMetaNode(GraphicalNode graphicNode) {
		final Node oldNode = SVC_NODES.getfullNodeWithHooks(graphicNode.getNode()).get();
		final double centimeterPosX = oldNode.getPosX();
		final double centimeterPosY = oldNode.getPosY();
		final int targetId = oldNode.getId();
		this.floor.getNodes().removeIf(n -> n.getId() == targetId);
		SVC_NODES.detachNodeFromFloor(oldNode);
		final MetaNode meta = new MetaNode();
		meta.setPosX(centimeterPosX);
		meta.setPosY(centimeterPosY);
		meta.setActiveLayers(oldNode.getActiveLayers());
		meta.addNode(oldNode);
		createNewNode(GuiUtils.centimetresTopixels(this.project, centimeterPosX),
				GuiUtils.centimetresTopixels(this.project, centimeterPosY), meta, false);
		for (final Node child : meta.getNodes()) {
			SVC_NODES.save(child);
		}
		this.floor.addNode(meta);
		this.floor = SVC_FLOORS.update(this.floor);
		final MetaNode savedMeta = (MetaNode) this.floor.getNodes().stream()
				.filter(n -> n instanceof MetaNode && n.getPosX() == centimeterPosX && n.getPosY() == centimeterPosY)
				.findFirst().orElse(oldNode);
		getChildren().remove(graphicNode);
		graphicNode = null;
		drawMetaNode(savedMeta);
		this.currentNodeModel = null;
	}

	private void drawMetaNode(final MetaNode meta) {
		final GraphicalMetaNode gn = new GraphicalMetaNode(this.project, this.floor, meta, LayerType.WATER_EVAC,
				this.drawingColor, (mouseEvent) -> onExistingNodeClick(mouseEvent));
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

	public void setCurrentNodeModel(final NodeModel tool, final NodeCross nodeCross) {
		if (tool == null) {
			this.currentNodeModel = null;
		} else {
			this.currentNodeModel = SVC_NODEMODELS.getByIdWithallowedHooks(tool.getId()).get();
		}
		this.direction = nodeCross;
		this.currentHookType = null;
		this.isDrawingTube = false;
	}

	public void setCurrentHookType(final HookType currentHookType) {
		this.currentHookType = currentHookType;
		this.direction = null;
		this.currentNodeModel = null;
		this.isDrawingTube = false;
	}
}
