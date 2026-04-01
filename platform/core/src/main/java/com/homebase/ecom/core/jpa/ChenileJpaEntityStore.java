package com.homebase.ecom.core.jpa;

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
            E existing = repository.findById(cm.getId()).orElse(null);
            if (existing != null) {
                E newEntity = toEntity.apply(model);
                if (mergeEntity != null) {
                    mergeEntity.accept(existing, newEntity);
                    entityToSave = existing;
                } else {
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

    private void copyVersionField(E source, E target) {
        try {
            java.lang.reflect.Field versionField = findField(source.getClass(), "version");
            if (versionField != null) {
                versionField.setAccessible(true);
                versionField.set(target, versionField.get(source));
            }
        } catch (Exception ignored) {
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
