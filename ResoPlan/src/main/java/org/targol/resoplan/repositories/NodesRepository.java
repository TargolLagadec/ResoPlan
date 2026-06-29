package org.targol.resoplan.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.targol.resoplan.model.AbstractNode;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Hook;
import org.targol.resoplan.model.MetaNode;
import org.targol.resoplan.model.Node;

import jakarta.transaction.Transactional;

public interface NodesRepository extends JpaRepository<AbstractNode, Integer> {

	@Query("""
			SELECT f FROM Floor f
			JOIN f.nodes n
			WHERE n.id = :nodeId
			""")
	Floor findFloorByNodeId(@Param("nodeId") int nodeId);

	@Query("""
			SELECT m FROM MetaNode m
			JOIN m.nodes enfant
			WHERE enfant.id = :nodeId
			""")
	Optional<MetaNode> findParentMetanode(@Param("nodeId") int nodeId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE NODE SET FLOOR_ID = NULL WHERE ID = :nodeId", nativeQuery = true)
	void detachFromFloor(@Param("nodeId") int nodeId);

	/**
	 * Reads a Node using its id and FORCES its {@link Hook}s reading.
	 *
	 * @param id Id of the Node to find
	 * @return Found Node as an {@link Optional} with all its {@link Hook}s loaded;
	 */
	@Query("SELECT n FROM Node n LEFT JOIN FETCH n.hooks WHERE n.id = :id")
	Optional<Node> findByIdWithAllowedHooks(@Param("id") int id);

}
