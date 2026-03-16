package org.chenile.jpautils.store;

import org.chenile.utils.entity.model.ChenileEntity;
import org.chenile.utils.entity.service.EntityStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Generic JPA-backed EntityStore for Chenile STM services.
 * Bridges domain model (M) and JPA entity (E) via mapper functions.
 *
 * On update (model has ID), loads the existing entity first to preserve the
 * {@code @Version} field, then applies a merge function before saving.
 */
public class ChenileJpaEntityStore<M, E> implements EntityStore<M> {

    private final JpaRepository<E, String> repository;
    private final Function<E, M> toModel;
    private final Function<M, E> toEntity;
    /**
     * Optional merge function: (existingEntity, newEntityFromModel) -> void.
     * When provided, updates are performed by loading the existing entity and
     * calling mergeEntity to copy the changed fields before saving.
     */
    private BiConsumer<E, E> mergeEntity;

    public ChenileJpaEntityStore(JpaRepository<E, String> repository,
                                  Function<E, M> toModel,
                                  Function<M, E> toEntity) {
        this.repository = repository;
        this.toModel = toModel;
        this.toEntity = toEntity;
    }

    public ChenileJpaEntityStore(JpaRepository<E, String> repository,
                                  Function<E, M> toModel,
                                  Function<M, E> toEntity,
                                  BiConsumer<E, E> mergeEntity) {
        this(repository, toModel, toEntity);
        this.mergeEntity = mergeEntity;
    }

    @Override
    public void store(M model) {
        E entityToSave;

        if (model instanceof ChenileEntity cm && cm.getId() != null) {
            // Update path: load existing entity to preserve @Version
            E existing = repository.findById(cm.getId()).orElse(null);
            if (existing != null) {
                E newEntity = toEntity.apply(model);
                if (mergeEntity != null) {
                    // Use explicit merge function if provided
                    mergeEntity.accept(existing, newEntity);
                    entityToSave = existing;
                } else {
                    // Default: copy version from existing to new entity so JPA
                    // can perform an optimistic-lock-aware merge
                    copyVersionField(existing, newEntity);
                    entityToSave = newEntity;
                }
            } else {
                entityToSave = toEntity.apply(model);
            }
        } else {
            entityToSave = toEntity.apply(model);
        }

        E saved = repository.save(entityToSave);
        // Copy generated ID back to domain model
        M updated = toModel.apply(saved);
        if (model instanceof ChenileEntity cm && updated instanceof ChenileEntity cu) {
            cm.setId(cu.getId());
        }
    }

    @Override
    public M retrieve(String id) {
        return repository.findById(id)
                .map(toModel)
                .orElse(null);
    }

    /** Copies the {@code version} field from source to target via reflection if present. */
    private void copyVersionField(E source, E target) {
        try {
            java.lang.reflect.Field versionField = findField(source.getClass(), "version");
            if (versionField != null) {
                versionField.setAccessible(true);
                versionField.set(target, versionField.get(source));
            }
        } catch (Exception ignored) {
            // If reflection fails, proceed without version copy (optimistic lock may fail)
        }
    }

    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
