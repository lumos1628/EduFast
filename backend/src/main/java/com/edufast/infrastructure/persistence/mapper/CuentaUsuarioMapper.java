package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.Role;
import com.edufast.domain.model.User;
import com.edufast.infrastructure.persistence.entity.CuentaUsuarioEntity;

/**
 * Traductor entre CuentaUsuarioEntity y el modelo de dominio User.
 */
public final class CuentaUsuarioMapper {

    private CuentaUsuarioMapper() {
    }

    public static User toDomain(CuentaUsuarioEntity entity, Role role) {
        return new User(
                entity.getId(),
                entity.getPersona().getNombreCompleto(),
                entity.getCorreo(),
                entity.getPasswordHash(),
                role);
    }
}