-- Datos completamente sintéticos para desarrollo: 6 grados de primaria,
-- 5 de secundaria, dos secciones por grado y 18 estudiantes por sección.
do $$
declare
    v_institucion_id bigint;
    v_anio_id bigint;
    v_seccion record;
    v_numero integer;
    v_codigo varchar(40);
    v_persona_id bigint;
    v_estudiante_id bigint;
    v_matricula_id bigint;
begin
    insert into public.instituciones_educativas
        (codigo_modular, codigo_local, nombre, tipo_gestion)
    values
        ('DEMO-EDUFAST', 'DEMO-LOCAL', 'Institución Educativa Demo EduFast', 'PUBLICA')
    on conflict (codigo_modular) do update
        set nombre = excluded.nombre
    returning id into v_institucion_id;

    insert into public.anios_escolares
        (institucion_id, anio, fecha_inicio, fecha_fin, estado)
    values
        (v_institucion_id, 2026, date '2026-03-16', date '2026-12-18', 'ACTIVO')
    on conflict (institucion_id, anio) do update
        set fecha_inicio = excluded.fecha_inicio,
            fecha_fin = excluded.fecha_fin
    returning id into v_anio_id;

    insert into public.secciones
        (anio_escolar_id, institucion_id, grado_id, nombre, capacidad_maxima, activa)
    select v_anio_id, v_institucion_id, g.id, s.nombre, 18, true
    from public.grados_educativos g
    cross join (values ('A'), ('B')) as s(nombre)
    on conflict (institucion_id, anio_escolar_id, grado_id, nombre) do nothing;

    for v_seccion in
        select s.id as seccion_id, s.nombre as seccion_nombre,
               g.codigo as grado_codigo, g.nombre as grado_nombre
        from public.secciones s
        join public.grados_educativos g on g.id = s.grado_id
        where s.anio_escolar_id = v_anio_id
        order by g.nivel_id, g.numero, s.nombre
    loop
        for v_numero in 1..18 loop
            v_codigo := format('DEMO-%s-%s-%s', v_seccion.grado_codigo,
                               v_seccion.seccion_nombre, lpad(v_numero::text, 3, '0'));

            insert into public.personas
                (nombres, apellidos, tipo_documento, numero_documento)
            values
                ('Estudiante', v_codigo, 'CODIGO_DEMO', v_codigo)
            on conflict (tipo_documento, numero_documento) do update
                set apellidos = excluded.apellidos
            returning id into v_persona_id;

            insert into public.estudiantes (persona_id, codigo_estudiante)
            values (v_persona_id, v_codigo)
            on conflict (codigo_estudiante) do update
                set persona_id = excluded.persona_id
            returning id into v_estudiante_id;

            insert into public.matriculas
                (estudiante_id, anio_escolar_id, institucion_id, estado)
            values
                (v_estudiante_id, v_anio_id, v_institucion_id, 'ACTIVA')
            on conflict (estudiante_id, anio_escolar_id) do update
                set estado = 'ACTIVA'
            returning id into v_matricula_id;

            insert into public.ubicaciones_matricula
                (matricula_id, seccion_id, fecha_inicio, motivo)
            values
                (v_matricula_id, v_seccion.seccion_id,
                 date '2026-03-16', 'MATRICULA_INICIAL')
            on conflict (matricula_id, fecha_inicio) do nothing;
        end loop;
    end loop;
end $$;

-- Cuentas de prueba con acceso a EduFast. La contraseña es "123456" (BCrypt).
do $$
declare
    v_institucion_id bigint;
    v_anio_id bigint;
    v_seccion_4a bigint;
    v_persona_docente bigint;
    v_cuenta_docente bigint;
    v_persona_apoderado bigint;
    v_cuenta_apoderado bigint;
    v_apoderado_id bigint;
    v_estudiante_demo bigint;
begin
    select id into v_institucion_id from public.instituciones_educativas
    where codigo_modular = 'DEMO-EDUFAST' limit 1;
    select id into v_anio_id from public.anios_escolares
    where institucion_id = v_institucion_id and anio = 2026 limit 1;

    select s.id into v_seccion_4a
    from public.secciones s
    join public.grados_educativos g on g.id = s.grado_id
    where s.anio_escolar_id = v_anio_id and g.codigo = 'PRI-04' and s.nombre = 'A';

    insert into public.personas (nombres, apellidos, tipo_documento, numero_documento)
    values ('Profesora', 'Demo EduFast', 'CODIGO_DEMO', 'DEMO-DOCENTE')
    on conflict (tipo_documento, numero_documento) do update
        set apellidos = excluded.apellidos
    returning id into v_persona_docente;

    insert into public.cuentas_usuario (persona_id, correo, password_hash, estado)
    values (v_persona_docente, 'profesor@edufast.com',
            '$2a$10$hBeuLCCsKcNtiaqDesBbfe64Y1TnQgi9baZy5oGxy1qb66X4kICX6', 'ACTIVA')
    on conflict (correo) do update
        set password_hash = excluded.password_hash
    returning id into v_cuenta_docente;

    insert into public.asignaciones_roles (cuenta_usuario_id, rol_id, institucion_id, anio_escolar_id)
    select v_cuenta_docente, r.id, v_institucion_id, v_anio_id
    from public.roles r where r.codigo = 'DOCENTE';

    insert into public.asignaciones_docentes (cuenta_usuario_id, seccion_id, es_tutor)
    values (v_cuenta_docente, v_seccion_4a, true)
    on conflict (cuenta_usuario_id, seccion_id, vigente_desde) do nothing;

    insert into public.personas (nombres, apellidos, tipo_documento, numero_documento)
    values ('Apoderado', 'Demo EduFast', 'CODIGO_DEMO', 'DEMO-APODERADO')
    on conflict (tipo_documento, numero_documento) do update
        set apellidos = excluded.apellidos
    returning id into v_persona_apoderado;

    insert into public.cuentas_usuario (persona_id, correo, password_hash, estado)
    values (v_persona_apoderado, 'apoderado@edufast.com',
            '$2a$10$hBeuLCCsKcNtiaqDesBbfe64Y1TnQgi9baZy5oGxy1qb66X4kICX6', 'ACTIVA')
    on conflict (correo) do update
        set password_hash = excluded.password_hash
    returning id into v_cuenta_apoderado;

    insert into public.apoderados (persona_id, cuenta_usuario_id)
    values (v_persona_apoderado, v_cuenta_apoderado)
    on conflict (persona_id) do nothing
    returning id into v_apoderado_id;

    if v_apoderado_id is null then
        select id into v_apoderado_id from public.apoderados where persona_id = v_persona_apoderado;
    end if;

    insert into public.contactos_persona (persona_id, tipo, valor, verificado_at, es_principal, activo)
    values (v_persona_apoderado, 'TELEFONO', '+51999999999', now(), true, true)
    on conflict (persona_id, tipo, valor) do nothing;

    insert into public.asignaciones_roles (cuenta_usuario_id, rol_id, institucion_id, anio_escolar_id)
    select v_cuenta_apoderado, r.id, v_institucion_id, v_anio_id
    from public.roles r where r.codigo = 'APODERADO';

    select e.id into v_estudiante_demo
    from public.estudiantes e
    where e.codigo_estudiante = 'DEMO-PRI-04-A-001' limit 1;

    insert into public.vinculos_apoderado_estudiante
        (apoderado_id, estudiante_id, parentesco, puede_consultar, recibe_notificaciones, verificado_at)
    values (v_apoderado_id, v_estudiante_demo, 'MADRE', true, true, now())
    on conflict (apoderado_id, estudiante_id, vigente_desde) do nothing;
end $$;
