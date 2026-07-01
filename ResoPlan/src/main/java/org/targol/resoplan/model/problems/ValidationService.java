package org.targol.resoplan.model.problems;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;

@Service
public class ValidationService {

	public List<Problem> validateProject(final Project project) {
		final List<Problem> problems = new ArrayList<>();
		if (project == null) {
			problems.add(Problem.noProject());
		}
		// 1. Validation globale projet
		if (project.getPlansScale() <= 0) {
			problems.add(Problem.projectError(project.getId(), "Échelle px/m non définie"));
		}

		// 2. Validation des étages
		for (final Floor floor : project.getFloors()) {
			if (floor.getImgPath() == null) {
				problems.add(Problem.floorError(project.getId(), floor.getId(), "Pas d'image de plan"));
			}

			// 3. Validation des Nodes / Hooks ...
//			for (final Node node : floor.getNodes()) {
//				if (isHookInTheVoid(node)) {
//					problems.add(Problem.warning(project.getId(), floor.getId(), node.get(), node.getId(),
//							"Un hook est dans le vide"));
//				}
//			}
		}
		return problems;
	}
}