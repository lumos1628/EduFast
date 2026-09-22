package com.edufast.infrastructure.persistence.entity;

import jakarta.persistence.*;

/**
 * Entidad JPA del curso. El modelo de negocio puro es domain/model/Course.
 */
@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "professor_id")
    private UserEntity professor;

    protected CourseEntity() {
    }

    public CourseEntity(Long id, String name, String code, UserEntity professor) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public UserEntity getProfessor() {
        return professor;
    }
}
