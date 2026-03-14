package com.homebase.ecom.user.infrastructure.persistence.adapter;

import com.homebase.ecom.user.domain.model.User;
import com.homebase.ecom.user.domain.port.UserRepository;
import com.homebase.ecom.user.infrastructure.persistence.entity.UserJpaEntity;
import com.homebase.ecom.user.infrastructure.persistence.mapper.UserMapper;

import java.util.Optional;

/**
 * Implementation of domain UserRepository port.
 * Delegates to Spring Data UserJpaRepository and maps between domain/JPA.
 * No Spring annotations here per gemini.md rules. Wired in UserConfiguration.
 */
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public Optional<User> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public Optional<User> findByKeycloakId(String keycloakId) {
        return jpaRepository.findByKeycloakId(keycloakId).map(mapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toModel);
    }

    @Override
    public java.util.List<User> findPendingVerificationOlderThan(java.time.Instant cutoff) {
        return jpaRepository.findByCurrentStateAndCreatedTimeBefore("PENDING_VERIFICATION", cutoff)
                .stream()
                .map(mapper::toModel)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
