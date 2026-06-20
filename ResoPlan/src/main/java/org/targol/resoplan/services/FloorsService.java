package org.targol.resoplan.services;

import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.repositories.FloorsRepository;

@Service
@Transactional
public class FloorsService {

	private final FloorsRepository repo;

	public FloorsService(final FloorsRepository repo) {
		this.repo = repo;
	}

	public Floor createFloor(final Floor floor) throws ServiceException {
		return this.repo.save(floor);
	}

	public Floor updateFloor(final Floor floorToUpdate) throws ServiceException {
		final int id = floorToUpdate.getId();
		if (id <= 0) {
			throw new ServiceException(Messages.getString("FloorsService.UpdateError.NoId")); //$NON-NLS-1$
		}
		final Optional<Floor> test = this.repo.findById(id);
		if (test.isEmpty()) {
			throw new ServiceException(Messages.getString("FloorsService.UpdateError.DoesntExist")); //$NON-NLS-1$
		}
		return this.repo.save(floorToUpdate);
	}

	public void deleteFloor(final int id) {
		this.repo.deleteById(id);
	}

}