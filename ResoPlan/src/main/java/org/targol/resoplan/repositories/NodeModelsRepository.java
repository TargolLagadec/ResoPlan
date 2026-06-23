package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.NodeModel;

public interface NodeModelsRepository extends JpaRepository<NodeModel, Integer> {

}
