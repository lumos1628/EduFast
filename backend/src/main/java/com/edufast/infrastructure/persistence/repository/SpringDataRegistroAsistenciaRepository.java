package com.edufast.infrastructure.persistence.repository;

import com.edufast.infrastructure.persistence.entity.RegistroAsistenciaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataRegistroAsistenciaRepository extends JpaRepository<RegistroAsistenciaEntity, Long> {

    @EntityGraph(attributePaths = {"ubicacionMatricula", "ubicacionMatricula.matricula",
            "ubicacionMatricula.matricula.estudiante", "ubicacionMatricula.matricula.estudiante.persona"})
    List<RegistroAsistenciaEntity> findByJornadaId(Long jornadaId);

    void deleteByJornadaId(Long jornadaId);
}