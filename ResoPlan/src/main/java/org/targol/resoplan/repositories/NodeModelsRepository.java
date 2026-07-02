package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;

public interface NodeModelsRepository extends JpaRepository<NodeModel, Integer> {

	/**
	 * Reads a NodeModel using its id and FORCES its {@link HookType}s reading.
	 *
	 * @param id Id of the NodeModel to find
	 * @return Found NodeModel as an {@link Optional} with all its {@link HookType}s
	 *         loaded;
	 */
	@Query("""
			SELECT m FROM NodeModel m
			LEFT JOIN FETCH m.allowedHooks
			WHERE m.id = :id
			""")
	Optional<NodeModel> findByIdWithAllowedHooks(@Param("id") int id);

	@Query("""
			SELECT m FROM NodeModel m
			LEFT JOIN FETCH m.allowedHooks
			""")
	List<NodeModel> findAllWithAllowedHooks();

	List<NodeModel> findByCategory(NodeCategory category);
}
