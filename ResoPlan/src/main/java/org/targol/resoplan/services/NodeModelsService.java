package org.targol.resoplan.services;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.repositories.NodeModelsRepository;

@Service
@Transactional
public class NodeModelsService {

	private final NodeModelsRepository repo;

	public NodeModelsService(final NodeModelsRepository repo) {
		this.repo = repo;
	}

	public NodeModel save(final NodeModel hook) throws ServiceException {
		return this.repo.save(hook);
	}

	public List<NodeModel> getAll() {
		return this.repo.findAll();
	}

	public List<NodeModel> getAllByCategory(final NodeCategory category) {
		return this.repo.findByCategory(category);
	}

	public void delete(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}

}