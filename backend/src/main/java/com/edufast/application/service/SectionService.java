package com.edufast.application.service;

import com.edufast.application.dto.SectionResponse;
import com.edufast.application.dto.StudentResponse;
import com.edufast.domain.model.Section;
import com.edufast.domain.model.User;

import java.util.List;

public interface SectionService {

    List<SectionResponse> getMySections(User user);

    List<StudentResponse> getSectionStudents(User user, Long sectionId);

    /**
     * Devuelve la sección solo si está asignada al docente.
     */
    Section getAssignedSection(User user, Long sectionId);
}