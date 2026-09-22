package com.edufast.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entidad JPA de la asistencia. El modelo de negocio puro es domain/model/Attendance.
 * Solo puede existir un registro por curso + fecha + alumno (constraint único).
 */
@Entity
@Table(name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "date", "student_id"}))
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @Column(nullable = false)
    private boolean present;

    protected AttendanceEntity() {
    }

    public AttendanceEntity(Long id, CourseEntity course, LocalDate date, StudentEntity student, boolean present) {
        this.id = id;
        this.course = course;
        this.date = date;
        this.student = student;
        this.present = present;
    }

    public Long getId() {
        return id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public LocalDate getDate() {
        return date;
    }

    public StudentEntity getStudent() {
        return student;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
