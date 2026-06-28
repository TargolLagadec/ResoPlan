package org.targol.resoplan.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.model.catalog.HookType;

public interface NodesRepository extends JpaRepository<Node, Integer> {

	/**
	 * Reads a Node using its id and FORCES its {@link HookType}s reading.
	 *
	 * @param id Id of the NodeModel to find
	 * @return Found NodeModel as an {@link Optional} with all its {@link HookType}s
	 *         loaded;
	 */
	@Query("SELECT n FROM Node n LEFT JOIN FETCH n.hooks WHERE n.id = :id")
	Optional<Node> findByIdWithallowedHooks(@Param("id") int id);
}
