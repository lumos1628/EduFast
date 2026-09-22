package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Student;
import com.edufast.infrastructure.persistence.entity.StudentEntity;

/**
 * Traductor entre domain/model/Student y StudentEntity.
 */
public final class StudentMapper {

    private StudentMapper() {
    }

    public static Student toDomain(StudentEntity entity) {
        return new Student(entity.getId(), entity.getName(), entity.getCode());
    }

    public static StudentEntity toEntity(Student student) {
        return new StudentEntity(student.getId(), student.getName(), student.getCode());
    }
}
