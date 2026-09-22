package com.edufast.infrastructure.controller;

import com.edufast.application.dto.CourseResponse;
import com.edufast.application.dto.StudentResponse;
import com.edufast.application.service.CourseService;
import com.edufast.domain.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/me")
    public List<CourseResponse> myCourses(@AuthenticationPrincipal User user) {
        return courseService.getMyCourses(user);
    }

    @GetMapping("/{courseId}/students")
    public List<StudentResponse> students(@AuthenticationPrincipal User user,
                                          @PathVariable Long courseId) {
        return courseService.getCourseStudents(user, courseId);
    }
}