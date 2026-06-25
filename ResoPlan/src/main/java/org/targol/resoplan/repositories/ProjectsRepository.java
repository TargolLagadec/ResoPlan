package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Project;

public interface ProjectsRepository extends JpaRepository<Project, Integer> {

	/**
	 * Reads a project using its id and FORCES its {@link Floor}s reading.
	 *
	 * @param id Id of the project to find
	 * @return Found project as an {@link Optional} with all its {@link Floor}s
	 *         loaded;
	 */
	@Query("SELECT p FROM Project p LEFT JOIN FETCH p.floors WHERE p.id = :id")
	Optional<Project> findByIdWithFloors(@Param("id") int id);

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
