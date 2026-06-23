package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.Layer;

public interface LayersRepository extends JpaRepository<Layer, Integer> {
}
