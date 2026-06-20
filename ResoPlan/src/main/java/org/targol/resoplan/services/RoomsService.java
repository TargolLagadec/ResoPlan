package org.targol.resoplan.services;

import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.i18n.Messages;
import org.targol.resoplan.model.Room;
import org.targol.resoplan.repositories.RoomsRepository;

@Service
@Transactional
public class RoomsService {

	private final RoomsRepository repo;

	public RoomsService(final RoomsRepository repo) {
		this.repo = repo;
	}

	public Room createRoom(final Room room) throws ServiceException {
		return this.repo.save(room);
	}

	public Room updateRoom(final Room roomToUpdate) throws ServiceException {
		final int id = roomToUpdate.getId();
		if (id <= 0) {
			throw new ServiceException(Messages.getString("RoomsService.UpdateError.NoId")); //$NON-NLS-1$
		}
		final Optional<Room> test = this.repo.findById(id);
		if (test.isEmpty()) {
			throw new ServiceException(Messages.getString("RoomsService.UpdateError.DoesntExist")); //$NON-NLS-1$
		}
		return this.repo.save(roomToUpdate);
	}

	public void deleteRoom(final int id) {
		this.repo.deleteById(id);
	}

}