package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.Floor;

public interface FloorsRepository extends JpaRepository<Floor, Integer> {

}
