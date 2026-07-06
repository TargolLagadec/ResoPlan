package org.targol.resoplan.ui.panels.floornetwork.layers.evac;

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
	private Floor floor;

	public EvacuationsLayer(final Floor floor) {
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
				System.err.println(
						"au refresh du EvacuationCanvas : position = (" + node.getPosX() + "," + node.getPosY() + ")");

				graphicalNode.setTranslateX(node.getPosX() - halfSize);
				graphicalNode.setTranslateY(node.getPosY() - halfSize);
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
			final NodeModel newTool = evt.getModel();
			if (newTool != null) {
				System.err.println("Dans le listener EvacuationsLayer, on dessine le node " + newTool.getName());
				setCurrentNodeModel(newTool, evt.getNodeCross());
			}
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
		System.err.println("Clic sur l'étage " + this.floor.getNumber() + " à la position (" + x + "," + y + ")");
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

	private void createNewNode(final double x, final double y, final INodeContainer container, final boolean draw) {
		if (this.direction == null || this.direction.equals(NodeCross.NONE)) {
			Node newNode = new Node(this.currentNodeModel);
			newNode.setPosX(x);
			newNode.setPosY(y);
			newNode = (Node) SVC_NODES.save(newNode);
			container.addNode(newNode);
			if (container instanceof Floor) {
				SVC_FLOORS.update(this.floor);
			}
			if (draw) {
				drawGraphicalNode(newNode);
			}
		} else {
			createLinkedNodes(x, y, container, draw);
		}
		UiEventBus.send(ProblemsUpdatedEvent.fireCheck(AppStateManager.getInstance().currentProjectProperty().get()));
	}

	private void createLinkedNodes(final double x, final double y, final INodeContainer container, final boolean draw) {
		final int levelOffset = this.direction.equals(NodeCross.GOES_DOWN) ? -1 : 1;
		final NodeCross twinDirection = this.direction.equals(NodeCross.GOES_DOWN) ? NodeCross.GOES_UP
				: NodeCross.GOES_DOWN;

		final Floor targetFloor = getFloorAtLevel(levelOffset);

		if (targetFloor != null) {
			final Node curNode = buildAndSaveDirectionalNode(x, y, this.direction);
			final Node linkedNode = buildAndSaveDirectionalNode(x, y, twinDirection);
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

	private Node buildAndSaveDirectionalNode(final double x, final double y, final NodeCross dir) {
		final Node curNode = new Node(this.currentNodeModel);
		curNode.setPosX(x);
		curNode.setPosY(y);
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

	private Floor getFloorAtLevel(final int i) {
		final Project proj = AppStateManager.getInstance().currentProjectProperty().get();
		final Floor withNoNodes = proj.getFloorByNumber(this.floor.getNumber() + i).get();
		return SVC_FLOORS.reloadWithNodes(withNoNodes).get();
	}

	private void drawGraphicalNode(final AbstractNode node) {
		if (node instanceof final Node realNode) {
			final GraphicalNode gn = new GraphicalNode(realNode, this.drawingColor,
					(mouseEvent) -> onExistingNodeClick(mouseEvent));
			getChildren().add(gn);
			requestLayout();
		} else {
			drawMetaNode((MetaNode) node);
		}
	}

	private void onExistingNodeClick(final MouseEvent event) {

		final AbstractGraphicalNode graphicNode = (AbstractGraphicalNode) event.getSource();
		if (graphicNode instanceof GraphicalNode realNode) {
			if (this.currentNodeModel != null) {
				System.out.println("Création d'un MetaNode demandée par clic sur un node classique"); //$NON-NLS-1$
				mutateToMetaNode(realNode);
			} else if (this.currentHookType != null) {
				System.out.println("Clic sur un noeud classique pour commencer un tracé de tuyau !"); //$NON-NLS-1$
				startDrawingLinkFromGraphNode(realNode);
			}
		} else {
			GraphicalMetaNode meta = (GraphicalMetaNode) graphicNode;
			if (this.currentNodeModel != null) {
				System.out.println("Ajout d'un nouveau node à un MetaNode demandée par clic sur un node metanode"); //$NON-NLS-1$
				addNodeToMetaNode(meta);
			} else if (this.currentHookType != null) {
				System.out.println("Clic sur un noeud metaNode pour commencer un tracé de tuyau !"); //$NON-NLS-1$
				startDrawingLinkFromGraphicalMetaNode(meta);
			}
		}
	}

	private void addNodeToMetaNode(GraphicalMetaNode meta) {
		final MetaNode oldNode = SVC_NODES.getfullMetaNodeWithChidrenNodes(meta.getNode()).get();
		final double posX = oldNode.getPosX();
		final double posY = oldNode.getPosY();
		final int targetId = oldNode.getId();
		createNewNode(posX, posY, oldNode, false);
		final MetaNode savedMeta = (MetaNode) this.floor.getNodes().stream()
				.filter(n -> n instanceof MetaNode && n.getPosX() == posX && n.getPosY() == posY).findFirst()
				.orElse(oldNode);
		drawMetaNode(savedMeta);
		this.currentNodeModel = null;
	}

	private void mutateToMetaNode(GraphicalNode graphicNode) {
		final Node oldNode = SVC_NODES.getfullNodeWithHooks(graphicNode.getNode()).get();
		final double posX = oldNode.getPosX();
		final double posY = oldNode.getPosY();
		final int targetId = oldNode.getId();
		this.floor.getNodes().removeIf(n -> n.getId() == targetId);
		SVC_NODES.detachNodeFromFloor(oldNode);
		final MetaNode meta = new MetaNode();
		meta.setPosX(posX);
		meta.setPosY(posY);
		meta.setActiveLayers(oldNode.getActiveLayers());
		meta.addNode(oldNode);
		createNewNode(posX, posY, meta, false);
		for (Node child : meta.getNodes()) {
			SVC_NODES.save(child);
		}
		this.floor.addNode(meta);
//		SVC_NODES.save(meta);
		this.floor = SVC_FLOORS.update(this.floor);
		// Récupération de l'instance pour l'IHM (avec id)
//		final MetaNode savedMeta = (MetaNode) this.floor.getNodes().stream()
//				.filter(n -> n instanceof MetaNode && n.getPosX() == posX && n.getPosY() == posY).findFirst()
//				.orElse(meta);
		getChildren().remove(graphicNode);
		graphicNode = null;
		drawMetaNode(meta);
		this.currentNodeModel = null;
	}

	private void drawMetaNode(final MetaNode meta) {
		final GraphicalMetaNode gn = new GraphicalMetaNode(meta, this.drawingColor,
				(mouseEvent) -> onExistingNodeClick(mouseEvent));
		getChildren().add(gn);
		requestLayout();
	}

	private void startDrawingLinkFromGraphNode(final GraphicalNode graphicNode) {
		// TODO Auto-generated method stub

	}

	private void startDrawingLinkFromGraphicalMetaNode(final GraphicalMetaNode graphicNode) {
		// TODO Auto-generated method stub

	}

	private void drawPipe(final double x1, final double y1, final double x2, final double y2) {
//		this.gc.setStroke(Color.BROWN);
//		// TODO Modifier ça
//		final double tubeDiam = 100;
//		this.gc.setLineWidth(tubeDiam / 10);
//		this.gc.setLineCap(javafx.scene.shape.StrokeLineCap.ROUND);
//		this.gc.strokeLine(x1, y1, x2, y2);
	}

	public void setCurrentNodeModel(final NodeModel tool, final NodeCross nodeCross) {
		this.currentNodeModel = SVC_NODEMODELS.getByIdWithallowedHooks(tool.getId()).get();
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
