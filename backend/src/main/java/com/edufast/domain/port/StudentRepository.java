package com.edufast.domain.port;

import com.edufast.domain.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * PUERTO: el contrato que el negocio necesita para persistir estudiantes.
 */
public interface StudentRepository {

    Optional<Student> findById(Long id);

    List<Student> findBySectionId(Long sectionId);

    Student save(Student student);
}