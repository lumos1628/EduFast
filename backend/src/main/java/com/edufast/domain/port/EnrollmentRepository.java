package com.edufast.domain.port;

import com.edufast.domain.model.Enrollment;

import java.util.List;

/**
 * PUERTO: el contrato que el dominio necesita para persistir matrículas.
 */
public interface EnrollmentRepository {

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    Enrollment save(Enrollment enrollment);
}
