package org.targol.resoplan.model.problems;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.Project;

@Service
public class ValidationService {

	public List<Problem> validateProject(final Project project) {
		final List<Problem> problems = new ArrayList<>();
		if (project == null) {
			problems.add(Problem.noProjectError());
			return problems;
		}
		// 1. Validation globale projet
		if (project.getPlansScale() <= 0) {
			problems.add(Problem.projectError(project.getId(), Messages.getString("Problem.project.noScale"))); //$NON-NLS-1$
		}

		// 2. Validation des étages
		for (final Floor floor : project.getFloors()) {
			if (floor.getImgPath() == null && !floor.isVirtual()) {
				problems.add(
						Problem.floorError(project.getId(), floor.getId(), Messages.getString("Problem.floor.noPlan"))); //$NON-NLS-1$
			}

			// 3. Validation des Nodes / Hooks ...
			for (final AbstractNode node : floor.getNodes()) {
				if (node instanceof final Node realNode) {
					int nbFreeHooks = realNode.getNbFreeHooks();
					if (nbFreeHooks > 0) {
						problems.add(Problem.warning(project.getId(), floor.getId(), node.getActiveLayers(),
								node.getId(), "Un hook est dans le vide"));
					}
				}
			}
		}
		return problems;
	}
}