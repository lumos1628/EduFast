package com.edufast.domain.port;

import com.edufast.domain.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * PUERTO: el contrato que el dominio necesita para persistir asistencias.
 */
public interface AttendanceRepository {

    List<Attendance> findByCourseIdAndDate(Long courseId, LocalDate date);

    Optional<Attendance> findByCourseIdAndStudentIdAndDate(Long courseId, Long studentId, LocalDate date);

    List<Attendance> saveAll(List<Attendance> attendances);
}
