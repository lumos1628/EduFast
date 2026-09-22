package com.edufast.application.dto;

import com.edufast.domain.model.Attendance;

import java.time.LocalDate;

public record AttendanceResponse(
        Long studentId,
        String studentName,
        LocalDate date,
        boolean present) {

    public static AttendanceResponse from(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getStudent().getId(),
                attendance.getStudent().getName(),
                attendance.getDate(),
                attendance.isPresent());
    }
}