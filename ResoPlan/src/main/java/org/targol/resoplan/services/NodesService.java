package org.targol.resoplan.services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.Node;
import org.targol.resoplan.repositories.NodesRepository;

@Service
@Transactional
public class NodesService {

	private final NodesRepository repo;

	public NodesService(final NodesRepository repo) {
		this.repo = repo;
	}

	public Node save(final Node hook) throws ServiceException {
		return this.repo.save(hook);
	}

	public List<Node> getAll() {
		return this.repo.findAll();
	}

	public Optional<Node> getByIdWithallowedHooks(final int id) {
		return this.repo.findByIdWithallowedHooks(id);
	}

	public void delete(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}

}