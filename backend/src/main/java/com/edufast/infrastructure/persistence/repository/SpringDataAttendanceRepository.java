package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data para asistencias.
 * El @EntityGraph trae curso y alumno en la MISMA consulta (evita el problema N+1).
 */
public interface SpringDataAttendanceRepository extends JpaRepository<AttendanceEntity, Long> {

    @EntityGraph(attributePaths = {"course", "student"})
    List<AttendanceEntity> findByCourseIdAndDate(Long courseId, LocalDate date);

    @EntityGraph(attributePaths = {"course", "student"})
    Optional<AttendanceEntity> findByCourseIdAndStudentIdAndDate(Long courseId, Long studentId, LocalDate date);
}
