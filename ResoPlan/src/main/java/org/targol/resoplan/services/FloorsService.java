package org.targol.resoplan.services;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.repositories.FloorsRepository;

@Service
@Transactional
public class FloorsService {

	private final FloorsRepository repo;

	public FloorsService(final FloorsRepository repo) {
		this.repo = repo;
	}

	public Floor save(final Floor floor) throws ServiceException {
		return this.repo.save(floor);
	}

	public void deleteFloor(final int id) {
		this.repo.deleteById(id);
	}

}