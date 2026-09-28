package com.edufast.domain.port;

import com.edufast.domain.model.User;

import java.util.Optional;

/**
 * PUERTO: el contrato que el negocio necesita para autenticar usuarios.
 * El adaptador construye el User a partir de persona + cuenta + rol.
 */
public interface UserRepository {

    Optional<User> findByEmail(String email);
}