package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data: genera el SQL solo a partir del nombre del método.
 * Nadie lo usa directamente fuera de UserRepositoryAdapter.
 */
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
