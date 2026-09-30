# EduFast

> Plataforma educativa para docentes peruanos. **MVP actual: asistencia digital** (tomar y visualizar por web y móvil).

---

## 🎯 El problema: dolores reales de los docentes (encuestas)

Hicimos **2 encuestas a docentes** (8 + 4 respuestas, incluyendo docentes de institución pública de primaria en Arequipa y Cajamarca). Estos son los 6 dolores identificados:

### 🔴 Dolor 1: Registrar notas y pasarlas al SIAGIE
- **20-40 min** por actividad para registrar la nota de todos los alumnos.
- Cerrar bimestre toma **1, 2-3, 4, 5 horas... hasta "1 día"** en el SIAGIE.
- Registran primero en **cuaderno físico o Excel** → *"una nota del cuaderno no coincide con la del SIAGIE"*.

### 🔴 Dolor 2: Redactar conclusiones descriptivas (el más fuerte)
- Toma **2 a 5 horas... hasta 10+ horas (más de un día)** por bimestre para todos los alumnos.
- 3 de 4 docentes de pública lo marcan como la parte **más difícil/lenta del SIAGIE**.

### 🟠 Dolor 3: Asistencia en papel
- Casi todos usan **"Lista en papel"**; toma **2 a 15 minutos por sesión**.
- Los padres **no se enteran a tiempo** de las faltas.
- **Todos** los encuestados quieren **alertas automáticas** de inasistencias/notas bajas.

### 🟠 Dolor 4: Comunicación con padres
- Solo en reuniones presenciales o WhatsApp; **30 min a 1 hora/semana**.

### 🟡 Dolor 5: Planificar unidades y sesiones
- **2 a 5 horas/semana** planificando. Quieren **generación automática de sesiones**.

### 🟡 Dolor 6: Generar reportes
- Libreta de un curso completo: **1 a 3+ horas** en Excel.

### 💬 Mensajes literales de los docentes
> *"Herramientas que nos sistematicen el llenado de **notas, listas de asistencia** y generación de **informes pedagógicos**."*

*(Los archivos de las encuestas no están en el repo por privacidad.)*

---

## 🚀 MVP actual

**Solo asistencia**: el docente se loguea, ve sus cursos, toma asistencia (web o móvil) y la visualiza.

| Feature | Dolor que ataca |
|---|---|
| Tomar asistencia desde web y móvil | #3 (asistencia en papel) |
| Visualizar asistencia desde web y móvil | #3 |

---

## 🏗️ Arquitectura

Monorepo con **Clean Architecture estricta** (dominio 100% puro, puertos y adaptadores). Las 3 piezas se comunican por **HTTP + JSON**; los frontends **nunca tocan la base de datos**.

```
┌────────────┐  HTTP + JSON  ┌──────────────────┐   JPA/SQL   ┌────────────┐
│  React     │ ────────────► │ Backend (Spring) │ ──────────► │ PostgreSQL │
│  (web)     │ ◄──────────── │ (Java + Tomcat)  │ ◄────────── │  edufast   │
└────────────┘               └──────────────────┘             └────────────┘
┌────────────┐      ▲          un solo backend
│  Kotlin    │ ─────┘          sirve a los dos
│  (móvil)   │ ◄─────          frontends
└────────────┘     HTTP + JSON
```

El backend está organizado en 3 capas con dependencias que solo apuntan hacia adentro:

```
infrastructure/  (controller, persistence, security)  ← el mundo exterior
     │
     ▼
application/     (service, dto)                       ← la lógica de negocio
     │
     ▼
domain/          (model, port, exception)             ← puro, sin frameworks
```

Las reglas de capas están protegidas por **tests automáticos de arquitectura** (ArchUnit): si alguien las rompe, `./gradlew test` falla.

El modelo escolar está versionado en `supabase/migrations/` y la API usa matrícula por año, grado y sección. Los endpoints de asistencia operan sobre secciones (`/api/v1/sections/...`); las tablas se crean con migraciones y Hibernate valida el esquema al arrancar (`ddl-auto=validate`).

### Estructura del monorepo

```
eduFast/
├── backend/         ← Java + Spring Boot (API REST)
├── mobile/          ← Kotlin + Android (esqueleto)
├── web/             ← React + Vite + TypeScript
├── ARCHITECTURE.md  ← 📚 la arquitectura explicada desde cero
├── USE_CASES.md     ← flujos de la primera entrega
├── DATABASE.md      ← ERD, reglas y diccionario de datos
├── supabase/        ← migraciones y semilla sintética
├── AGENTS.md        ← 🤖 reglas obligatorias para agentes de IA y colaboradores
└── README.md
```

