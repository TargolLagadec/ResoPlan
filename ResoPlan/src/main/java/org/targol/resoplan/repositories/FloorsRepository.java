package org.targol.resoplan.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Hook;

public interface FloorsRepository extends JpaRepository<Floor, Integer> {

	/**
	 * Reads a Floor using its id and FORCES its {@link Hook}s reading.
	 *
	 * @param id Id of the project to find
	 * @return Found project as an {@link Optional} with all its {@link Hook}s
	 *         loaded;
	 */
	@Query("SELECT f FROM Floor f LEFT JOIN FETCH f.nodes WHERE f.id = :id")
	Optional<Floor> findByIdWithNodes(@Param("id") int id);
}
