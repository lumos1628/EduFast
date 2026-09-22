package com.edufast.infrastructure.controller;

import com.edufast.application.dto.AttendanceRequest;
import com.edufast.application.dto.AttendanceResponse;
import com.edufast.application.service.AttendanceService;
import com.edufast.domain.model.User;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public List<AttendanceResponse> save(@AuthenticationPrincipal User user,
                                         @PathVariable Long courseId,
                                         @Valid @RequestBody AttendanceRequest request) {
        return attendanceService.takeAttendance(user, courseId, request);
    }

    @GetMapping
    public List<AttendanceResponse> get(@AuthenticationPrincipal User user,
                                        @PathVariable Long courseId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.getAttendance(user, courseId, date);
    }
}