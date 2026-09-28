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

### Base de datos local (desarrollo)
```sql
CREATE ROLE edufast WITH LOGIN PASSWORD 'edufast123' BYPASSRLS;
CREATE DATABASE edufast OWNER edufast;
```
Aplica migraciones y semilla sobre esa base:
```bash
psql -d edufast -f supabase/migrations/20260928000100_base_escolar.sql
psql -d edufast -f supabase/migrations/20260928000200_curriculo_planificacion.sql
psql -d edufast -f supabase/migrations/20260928000300_actividades_notificaciones.sql
psql -d edufast -f supabase/seed.sql
```
El rol `edufast` usa `BYPASSRLS` para comportarse como el rol de servicio del backend. Hibernate valida el esquema, no lo modifica.

### Migraciones Supabase del esquema

Con Supabase CLI instalado y Docker activo, ejecuta localmente:

```bash
supabase start
supabase db reset
```

Para aplicar migraciones al proyecto Supabase compartido de desarrollo, autentícate y enlázalo:

```bash
npx supabase@latest login
npx supabase@latest link --project-ref <DEV_PROJECT_REF>
npx supabase@latest db push --dry-run
npx supabase@latest db push
```

`db push` aplica las migraciones; la semilla se ejecuta aparte y solo en desarrollo. Copia la URI PostgreSQL desde Supabase → **Database → Connect** y ejecútala una vez:

```bash
psql "$SUPABASE_DB_URL" -v ON_ERROR_STOP=1 -f supabase/seed.sql
```

La semilla crea datos sintéticos: 22 secciones, 396 estudiantes y cuentas demo. Es idempotente para permitir reinicializar el entorno de desarrollo. No ejecutes la semilla en producción ni guardes contraseñas o claves en Git. El backend se conecta a PostgreSQL con credenciales privadas; web y Android solo llaman a la API Spring. RLS y los permisos bloquean el acceso directo de clientes Supabase (`anon`/`authenticated`), no el acceso autorizado del backend.

### Variables de entorno del backend actual
El prototipo funciona con la base local por defecto. En producción, define:

| Variable | Qué es | Default (solo dev) |
|---|---|---|
| `DB_URL` | URL de PostgreSQL | `jdbc:postgresql://localhost:5432/edufast` |
| `DB_USER` | Usuario de la BD | `edufast` |
| `DB_PASSWORD` | Contraseña de la BD | `edufast123` |
| `JWT_SECRET` | Clave para firmar tokens | valor de desarrollo |

Para el Supabase compartido, copia `backend/.env.example` a `backend/.env`, completa los valores con los datos de conexión del proyecto y expórtalos antes de iniciar el backend:

```bash
cp backend/.env.example backend/.env
set -a
source backend/.env
set +a
cd backend && ./gradlew bootRun
```

`backend/.env` está excluido de Git. Cada colaborador necesita acceso al proyecto Supabase de desarrollo y recibe sus credenciales por un canal privado; el `clone` del repositorio no concede acceso al proyecto. Mantén producción en un proyecto separado y no cargues allí la semilla demo.

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
Requiere **Android Studio**. Abrir la carpeta `mobile/`, sincronizar Gradle y correr en un emulador. La app se conecta a `http://10.0.2.2:8080`.

### Datos de prueba
```
Docente:   profesor@edufast.com / 123456   (asignado a 4.º de primaria, sección A)
Apoderado: apoderado@edufast.com / 123456  (vinculado a un estudiante de esa sección)
```
Los datos los crea `supabase/seed.sql` solo en desarrollo.
