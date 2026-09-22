package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Enrollment;
import com.edufast.domain.port.EnrollmentRepository;
import com.edufast.infrastructure.persistence.mapper.EnrollmentMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataEnrollmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ADAPTADOR del puerto EnrollmentRepository.
 */
@Repository
public class EnrollmentRepositoryAdapter implements EnrollmentRepository {

    private final SpringDataEnrollmentRepository jpa;

    public EnrollmentRepositoryAdapter(SpringDataEnrollmentRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Enrollment> findByCourseId(Long courseId) {
        return jpa.findByCourseId(courseId).stream()
                .map(EnrollmentMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByCourseIdAndStudentId(Long courseId, Long studentId) {
        return jpa.existsByCourseIdAndStudentId(courseId, studentId);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        return EnrollmentMapper.toDomain(jpa.save(EnrollmentMapper.toEntity(enrollment)));
    }
}
