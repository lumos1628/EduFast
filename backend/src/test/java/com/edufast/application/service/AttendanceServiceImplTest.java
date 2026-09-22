package com.edufast.application.service;

import com.edufast.application.dto.AttendanceRequest;
import com.edufast.application.dto.AttendanceResponse;
import com.edufast.domain.exception.NotFoundException;
import com.edufast.domain.model.Attendance;
import com.edufast.domain.model.Course;
import com.edufast.domain.model.Role;
import com.edufast.domain.model.Student;
import com.edufast.domain.model.User;
import com.edufast.domain.port.AttendanceRepository;
import com.edufast.domain.port.EnrollmentRepository;
import com.edufast.domain.port.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 9, 14);

    @Mock
    private CourseService courseService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    private AttendanceServiceImpl attendanceService;

    private User professor;
    private Course course;
    private Student student;

    @BeforeEach
    void setUp() {
        attendanceService = new AttendanceServiceImpl(
                courseService, attendanceRepository, studentRepository, enrollmentRepository);
        professor = new User(1L, "Profesor Demo", "profesor@edufast.com", "hash", Role.PROFESSOR);
        course = new Course(1L, "Matemática", "MAT-101", professor);
        student = new Student(1L, "Ana Torres", "A-001");
    }

    @Test
    void tomarAsistenciaNuevaCreaRegistro() {
        when(courseService.getOwnedCourse(professor, 1L)).thenReturn(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(true);
        when(attendanceRepository.findByCourseIdAndStudentIdAndDate(1L, 1L, FECHA))
                .thenReturn(Optional.empty());
        when(attendanceRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<AttendanceResponse> results = attendanceService.takeAttendance(professor, 1L,
                new AttendanceRequest(FECHA, List.of(new AttendanceRequest.AttendanceEntry(1L, true))));

        assertEquals(1, results.size());
        assertTrue(results.get(0).present());
        assertEquals("Ana Torres", results.get(0).studentName());
        verify(attendanceRepository).saveAll(anyList());
    }

    @Test
    void tomarAsistenciaExistenteActualizaPresente() {
        Attendance existing = new Attendance(5L, course, FECHA, student, true);
        when(courseService.getOwnedCourse(professor, 1L)).thenReturn(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(true);
        when(attendanceRepository.findByCourseIdAndStudentIdAndDate(1L, 1L, FECHA))
                .thenReturn(Optional.of(existing));
        when(attendanceRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<AttendanceResponse> results = attendanceService.takeAttendance(professor, 1L,
                new AttendanceRequest(FECHA, List.of(new AttendanceRequest.AttendanceEntry(1L, false))));

        assertFalse(results.get(0).present());
        assertFalse(existing.isPresent());
    }

    @Test
    void tomarAsistenciaDeAlumnoNoMatriculadoLanzaExcepcion() {
        when(courseService.getOwnedCourse(professor, 1L)).thenReturn(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> attendanceService.takeAttendance(professor, 1L,
                new AttendanceRequest(FECHA, List.of(new AttendanceRequest.AttendanceEntry(1L, true)))));
    }

    @Test
    void tomarAsistenciaConAlumnoDuplicadoUsaLaUltimaEntrada() {
        when(courseService.getOwnedCourse(professor, 1L)).thenReturn(course);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(enrollmentRepository.existsByCourseIdAndStudentId(1L, 1L)).thenReturn(true);
        when(attendanceRepository.findByCourseIdAndStudentIdAndDate(1L, 1L, FECHA))
                .thenReturn(Optional.empty());
        when(attendanceRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<AttendanceResponse> results = attendanceService.takeAttendance(professor, 1L,
                new AttendanceRequest(FECHA, List.of(
                        new AttendanceRequest.AttendanceEntry(1L, true),
                        new AttendanceRequest.AttendanceEntry(1L, false))));

        assertEquals(1, results.size());
        assertFalse(results.get(0).present());
    }

    @Test
    void verAsistenciaFiltraPorCursoYFecha() {
        when(courseService.getOwnedCourse(professor, 1L)).thenReturn(course);
        when(attendanceRepository.findByCourseIdAndDate(1L, FECHA))
                .thenReturn(List.of(new Attendance(5L, course, FECHA, student, true)));

        List<AttendanceResponse> results = attendanceService.getAttendance(professor, 1L, FECHA);

        assertEquals(1, results.size());
        assertTrue(results.get(0).present());
    }
}
