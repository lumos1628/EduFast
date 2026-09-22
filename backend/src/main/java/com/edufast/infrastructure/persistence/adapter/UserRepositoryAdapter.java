package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.User;
import com.edufast.domain.port.UserRepository;
import com.edufast.infrastructure.persistence.mapper.UserMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ADAPTADOR del puerto UserRepository.
 * Conecta el dominio (que solo conoce la interfaz) con Spring Data JPA.
 * Si mañana cambia la base de datos, solo cambia esta clase.
 */
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository jpa;

    public UserRepositoryAdapter(SpringDataUserRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpa.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(jpa.save(UserMapper.toEntity(user)));
    }
}
