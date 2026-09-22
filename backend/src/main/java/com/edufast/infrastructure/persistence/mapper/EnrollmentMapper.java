package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Enrollment;
import com.edufast.infrastructure.persistence.entity.EnrollmentEntity;

/**
 * Traductor entre domain/model/Enrollment y EnrollmentEntity.
 */
public final class EnrollmentMapper {

    private EnrollmentMapper() {
    }

    public static Enrollment toDomain(EnrollmentEntity entity) {
        return new Enrollment(entity.getId(),
                CourseMapper.toDomain(entity.getCourse()),
                StudentMapper.toDomain(entity.getStudent()));
    }

    public static EnrollmentEntity toEntity(Enrollment enrollment) {
        return new EnrollmentEntity(enrollment.getId(),
                CourseMapper.toEntity(enrollment.getCourse()),
                StudentMapper.toEntity(enrollment.getStudent()));
    }
}