---

## 📚 Documentación

| Documento | Para quién | Contenido |
|---|---|---|
| `ARCHITECTURE.md` | Personas que aprenden arquitectura | La arquitectura explicada con analogías, el viaje de una petición real, evaluación honesta, tutorial para agregar features y glosario |
| `USE_CASES.md` | Producto y desarrollo | Actores, flujos, excepciones y alcance de la primera entrega |
| `DATABASE.md` | Producto y desarrollo | Diagrama ER, tablas, restricciones, seguridad y migraciones |
| `AGENTS.md` | Agentes de IA y colaboradores nuevos | Reglas obligatorias, dónde va cada archivo, checklist para agregar features, errores a evitar |

---

## 🧭 Roadmap (post-MVP)

1. Alinear la API de asistencia con matrícula por año, grado y sección.
2. Push y consulta segura para apoderados.
3. Publicación y seguimiento de actividades para casa.
4. Planificación de unidades/sesiones y evaluación por competencias.
5. IA, simuladores y repositorio multimedia según futuras épicas.

---

## ▶️ Cómo correr el proyecto

### Requisitos
- Java 25, Node 20+, PostgreSQL 16 (local, puerto 5432).

### Configuración para colaboradores

1. Copia la plantilla y completa los valores privados:
   ```bash
   cp backend/.env.example backend/.env
   ```
   Gradle carga `backend/.env` solo para `./gradlew test` y `./gradlew bootRun`; no hace falta exportarlo a mano. Si vas a ejecutar comandos `psql` o `supabase` CLI, expórtalo tú:
   ```bash
   set -a && source backend/.env && set +a
   ```
2. El backend ya apunta por defecto al **Supabase de desarrollo** (el host y el usuario no son secretos y viven en `application.properties`). La plantilla trae los **valores compartidos** (`EDUFAST_DEMO_EMAIL_DOMAIN=example.test`, `EDUFAST_DEMO_PASSWORD=demo123`). Solo debes completar los campos **privados** en `backend/.env`:
   - `DB_PASSWORD`: contraseña del Supabase de desarrollo, que se comparte por un canal privado. El `clone` del repositorio **no** concede acceso al proyecto.
   - `SUPABASE_DB_URL`: URI para `psql` (supabase/seed.sql), también privada.
   - `JWT_SECRET`: genera uno propio con `openssl rand -hex 32`.
3. No re-ejecutes la semilla en la base compartida sin acordarlo, porque cambiaría los datos para todo el equipo.

> Para trabajar contra un **Postgres local**, define `DB_URL`, `DB_USER` y `DB_PASSWORD` en `backend/.env`; esos valores ganan sobre el default de Supabase.

### Base de datos local (desarrollo)
```sql
CREATE ROLE edufast WITH LOGIN PASSWORD 'edufast123' BYPASSRLS;
CREATE DATABASE edufast OWNER edufast;
```
Si el rol `edufast` ya existía con otra contraseña, alínealo en vez de crearlo:
```sql
ALTER ROLE edufast WITH LOGIN PASSWORD 'edufast123' BYPASSRLS;
```
Aplica migraciones y semilla sobre esa base:
```bash
psql -d edufast -f supabase/migrations/20260928000100_base_escolar.sql
psql -d edufast -f supabase/migrations/20260928000200_curriculo_planificacion.sql
psql -d edufast -f supabase/migrations/20260928000300_actividades_notificaciones.sql
psql -d edufast -f supabase/migrations/20260928000400_roles_y_alcances_personal.sql
cp backend/.env.example backend/.env
set -a && source backend/.env && set +a
psql -d edufast -f supabase/seed.sql
```
El rol `edufast` usa `BYPASSRLS` para comportarse como el rol de servicio del backend. Hibernate valida el esquema, no lo modifica.

### Migraciones Supabase del esquema

Con Supabase CLI instalado y Docker activo, ejecuta localmente:

```bash
cp backend/.env.example backend/.env
# Completa los valores locales en backend/.env antes de continuar.
set -a
source backend/.env
set +a
supabase start
supabase db reset
psql 'postgresql://postgres:postgres@127.0.0.1:54322/postgres' \
  -v ON_ERROR_STOP=1 -f supabase/seed.sql
```

