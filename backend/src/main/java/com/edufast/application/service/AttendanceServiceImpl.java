package com.edufast.application.service;

import com.edufast.application.dto.AttendanceRequest;
import com.edufast.application.dto.AttendanceResponse;
import com.edufast.domain.exception.NotFoundException;
import com.edufast.domain.model.Attendance;
import com.edufast.domain.model.Course;
import com.edufast.domain.model.Student;
import com.edufast.domain.model.User;
import com.edufast.domain.port.AttendanceRepository;
import com.edufast.domain.port.EnrollmentRepository;
import com.edufast.domain.port.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final CourseService courseService;
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceServiceImpl(CourseService courseService,
                                 AttendanceRepository attendanceRepository,
                                 StudentRepository studentRepository,
                                 EnrollmentRepository enrollmentRepository) {
        this.courseService = courseService;
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    @Transactional
    public List<AttendanceResponse> takeAttendance(User user, Long courseId, AttendanceRequest request) {
        Course course = courseService.getOwnedCourse(user, courseId);
        List<Attendance> toSave = new ArrayList<>();

        for (AttendanceRequest.AttendanceEntry entry : deduplicate(request.attendance())) {
            Student student = studentRepository.findById(entry.studentId())
                    .orElseThrow(() -> new NotFoundException("Alumno no encontrado"));

            if (!enrollmentRepository.existsByCourseIdAndStudentId(courseId, student.getId())) {
                throw new NotFoundException("El alumno no está matriculado en este curso");
            }

            Attendance attendance = attendanceRepository
                    .findByCourseIdAndStudentIdAndDate(courseId, student.getId(), request.date())
                    .orElseGet(() -> new Attendance(null, course, request.date(), student, entry.present()));

            attendance.setPresent(entry.present());
            toSave.add(attendance);
        }

        return attendanceRepository.saveAll(toSave).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendance(User user, Long courseId, LocalDate date) {
        courseService.getOwnedCourse(user, courseId);
        return attendanceRepository.findByCourseIdAndDate(courseId, date).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    /**
     * Si el request trae el mismo alumno dos veces, gana la última entrada.
     * Evita violar el constraint único (curso + fecha + alumno) de la base de datos.
     */
    private List<AttendanceRequest.AttendanceEntry> deduplicate(List<AttendanceRequest.AttendanceEntry> entries) {
        Map<Long, AttendanceRequest.AttendanceEntry> byStudent = new LinkedHashMap<>();
        for (AttendanceRequest.AttendanceEntry entry : entries) {
            byStudent.put(entry.studentId(), entry);
        }
        return List.copyOf(byStudent.values());
    }
}
