package com.edufast.infrastructure.persistence.mapper;

import com.edufast.domain.model.User;
import com.edufast.infrastructure.persistence.entity.UserEntity;

/**
 * Traductor entre el modelo de dominio (puro) y la entidad JPA (base de datos).
 * Es la única clase que conoce AMBOS mundos del usuario.
 */
public final class UserMapper {

    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword(), entity.getRole());
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
