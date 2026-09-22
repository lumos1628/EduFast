package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Student;
import com.edufast.domain.port.StudentRepository;
import com.edufast.infrastructure.persistence.mapper.StudentMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataStudentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ADAPTADOR del puerto StudentRepository.
 */
@Repository
public class StudentRepositoryAdapter implements StudentRepository {

    private final SpringDataStudentRepository jpa;

    public StudentRepositoryAdapter(SpringDataStudentRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jpa.findById(id).map(StudentMapper::toDomain);
    }

    @Override
    public List<Student> saveAll(List<Student> students) {
        return jpa.saveAll(students.stream().map(StudentMapper::toEntity).toList())
                .stream()
                .map(StudentMapper::toDomain)
                .toList();
    }
}
