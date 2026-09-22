package com.edufast.application.service;

import com.edufast.application.dto.CourseResponse;
import com.edufast.application.dto.StudentResponse;
import com.edufast.domain.exception.ForbiddenException;
import com.edufast.domain.exception.NotFoundException;
import com.edufast.domain.model.Course;
import com.edufast.domain.model.Enrollment;
import com.edufast.domain.model.User;
import com.edufast.domain.port.CourseRepository;
import com.edufast.domain.port.EnrollmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseServiceImpl(CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getMyCourses(User user) {
        return courseRepository.findByProfessorId(user.getId()).stream()
                .map(CourseResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getCourseStudents(User user, Long courseId) {
        Course course = getOwnedCourse(user, courseId);
        return enrollmentRepository.findByCourseId(course.getId()).stream()
                .map(Enrollment::getStudent)
                .map(StudentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Course getOwnedCourse(User user, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        if (!course.getProfessor().getId().equals(user.getId())) {
            throw new ForbiddenException("No tienes acceso a este curso");
        }
        return course;
    }
}
