package org.targol.resoplan.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.targol.resoplan.model.Room;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Integer> {
}
