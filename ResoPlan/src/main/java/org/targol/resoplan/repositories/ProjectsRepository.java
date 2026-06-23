package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.Project;

public interface ProjectsRepository extends JpaRepository<Project, Integer> {

	/**
	 * Reads a project using its name.
	 *
	 * @param name The name of the project to find
	 * @return Found project as an {@link Optional}.
	 */
	Optional<Project> findByName(String name);

	/**
	 *
	 * @return the list of the 10 last openend projects.
	 */
	List<Project> findTop10ByOrderByLastOpenedDesc();
}
