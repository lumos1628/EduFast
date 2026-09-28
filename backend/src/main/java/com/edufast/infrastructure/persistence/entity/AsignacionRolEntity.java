package com.edufast.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "asignaciones_roles")
public class AsignacionRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_usuario_id")
    private CuentaUsuarioEntity cuenta;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id")
    private RolEntity rol;

    @Column(name = "vigente_desde", nullable = false)
    private LocalDate vigenteDesde;

    @Column(name = "vigente_hasta")
    private LocalDate vigenteHasta;

    protected AsignacionRolEntity() {
    }

    public AsignacionRolEntity(Long id, CuentaUsuarioEntity cuenta, RolEntity rol) {
        this.id = id;
        this.cuenta = cuenta;
        this.rol = rol;
        this.vigenteDesde = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public CuentaUsuarioEntity getCuenta() {
        return cuenta;
    }

    public RolEntity getRol() {
        return rol;
    }

    public LocalDate getVigenteDesde() {
        return vigenteDesde;
    }

    public LocalDate getVigenteHasta() {
        return vigenteHasta;
    }
}