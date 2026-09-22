package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Spring Data para alumnos.
 */
public interface SpringDataStudentRepository extends JpaRepository<StudentEntity, Long> {
}
