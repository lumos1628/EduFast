package com.edufast.domain.model;

/**
 * Modelo de dominio PURO: sin anotaciones de frameworks.
 */
public class Course {

    private final Long id;
    private final String name;
    private final String code;
    private final User professor;

    public Course(Long id, String name, String code, User professor) {
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

    public User getProfessor() {
        return professor;
    }
}
