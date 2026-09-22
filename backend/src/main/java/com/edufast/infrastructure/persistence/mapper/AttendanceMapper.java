package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Attendance;
import com.edufast.infrastructure.persistence.entity.AttendanceEntity;

/**
 * Traductor entre domain/model/Attendance y AttendanceEntity.
 */
public final class AttendanceMapper {

    private AttendanceMapper() {
    }

    public static Attendance toDomain(AttendanceEntity entity) {
        return new Attendance(entity.getId(),
                CourseMapper.toDomain(entity.getCourse()),
                entity.getDate(),
                StudentMapper.toDomain(entity.getStudent()),
                entity.isPresent());
    }

    public static AttendanceEntity toEntity(Attendance attendance) {
        return new AttendanceEntity(attendance.getId(),
                CourseMapper.toEntity(attendance.getCourse()),
                attendance.getDate(),
                StudentMapper.toEntity(attendance.getStudent()),
                attendance.isPresent());
    }
}
