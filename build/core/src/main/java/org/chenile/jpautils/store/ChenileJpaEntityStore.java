package org.chenile.jpautils.store;

import org.chenile.utils.entity.service.EntityStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.Function;

/**
 * Generic JPA-backed EntityStore for Chenile STM services.
 * Bridges domain model (M) and JPA entity (E) via mapper functions.
 */
public class ChenileJpaEntityStore<M, E> implements EntityStore<M> {

    private final JpaRepository<E, String> repository;
    private final Function<E, M> toModel;
    private final Function<M, E> toEntity;

    public ChenileJpaEntityStore(JpaRepository<E, String> repository,
                                  Function<E, M> toModel,
                                  Function<M, E> toEntity) {
        this.repository = repository;
        this.toModel = toModel;
        this.toEntity = toEntity;
    }

    @Override
    public void store(M model) {
        E entity = toEntity.apply(model);
        repository.save(entity);
    }

    @Override
    public M retrieve(String id) {
        return repository.findById(id)
                .map(toModel)
                .orElse(null);
    }
}
