package com.edufast.application.service;

import com.edufast.application.dto.CourseResponse;
import com.edufast.application.dto.StudentResponse;
import com.edufast.domain.model.Course;
import com.edufast.domain.model.User;

import java.util.List;

public interface CourseService {

    List<CourseResponse> getMyCourses(User user);

    List<StudentResponse> getCourseStudents(User user, Long courseId);

    /**
     * Devuelve el curso solo si pertenece al profesor.
     * Lanza NotFoundException si no existe y ForbiddenException si es de otro profesor.
     * Otros servicios (como asistencia) lo usan para proteger sus operaciones.
     */
    Course getOwnedCourse(User user, Long courseId);
}
