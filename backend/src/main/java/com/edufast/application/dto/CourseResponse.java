package com.edufast.application.dto;

import com.edufast.domain.model.Course;

public record CourseResponse(
        Long id,
        String name,
        String code) {

    public static CourseResponse from(Course course) {
        return new CourseResponse(course.getId(), course.getName(), course.getCode());
    }
}