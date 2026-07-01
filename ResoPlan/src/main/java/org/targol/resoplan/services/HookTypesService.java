package org.targol.resoplan.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.model.catalog.HookType;
import org.targol.resoplan.repositories.HookTypesRepository;

@Service
public class HookTypesService extends NoCacheService {

	private final HookTypesRepository repo;

	public HookTypesService(final HookTypesRepository repo) {
		this.repo = repo;
	}

	public HookType save(final HookType hook) throws ServiceException {
		return saveAndClear(hook);
	}

	@Transactional(readOnly = true)
	public List<HookType> getAllFromLayer(final LayerType layer) {
		return detachAll(this.repo.findByLayerOrderByHookKeyAsc(layer));
	}

	@Transactional(readOnly = true)
	public List<HookType> getAll() {
		return detachAll(this.repo.findAll());
	}

	@Transactional(readOnly = true)
	Optional<HookType> getByHookKey(final String libKey) {
		return detachOptionalIfPresent(this.repo.findByHookKey(libKey));
	}

	public void deleteHookType(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}
}