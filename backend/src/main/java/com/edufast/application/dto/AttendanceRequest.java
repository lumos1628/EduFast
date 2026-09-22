package com.edufast.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AttendanceRequest(
        @NotNull LocalDate date,
        @NotEmpty @Valid List<AttendanceEntry> attendance) {

    public record AttendanceEntry(
            @NotNull Long studentId,
            @NotNull Boolean present) {
    }
}