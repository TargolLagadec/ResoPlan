package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.targol.resoplan.model.Floor;

@Repository
public interface FloorsRepository extends JpaRepository<Floor, Integer> {

}
