package com.edufast.application.service;

import com.edufast.application.dto.AttendanceRequest;
import com.edufast.application.dto.AttendanceResponse;
import com.edufast.domain.model.User;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    List<AttendanceResponse> takeAttendance(User user, Long courseId, AttendanceRequest request);

    List<AttendanceResponse> getAttendance(User user, Long courseId, LocalDate date);
}