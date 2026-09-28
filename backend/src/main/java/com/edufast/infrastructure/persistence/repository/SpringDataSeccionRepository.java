package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.SeccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataSeccionRepository extends JpaRepository<SeccionEntity, Long> {
}