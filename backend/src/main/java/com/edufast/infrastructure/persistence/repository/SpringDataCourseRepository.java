package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio de Spring Data para cursos.
 * El @EntityGraph trae al profesor en la MISMA consulta (evita el problema N+1).
 */
public interface SpringDataCourseRepository extends JpaRepository<CourseEntity, Long> {

    @EntityGraph(attributePaths = "professor")
    List<CourseEntity> findByProfessorId(Long professorId);
}
