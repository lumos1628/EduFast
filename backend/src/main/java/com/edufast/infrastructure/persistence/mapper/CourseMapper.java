package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Course;
import com.edufast.infrastructure.persistence.entity.CourseEntity;

/**
 * Traductor entre domain/model/Course y CourseEntity.
 */
public final class CourseMapper {

    private CourseMapper() {
    }

    public static Course toDomain(CourseEntity entity) {
        return new Course(entity.getId(), entity.getName(), entity.getCode(), UserMapper.toDomain(entity.getProfessor()));
    }

    public static CourseEntity toEntity(Course course) {
        return new CourseEntity(course.getId(), course.getName(), course.getCode(), UserMapper.toEntity(course.getProfessor()));
    }
}
