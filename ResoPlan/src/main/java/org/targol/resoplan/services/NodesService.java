package org.targol.resoplan.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.repositories.NodesRepository;

@Service
@Transactional
public class NodesService {

	private final NodesRepository repo;

	public NodesService(final NodesRepository repo) {
		this.repo = repo;
	}

	public AbstractNode save(final AbstractNode node) throws ServiceException {
		return this.repo.save(node);
	}

	@Transactional
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
			this.repo.save(node);
			if (realNode.getLinkedNode() != null) {
				runRecursiveLazyMove(realNode.getLinkedNode(), xFin, yFin, impactedFloorIds);
			}
			this.repo.findParentMetanode(node.getId())
					.ifPresent(parent -> runRecursiveLazyMove(parent, xFin, yFin, impactedFloorIds));
		} else if (node instanceof final MetaNode meta) {
			node.setPosX(xFin);
			node.setPosY(yFin);
			this.repo.save(meta);
			for (final Node child : meta.getNodes()) {
				runRecursiveLazyMove(child, xFin, yFin, impactedFloorIds);
			}
		}
	}

	@Transactional
	public MetaNode updateMetaNodeCoordinates(final MetaNode attachedMetaNode) {
		final MetaNode databaseMeta = (MetaNode) this.repo.findById(attachedMetaNode.getId()).orElseThrow();

		databaseMeta.setPosX(attachedMetaNode.getPosX());
		databaseMeta.setPosY(attachedMetaNode.getPosY());

		for (final Node child : databaseMeta.getNodes()) {
			child.setPosX(attachedMetaNode.getPosX());
			child.setPosY(attachedMetaNode.getPosY());
			if (child.getLinkedNode() != null) {
				// FIXME ici
			}
		}

		return this.repo.saveAndFlush(databaseMeta);
	}

	public List<AbstractNode> getAll() {
		return this.repo.findAll();
	}

	public Optional<Node> getByIdWithAllowedHooks(final int id) {
		return this.repo.findByIdWithAllowedHooks(id);
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