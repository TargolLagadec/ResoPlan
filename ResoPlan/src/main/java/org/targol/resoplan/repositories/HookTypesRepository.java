package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.model.enums.LayerType;

public interface HookTypesRepository extends JpaRepository<HookType, Integer> {
	List<HookType> findByLayer(LayerType layer);

	Optional<HookType> findByLibKey(String libKey);
}
