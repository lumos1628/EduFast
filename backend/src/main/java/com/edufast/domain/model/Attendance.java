package com.edufast.domain.model;

import java.time.LocalDate;

/**
 * Modelo de dominio PURO: estado de asistencia de un alumno en una fecha.
 */
public class Attendance {

    private final Long studentId;
    private final LocalDate date;
    private boolean present;

    public Attendance(Long studentId, LocalDate date, boolean present) {
        this.studentId = studentId;
        this.date = date;
        this.present = present;
    }

    public Long getStudentId() {
        return studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}