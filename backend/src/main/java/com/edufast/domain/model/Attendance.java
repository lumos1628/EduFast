package com.edufast.domain.model;

import java.time.LocalDate;

/**
 * Modelo de dominio PURO: sin anotaciones de frameworks.
 * Registro de asistencia de un alumno en un curso y fecha.
 */
public class Attendance {

    private final Long id;
    private final Course course;
    private final LocalDate date;
    private final Student student;
    private boolean present;

    public Attendance(Long id, Course course, LocalDate date, Student student, boolean present) {
        this.id = id;
        this.course = course;
        this.date = date;
        this.student = student;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getDate() {
        return date;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
