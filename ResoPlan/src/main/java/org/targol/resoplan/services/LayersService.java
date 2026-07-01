package org.targol.resoplan.services;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.targol.resoplan.model.Layer;
import org.targol.resoplan.repositories.LayersRepository;

@Service
public class LayersService extends NoCacheService {

	private final LayersRepository repo;

	public LayersService(final LayersRepository repo) {
		this.repo = repo;
	}

	public Layer save(final Layer layer) throws ServiceException {
		return saveAndClear(layer);
	}

	public void deleteRoom(final int id) {
		this.repo.deleteById(id);
	}

}