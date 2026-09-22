package com.edufast.infrastructure.config;

import com.edufast.domain.model.Course;
import com.edufast.domain.model.Enrollment;
import com.edufast.domain.model.Role;
import com.edufast.domain.model.Student;
import com.edufast.domain.model.User;
import com.edufast.domain.port.CourseRepository;
import com.edufast.domain.port.EnrollmentRepository;
import com.edufast.domain.port.PasswordHasher;
import com.edufast.domain.port.StudentRepository;
import com.edufast.domain.port.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Carga datos de demostración al arrancar (solo si el profesor demo no existe).
 * Usa los PUERTOS del dominio, no las clases de infraestructura.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordHasher passwordHasher;

    public DataSeeder(UserRepository userRepository,
                      CourseRepository courseRepository,
                      StudentRepository studentRepository,
                      EnrollmentRepository enrollmentRepository,
                      PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("profesor@edufast.com").isPresent()) {
            return;
        }

        User professor = userRepository.save(new User(null, "Profesor Demo", "profesor@edufast.com",
                passwordHasher.hash("123456"), Role.PROFESSOR));

        Course matematica = courseRepository.save(new Course(null, "Matemática", "MAT-101", professor));
        Course comunicacion = courseRepository.save(new Course(null, "Comunicación", "COM-102", professor));
        Course ciencia = courseRepository.save(new Course(null, "Ciencia y Tecnología", "CYT-103", professor));

        List<Student> students = studentRepository.saveAll(List.of(
                new Student(null, "Ana Torres", "A-001"),
                new Student(null, "Bruno Flores", "A-002"),
                new Student(null, "Carmen Ruiz", "A-003"),
                new Student(null, "Diego Salas", "A-004"),
                new Student(null, "Elena Paredes", "A-005"),
                new Student(null, "Franco Vega", "A-006"),
                new Student(null, "Gloria Chávez", "A-007"),
                new Student(null, "Hugo Ríos", "A-008"),
                new Student(null, "Irene Quispe", "A-009"),
                new Student(null, "Jorge Mamani", "A-010")));

        for (Student student : students) {
            enrollmentRepository.save(new Enrollment(null, matematica, student));
            enrollmentRepository.save(new Enrollment(null, comunicacion, student));
            if (student.getId() % 2 == 0) {
                enrollmentRepository.save(new Enrollment(null, ciencia, student));
            }
        }

        log.info("=======================================");
        log.info("Datos de demostración cargados:");
        log.info("  Profesor: profesor@edufast.com / 123456");
        log.info("  Cursos: Matemática, Comunicación, Ciencia y Tecnología");
        log.info("  10 alumnos matriculados.");
        log.info("=======================================");
    }
}
