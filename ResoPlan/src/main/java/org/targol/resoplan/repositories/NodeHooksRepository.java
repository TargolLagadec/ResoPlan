package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.NodeHook;

public interface NodeHooksRepository extends JpaRepository<NodeHook, Integer> {

}
