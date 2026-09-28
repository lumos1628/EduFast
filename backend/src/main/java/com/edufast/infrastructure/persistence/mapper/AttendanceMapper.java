package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Attendance;
import com.edufast.infrastructure.persistence.entity.RegistroAsistenciaEntity;

import java.time.LocalDate;

/**
 * Traductor entre RegistroAsistenciaEntity y el modelo de dominio Attendance.
 */
public final class AttendanceMapper {

    private AttendanceMapper() {
    }

    public static Attendance toDomain(RegistroAsistenciaEntity entity) {
        LocalDate fecha = entity.getJornada().getFecha();
        Long studentId = entity.getUbicacionMatricula().getMatricula().getEstudiante().getId();
        boolean present = "PRESENTE".equals(entity.getEstado()) || "TARDANZA".equals(entity.getEstado());
        return new Attendance(studentId, fecha, present);
    }
}