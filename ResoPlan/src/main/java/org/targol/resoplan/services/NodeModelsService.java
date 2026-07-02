package org.targol.resoplan.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.targol.resoplan.model.catalog.NodeModel;
import org.targol.resoplan.model.catalog.enums.NodeCategory;
import org.targol.resoplan.repositories.NodeModelsRepository;

@Service
public class NodeModelsService extends NoCacheService {

	private final NodeModelsRepository repo;

	public NodeModelsService(final NodeModelsRepository repo) {
		this.repo = repo;
	}

	public NodeModel save(final NodeModel hook) throws ServiceException {
		return saveAndClear(hook);
	}

	@Transactional(readOnly = true)
	public List<NodeModel> getAll() {
		return detachAll(this.repo.findAll());
	}

	@Transactional(readOnly = true)
	public List<NodeModel> getAllByCategory(final NodeCategory category) {
		return detachAll(this.repo.findByCategory(category));
	}

	@Transactional(readOnly = true)
	public Map<Integer, NodeModel> getAllWithAllowedHooks() {
		final List<NodeModel> ret = detachAll(this.repo.findAllWithAllowedHooks());
		if (ret.isEmpty()) {
			return Map.of();
		}
		return ret.stream().collect(Collectors.toMap(NodeModel::getId, model -> model));
	}

	@Transactional(readOnly = true)
	public Optional<NodeModel> getByIdWithallowedHooks(final int id) {
		return detachOptionalIfPresent(this.repo.findByIdWithAllowedHooks(id));
	}

	public void delete(final int id) {
		this.repo.deleteById(id);
	}

	// TODO remove that : too dangerous !
	public void deleteAll() {
		this.repo.deleteAll();
	}

}