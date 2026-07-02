package org.targol.resoplan.model.problems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.services.FloorsService;
import org.targol.resoplan.services.NodeModelsService;
import org.targol.resoplan.services.NodesService;
import org.targol.resoplan.services.ProjectsService;

@Service
public class ValidationService {

	private final ProjectsService projectsSvc;
	private final FloorsService floorsSvc;
	private final NodesService nodesSvc;
	private final NodeModelsService nodeModsSvc;

	public ValidationService(final ProjectsService projectsSvc, final FloorsService floorsSvc,
			final NodesService nodesSvc, final NodeModelsService nodeModsSvc) {
		this.projectsSvc = projectsSvc;
		this.floorsSvc = floorsSvc;
		this.nodesSvc = nodesSvc;
		this.nodeModsSvc = nodeModsSvc;
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
				if (node instanceof Node) {
					final Node realNode = this.nodesSvc.getByIdWithHooks(node.getId()).get();
					// On charge les données des relations lazy Loadées.
					realNode.setModel(modelsById.get(realNode.getModel().getId()));
					final Map<LayerType, Integer> freeHooks = realNode.getNbFreeHooksPerLayer();
					for (final LayerType layer : freeHooks.keySet()) {
						problems.add(Problem.nodeWarning(project.getId(), floor.getId(), layer, node.getId(),
								Messages.getString("Problem.node.unlinkedHook", realNode.getModel().getName(),
										realNode.getPosX(), realNode.getPosY(), freeHooks.get(layer))));

					}
				}
			}
		}
		return problems;
	}
}