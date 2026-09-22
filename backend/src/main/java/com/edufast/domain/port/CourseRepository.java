package com.edufast.domain.port;

import com.edufast.domain.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * PUERTO: el contrato que el dominio necesita para persistir cursos.
 */
public interface CourseRepository {

    List<Course> findByProfessorId(Long professorId);

    Optional<Course> findById(Long id);

    Course save(Course course);
}
