package com.edufast.domain.model;

/**
 * Modelo de dominio PURO: sin anotaciones de frameworks.
 * Representa la matrícula de un alumno en un curso.
 */
public class Enrollment {

    private final Long id;
    private final Course course;
    private final Student student;

    public Enrollment(Long id, Course course, Student student) {
        this.id = id;
        this.course = course;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public Student getStudent() {
        return student;
    }
}
