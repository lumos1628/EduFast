package com.edufast.domain.port;

import com.edufast.domain.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * PUERTO: el contrato que el dominio necesita para persistir alumnos.
 */
public interface StudentRepository {

    Optional<Student> findById(Long id);

    List<Student> saveAll(List<Student> students);
}
