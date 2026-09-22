package com.edufast.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA de la matrícula. El modelo de negocio puro es domain/model/Enrollment.
 * Un alumno solo puede estar matriculado una vez por curso (constraint único).
 */
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "student_id"}))
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    protected EnrollmentEntity() {
    }

    public EnrollmentEntity(Long id, CourseEntity course, StudentEntity student) {
        this.id = id;
        this.course = course;
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public StudentEntity getStudent() {
        return student;
    }
}
