package com.edufast.domain.port;

import com.edufast.domain.model.User;

import java.util.Optional;

/**
 * PUERTO: el contrato que el dominio necesita para persistir usuarios.
 * La implementación (adaptador JPA) vive en infrastructure.
 */
public interface UserRepository {

    Optional<User> findByEmail(String email);

    User save(User user);
}
