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
@Transactional
public class HookTypesService {

	private final HookTypesRepository repo;

	public HookTypesService(final HookTypesRepository repo) {
		this.repo = repo;
	}

	public HookType save(final HookType hook) throws ServiceException {
		return this.repo.save(hook);
	}

	public List<HookType> getAllFromLayer(final LayerType layer) {
		return this.repo.findByLayerOrderByHookKeyAsc(layer);
	}

	public List<HookType> getAll() {
		return this.repo.findAll();
	}

	Optional<HookType> getByHookKey(final String libKey) {
		return this.repo.findByHookKey(libKey);
	}

	public void deleteHookType(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}
}