Para aplicar migraciones al proyecto Supabase compartido de desarrollo, autentícate y enlázalo:

```bash
npx supabase@latest login
npx supabase@latest link --project-ref <DEV_PROJECT_REF>
npx supabase@latest db push --dry-run
npx supabase@latest db push
cp backend/.env.example backend/.env
# Completa las credenciales y los parámetros locales en backend/.env.
set -a
source backend/.env
set +a
psql "$SUPABASE_DB_URL" -v ON_ERROR_STOP=1 -f supabase/seed.sql
```

`db push` aplica el esquema; la semilla se ejecuta por separado con `psql` y solo en desarrollo, porque toma su configuración del entorno local. Genera 10 estudiantes por sección por defecto y una cuenta de apoderado por familia; las parejas de hermanos y el tamaño de cada sección son parámetros locales. También genera cuentas demo de docentes, directores y secretaría, con correos del dominio de prueba configurado y una contraseña que defines localmente. No se crean cuentas para estudiantes ni se guardan personas o contraseñas en Git. No cargues la semilla en producción. El backend se conecta a PostgreSQL con credenciales privadas; web y Android solo llaman a la API Spring.

### Variables de entorno del backend

| Variable | Tipo | Qué es | Valor |
|---|---|---|---|
| `DB_URL` | Default en repo | URL JDBC de PostgreSQL | Supabase dev; override en `backend/.env` para local |
| `DB_USER` | Default en repo | Usuario de la BD | `edufast`; override en `backend/.env` para local |
| `DB_PASSWORD` | Privado | Contraseña de la BD | canal privado (obligatoria) |
| `SUPABASE_DB_URL` | Privado | URI para ejecutar la semilla | Supabase → Connect |
| `JWT_SECRET` | Privado | Clave para firmar tokens | `openssl rand -hex 32` |
| `EDUFAST_DEMO_EMAIL_DOMAIN` | Compartido | Dominio de correos demo | `example.test` |
| `EDUFAST_DEMO_PASSWORD` | Compartido | Contraseña de las cuentas demo | `demo123` |
| `EDUFAST_DEMO_STUDENTS_PER_SECTION` | Compartido | Alumnos por sección en la semilla | `10` |
| `EDUFAST_DEMO_SIBLING_PAIRS` | Compartido | Pares de hermanos en la semilla | `10` |

Inicia el backend (Gradle carga `backend/.env` automáticamente):

```bash
cd backend && ./gradlew bootRun
```

`backend/.env` está excluido de Git. Los valores privados se comparten por un canal seguro; el `clone` del repositorio no concede acceso a Supabase. Mantén producción en un proyecto separado y no cargues allí la semilla demo.

### 1. Backend (Java + Spring Boot)
```bash
cd backend
./gradlew bootRun
```
- API: `http://localhost:8080`
- Swagger (docs interactivas): `http://localhost:8080/swagger-ui/index.html`
- Tests (incluyen las reglas de arquitectura): `./gradlew test`

### 2. Web (React + Vite)
```bash
cd web
npm install     # primera vez
npm run dev
```
- Web: `http://localhost:5173` (el proxy reenvía `/api` al backend)

### 3. Móvil (Kotlin + Android)
Requiere **Android Studio** y **JDK 21** para compilar. Abrir la carpeta `mobile/`, sincronizar Gradle y correr en un emulador.

- La URL de la API vive en `BuildConfig.API_BASE_URL` (no está en el código). En debug apunta a `http://10.0.2.2:8080`; se puede sobreescribir con la propiedad Gradle `edufast.debugApiBaseUrl`. Para release, define `edufast.apiBaseUrl`.
- El HTTP en claro al backend local solo se habilita en debug (`mobile/app/src/debug/AndroidManifest.xml`); release no lo incluye.

### Datos de prueba
```
Dominio:     example.test
Contraseña:  demo123

Personal:    personal.docente.regular@example.test
             personal.docente.auxiliar@example.test
             personal.director.primaria@example.test
             personal.director.general@example.test
             personal.secretaria@example.test
Apoderados:  apoderado.hermanos.0001@example.test (y los generados por familia)
```
El docente regular está asignado a **4.º de primaria - A**. Los datos sintéticos los genera `supabase/seed.sql` solo en desarrollo. Revisa `backend/.env.example` para los parámetros configurables.
