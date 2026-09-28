package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Section;
import com.edufast.infrastructure.persistence.entity.SeccionEntity;

/**
 * Traductor entre SeccionEntity y el modelo de dominio Section.
 */
public final class SectionMapper {

    private SectionMapper() {
    }

    public static Section toDomain(SeccionEntity entity) {
        return new Section(
                entity.getId(),
                entity.getNombre(),
                entity.getGrado().getNombre(),
                entity.getGrado().getCodigo());
    }
}