package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Course;
import com.edufast.domain.port.CourseRepository;
import com.edufast.infrastructure.persistence.mapper.CourseMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataCourseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ADAPTADOR del puerto CourseRepository.
 */
@Repository
public class CourseRepositoryAdapter implements CourseRepository {

    private final SpringDataCourseRepository jpa;

    public CourseRepositoryAdapter(SpringDataCourseRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Course> findByProfessorId(Long professorId) {
        return jpa.findByProfessorId(professorId).stream()
                .map(CourseMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Course> findById(Long id) {
        return jpa.findById(id).map(CourseMapper::toDomain);
    }

    @Override
    public Course save(Course course) {
        return CourseMapper.toDomain(jpa.save(CourseMapper.toEntity(course)));
    }
}
