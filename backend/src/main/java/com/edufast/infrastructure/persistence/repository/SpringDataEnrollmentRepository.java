package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.EnrollmentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Spring Data para matrículas.
 * El @EntityGraph trae curso y alumno en la MISMA consulta (evita el problema N+1).
 */
public interface SpringDataEnrollmentRepository extends JpaRepository<EnrollmentEntity, Long> {

    @EntityGraph(attributePaths = {"course", "student"})
    List<EnrollmentEntity> findByCourseId(Long courseId);

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);
}
