package com.homebase.ecom.user.infrastructure.persistence.adapter;

import com.homebase.ecom.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for UserJpaEntity.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {
    
    Optional<UserJpaEntity> findByKeycloakId(String keycloakId);
    
    Optional<UserJpaEntity> findByEmail(String email);

    java.util.List<UserJpaEntity> findByCurrentStateAndCreatedTimeBefore(String currentState, java.time.Instant cutoff);
}
