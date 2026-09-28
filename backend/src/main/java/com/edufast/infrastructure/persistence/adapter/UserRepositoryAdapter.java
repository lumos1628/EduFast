package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Role;
import com.edufast.domain.model.User;
import com.edufast.domain.port.UserRepository;
import com.edufast.infrastructure.persistence.mapper.CuentaUsuarioMapper;
import com.edufast.infrastructure.persistence.repository.SpringDataAsignacionRolRepository;
import com.edufast.infrastructure.persistence.repository.SpringDataCuentaUsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Optional;

/**
 * ADAPTADOR del puerto UserRepository.
 * Construye el User a partir de persona + cuenta + primer rol vigente.
 */
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataCuentaUsuarioRepository cuentas;
    private final SpringDataAsignacionRolRepository asignaciones;

    public UserRepositoryAdapter(SpringDataCuentaUsuarioRepository cuentas,
                                 SpringDataAsignacionRolRepository asignaciones) {
        this.cuentas = cuentas;
        this.asignaciones = asignaciones;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return cuentas.findByCorreo(email).flatMap(cuenta -> {
            Role rol = asignaciones.findByCuentaId(cuenta.getId()).stream()
                    .filter(a -> a.getVigenteHasta() == null)
                    .map(a -> rolDeCodigo(a.getRol().getCodigo()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .min(Comparator.comparingInt(Enum::ordinal))
                    .orElse(null);

            if (rol == null) {
                return Optional.empty();
            }
            return Optional.of(CuentaUsuarioMapper.toDomain(cuenta, rol));
        });
    }

    private Optional<Role> rolDeCodigo(String codigo) {
        try {
            return Optional.of(Role.valueOf(codigo));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}