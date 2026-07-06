package org.targol.resoplan.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.enums.NodeCross;
import org.targol.resoplan.model.problems.NodeCollision;
import org.targol.resoplan.model.problems.Severity;
import org.targol.resoplan.repositories.NodesRepository;
import org.targol.resoplan.ui.utils.AppStateManager;
import org.targol.resoplan.utils.MiscUtils;

@Service
public class NodesService extends NoCacheService {

	private final NodesRepository repo;

	public NodesService(final NodesRepository repo) {
		this.repo = repo;
	}

	public AbstractNode save(final AbstractNode node) throws ServiceException {
		return saveAndClear(node);
	}

	public boolean canPutInSameMetaNode(Node node, NodeCross newNodeDirection) {
		if (node.getLinkedNode() == null) {
			return true;
		}
		return !node.getNodeCross().equals(newNodeDirection);
	}

	public boolean canPutInSameMetaNode(MetaNode meta, NodeCross newNodeDirection) {
		meta = getfullMetaNodeWithChidrenNodes(meta).get();
		for (Node node : meta.getNodes()) {
			if (!canPutInSameMetaNode(node, newNodeDirection)) {
				return false;
			}
		}
		return true;
	}

	public boolean areOnSameFloor(Node node1, Node node2) {
		Map<Integer, Integer> nodesToFloor = AppStateManager.getInstance().nodeIdToFloorId().get();
		return nodesToFloor.get(node1.getId()) == nodesToFloor.get(node2.getId());
	}

	public List<NodeCollision> checkCollisions(Node node) {
		List<NodeCollision> ret = new ArrayList<>();
		List<AbstractNode> nodesAtPlace = detachAll(
				this.repo.findByPosXAndPosYAndPosZ(node.getPosX(), node.getPosY(), node.getPosZ()));

		for (AbstractNode other : nodesAtPlace) {
			// On évite de se comparer à soi-même et on exclut les MetaNodes spécifiques si besoin, on évacue aussi les
			// noeuds qui ne sont pas au même étage
			if (other.getId() == node.getId() || other instanceof MetaNode) {
				continue;
			}
			Node realNode = (Node) other;
			if (!areOnSameFloor(realNode, node)) {
				continue;
			}
			// --- Détection de la collision ---
			Set<LayerType> common = MiscUtils.getCommonElements(node.getActiveLayers(), realNode.getActiveLayers());
			Severity sev;
			if (common.size() > 0) {
				// Même endroit, même calque = Erreur critique
				sev = Severity.ERROR;
			} else {
				// Même endroit, calque différent = warning
				sev = Severity.WARNING;
			}
			for (LayerType layer : node.getActiveLayers()) {
				NodeCollision coll = new NodeCollision(sev, realNode, layer);
				ret.add(coll);
			}
		}
		return ret;
	}

	public Set<Floor> processLazyMove(final AbstractNode node, final double xFin, final double yFin) {
		final Set<Floor> impactedFloorIds = new HashSet<>();
		runRecursiveLazyMove(node, xFin, yFin, impactedFloorIds);
		return impactedFloorIds;
	}

	private void runRecursiveLazyMove(final AbstractNode node, final double xFin, final double yFin,
			final Set<Floor> impactedFloorIds) {
		if (node.getPosX() == xFin && node.getPosY() == yFin) {
			return;
		}
		final Floor floor = this.repo.findFloorByNodeId(node.getId());
		if (floor != null) {
			impactedFloorIds.add(floor);
		}
		if (node instanceof final Node realNode) {
			node.setPosX(xFin);
			node.setPosY(yFin);
			saveAndClear(node);
			if (realNode.getLinkedNode() != null) {
				runRecursiveLazyMove(realNode.getLinkedNode(), xFin, yFin, impactedFloorIds);
			}
			this.repo.findParentMetanode(node.getId())
					.ifPresent(parent -> runRecursiveLazyMove(parent, xFin, yFin, impactedFloorIds));
		} else if (node instanceof MetaNode meta) {
			node.setPosX(xFin);
			node.setPosY(yFin);
			saveAndClear(meta);
			meta = this.repo.findMetaByIdWithChildrenNodes(meta.getId()).get();
			for (final Node child : meta.getNodes()) {
				runRecursiveLazyMove(child, xFin, yFin, impactedFloorIds);
			}
		}
	}

	@Transactional(readOnly = true)
	public Optional<Node> getfullNodeWithHooks(Node node) {
		return detachOptionalIfPresent(this.repo.findByIdWithHooks(node.getId()));
	}

	@Transactional(readOnly = true)
	public Optional<MetaNode> getfullMetaNodeWithChidrenNodes(MetaNode node) {
		return detachOptionalIfPresent(this.repo.findMetaByIdWithAllowedChildren(node.getId()));
	}

	public void detachNodeFromFloor(final AbstractNode node) {
		this.repo.detachFromFloor(node.getId());
	}

	public void delete(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}

}