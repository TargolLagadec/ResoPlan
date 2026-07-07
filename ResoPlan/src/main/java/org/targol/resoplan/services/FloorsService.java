package org.targol.resoplan.services;

import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.targol.resoplan.model.Floor;
import org.targol.resoplan.model.Layer;
import org.targol.resoplan.model.LayerType;
import org.targol.resoplan.repositories.FloorsRepository;

@Service
public class FloorsService extends NoCacheService {

	private final FloorsRepository repo;

	public FloorsService(final FloorsRepository repo) {
		this.repo = repo;
	}

	public Optional<Floor> reloadWithNodes(final Floor f) {
		return findByIdWithNodes(f.getId());
	}

	public Optional<Floor> findByIdWithNodes(int id) {
		return detachOptionalIfPresent(this.repo.findByIdWithNodes(id));
	}

	public Floor create(final Floor floor) throws ServiceException {
		// adding layers
		for (final LayerType type : LayerType.values()) {
			final Layer layer = new Layer(type);
			floor.addLayer(layer);
		}
		return saveAndClear(floor);
	}

	public Floor update(final Floor floor) throws ServiceException {
		return saveAndClear(floor);
	}

	public void deleteFloor(final int id) {
		this.repo.deleteById(id);
	}

}