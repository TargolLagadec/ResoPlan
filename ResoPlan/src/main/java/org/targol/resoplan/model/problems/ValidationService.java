package org.targol.resoplan.model.problems;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;

@Service
public class ValidationService {

	private final ProjectsService projectsSvc;
	private final NodesService nodesSvc;
	private final NodeModelsService nodeModsSvc;
	private NumberFormat numberFormat = NumberFormat.getInstance();

	public ValidationService(final ProjectsService projectsSvc, final NodesService nodesSvc,
			final NodeModelsService nodeModsSvc) {
		this.projectsSvc = projectsSvc;
		this.nodesSvc = nodesSvc;
		this.nodeModsSvc = nodeModsSvc;
		this.numberFormat.setMaximumFractionDigits(2);
	}

	public List<Problem> validateProject(Project project) {
		final List<Problem> problems = new ArrayList<>();
		if (project == null) {
			problems.add(Problem.noProjectError());
			return problems;
		}
		// On charge toute la grappe projet lazy loadée jusqu'aux Nodes (attention, pas
		// les Hooks)...
		project = this.projectsSvc.openProjectWithFloorsAndNodes(project).get();
		// Et le catalogue des NodeModels
		final Map<Integer, NodeModel> modelsById = this.nodeModsSvc.getAllWithAllowedHooks();

		// 1. Validation globale projet
		if (project.getPlansScale() <= 0) {
			problems.add(Problem.projectError(project.getId(), Messages.getString("Problem.project.noScale"))); //$NON-NLS-1$
		}

		// 2. Validation des étages
		for (final Floor floor : project.getFloors()) {
			if (floor.getImgPath() == null && !floor.isVirtual()) {
				problems.add(Problem.floorError(project.getId(), floor.getId(),
						Messages.getString("Problem.floor.noPlan", floor.getNumber()))); //$NON-NLS-1$
			}

			// 3. Validation des Nodes / Hooks ...
			for (final AbstractNode node : floor.getNodes()) {
				if (node instanceof Node realNode) {
					validateNode(project, floor, realNode, problems);
				} else {
					MetaNode meta = this.nodesSvc.getfullMetaNodeWithChidrenNodes((MetaNode) node).get();
					for (Node childNode : meta.getNodes()) {
						validateNode(project, floor, childNode, problems);
					}
				}
			}
		}
		return problems;
	}

	private void validateNode(Project project, Floor floor, Node node, List<Problem> problems) {
		Node realNode = this.nodesSvc.getfullNodeWithHooks(node).get();
		String formatedPosX = this.numberFormat.format(realNode.getPosX());
		String formatedPosY = this.numberFormat.format(realNode.getPosY());
		String formatedPosZ = this.numberFormat.format(realNode.getPosZ());
		// Vérification qu'il n'y a pas plusieurs nodes au même endroit
		List<NodeCollision> collisions = this.nodesSvc.checkCollisions(realNode);
		if (collisions.size() > 0) {
			for (NodeCollision col : collisions) {
				if (Severity.ERROR.equals(col.severity())) {
					problems.add(Problem.nodeError(project.getId(), floor.getId(), col.layer(), node.getId(),
							Messages.getString("Problem.node.samePlace.error", //$NON-NLS-1$
									realNode.getModel().getName(), formatedPosX, formatedPosY, formatedPosZ,
									floor.getNumber(), col.layer(), col.other().getModel().getName())));
				} else {
					problems.add(Problem.nodeWarning(project.getId(), floor.getId(), col.layer(), node.getId(),
							Messages.getString("Problem.node.samePlace.error", //$NON-NLS-1$
									realNode.getModel().getName(), formatedPosX, formatedPosY, formatedPosZ,
									floor.getNumber(), col.layer(), col.other().getModel().getName())));
				}
			}

		}
		final Map<LayerType, Integer> freeHooks = realNode.getNbFreeHooksPerLayer();
		for (final LayerType layer : freeHooks.keySet()) {
			problems.add(Problem.nodeWarning(project.getId(), floor.getId(), layer, node.getId(),
					Messages.getString("Problem.node.unlinkedHook", realNode.getModel().getName(), //$NON-NLS-1$
							formatedPosX, formatedPosY, formatedPosZ, floor.getNumber(), layer.getLabel(),
							freeHooks.get(layer))));

		}
	}
}