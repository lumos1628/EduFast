package com.edufast.domain.port;

import com.edufast.domain.model.User;

/**
 * PUERTO: genera tokens de sesión para un usuario autenticado.
 * La implementación (JWT) vive en infrastructure/security.
 * Así la capa application no depende de ninguna librería de seguridad.
 */
public interface TokenProvider {

    String generateToken(User user);
}
