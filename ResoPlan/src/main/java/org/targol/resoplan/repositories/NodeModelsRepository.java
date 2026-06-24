package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.catalog.NodeModel;

public interface NodeModelsRepository extends JpaRepository<NodeModel, Integer> {

}
