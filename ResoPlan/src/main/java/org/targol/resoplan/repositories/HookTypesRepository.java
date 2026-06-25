package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.enums.HookType;

public interface HookTypesRepository extends JpaRepository<HookType, Integer> {
	List<HookType> findByLayer(LayerType layer);

	Optional<HookType> findByLibKey(String libKey);
}
