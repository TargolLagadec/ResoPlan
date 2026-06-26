package org.targol.resoplan.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.HookType;

public interface HookTypesRepository extends JpaRepository<HookType, Integer> {
	List<HookType> findByLayerOrderByHookKeyAsc(LayerType layer);

	Optional<HookType> findByHookKey(String libKey);
}
