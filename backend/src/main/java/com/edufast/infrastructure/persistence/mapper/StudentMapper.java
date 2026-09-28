package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Student;
import com.edufast.infrastructure.persistence.entity.EstudianteEntity;

/**
 * Traductor entre EstudianteEntity y el modelo de dominio Student.
 */
public final class StudentMapper {

    private StudentMapper() {
    }

    public static Student toDomain(EstudianteEntity entity) {
        return new Student(
                entity.getId(),
                entity.getPersona().getNombreCompleto(),
                entity.getCodigoEstudiante());
    }
}