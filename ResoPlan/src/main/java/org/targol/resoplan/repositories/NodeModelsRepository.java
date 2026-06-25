package org.targol.resoplan.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;

public interface NodeModelsRepository extends JpaRepository<NodeModel, Integer> {

	List<NodeModel> findByCategory(NodeCategory category);
}
