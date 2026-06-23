package org.targol.resoplan.services;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.Layer;
import org.targol.resoplan.repositories.LayersRepository;

@Service
@Transactional
public class LayersService {

	private final LayersRepository repo;

	public LayersService(final LayersRepository repo) {
		this.repo = repo;
	}

	public Layer save(final Layer layer) throws ServiceException {
		return this.repo.save(layer);
	}

	public void deleteRoom(final int id) {
		this.repo.deleteById(id);
	}

}