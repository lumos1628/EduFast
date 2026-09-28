package com.edufast.application.dto;

import java.time.LocalDate;

public record AttendanceResponse(
        Long studentId,
        String studentName,
        LocalDate date,
        boolean present) {
}