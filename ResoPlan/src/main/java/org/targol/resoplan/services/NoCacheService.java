package org.targol.resoplan.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional
public abstract class NoCacheService {

	@PersistenceContext
	private EntityManager entityManager;

	protected <T> T saveAndClear(final T entity) {
		final T savedEntity = this.entityManager.merge(entity);
		this.entityManager.flush();
		this.entityManager.clear();
		return savedEntity;
	}

	protected <T> T detach(final T entity) {
		if (entity != null) {
			this.entityManager.detach(entity);
		}
		return entity;
	}

	protected <T> List<T> detachAll(final List<T> entities) {
		for (final T entity : entities) {
			this.entityManager.detach(entity);
		}
		return entities;
	}

	protected <T> Set<T> detachAll(final Set<T> entities) {
		for (final T entity : entities) {
			this.entityManager.detach(entity);
		}
		return entities;
	}

	protected <T> Optional<T> detachOptionalIfPresent(final Optional<T> entity) {
		if (entity.isPresent()) {
			this.entityManager.detach(entity.get());
		}
		return entity;
	}

}