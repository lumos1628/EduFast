package com.edufast.infrastructure.persistence.adapter;

import com.edufast.domain.model.Role;
import com.edufast.domain.model.User;
import com.edufast.domain.port.UserRepository;
import com.edufast.infrastructure.persistence.entity.AsignacionRolEntity;
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
            Optional<AsignacionRolEntity> asignacionActiva = asignaciones.findByCuentaId(cuenta.getId()).stream()
                    .filter(a -> a.getVigenteHasta() == null)
                    .filter(a -> rolDeCodigo(a.getRol().getCodigo()).isPresent())
                    .min(Comparator.comparingInt(a ->
                            rolDeCodigo(a.getRol().getCodigo()).orElseThrow().ordinal()));

            if (asignacionActiva.isEmpty()) {
                return Optional.empty();
            }

            AsignacionRolEntity assignment = asignacionActiva.get();
            Role role = rolDeCodigo(assignment.getRol().getCodigo()).orElseThrow();
            String roleScope = role == Role.DIRECTOR
                    ? assignment.getNivelEducativoId() == null ? "INSTITUCION" : "NIVEL_EDUCATIVO"
                    : null;
            Long supervisorUserId = assignment.getReportaA() == null
                    ? null
                    : assignment.getReportaA().getId();

            return Optional.of(CuentaUsuarioMapper.toDomain(
                    cuenta,
                    role,
                    roleScope,
                    assignment.getNivelEducativoId(),
                    supervisorUserId));
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
