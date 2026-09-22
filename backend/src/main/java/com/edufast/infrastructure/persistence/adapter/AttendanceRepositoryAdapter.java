package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Attendance;
import com.edufast.domain.port.AttendanceRepository;
import com.edufast.infrastructure.persistence.mapper.AttendanceMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataAttendanceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ADAPTADOR del puerto AttendanceRepository.
 */
@Repository
public class AttendanceRepositoryAdapter implements AttendanceRepository {

    private final SpringDataAttendanceRepository jpa;

    public AttendanceRepositoryAdapter(SpringDataAttendanceRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Attendance> findByCourseIdAndDate(Long courseId, LocalDate date) {
        return jpa.findByCourseIdAndDate(courseId, date).stream()
                .map(AttendanceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Attendance> findByCourseIdAndStudentIdAndDate(Long courseId, Long studentId, LocalDate date) {
        return jpa.findByCourseIdAndStudentIdAndDate(courseId, studentId, date)
                .map(AttendanceMapper::toDomain);
    }

    @Override
    public List<Attendance> saveAll(List<Attendance> attendances) {
        return jpa.saveAll(attendances.stream().map(AttendanceMapper::toEntity).toList())
                .stream()
                .map(AttendanceMapper::toDomain)
                .toList();
    }
}
