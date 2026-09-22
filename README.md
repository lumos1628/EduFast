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
> *"Diseñar cosas sencillas, rápidas y que funcionen **sin internet o con pocos datos**, porque el wifi es casi mito."*
>
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

### Estructura del monorepo

```
eduFast/
├── backend/         ← Java + Spring Boot (API REST)
├── mobile/          ← Kotlin + Android (esqueleto)
├── web/             ← React + Vite + TypeScript
├── ARCHITECTURE.md  ← 📚 la arquitectura explicada desde cero
├── AGENTS.md        ← 🤖 reglas obligatorias para agentes de IA y colaboradores
└── README.md
```

---

## 📚 Documentación

| Documento | Para quién | Contenido |
|---|---|---|
| `ARCHITECTURE.md` | Personas que aprenden arquitectura | La arquitectura explicada con analogías, el viaje de una petición real, evaluación honesta, tutorial para agregar features y glosario |
| `AGENTS.md` | Agentes de IA y colaboradores nuevos | Reglas obligatorias, dónde va cada archivo, checklist para agregar features, errores a evitar |

---

## 🧭 Roadmap (post-MVP)

1. Registro de notas por competencia (dolor #1)
2. Conclusiones descriptivas con IA (dolor #2)
3. Alertas automáticas a padres (dolor #4)
4. Planificación de unidades/sesiones (dolor #5)
5. Modo offline con sincronización (requisito clave: "el wifi es casi mito")

---

## ▶️ Cómo correr el proyecto

### Requisitos
- Java 25, Node 20+, PostgreSQL 16 (local, puerto 5432).

### Base de datos (una sola vez)
```sql
CREATE ROLE edufast WITH LOGIN PASSWORD 'edufast123';
CREATE DATABASE edufast OWNER edufast;
```
Las tablas se crean solas (JPA) y se cargan datos de ejemplo al arrancar.

### Variables de entorno (opcional en desarrollo)
El proyecto funciona sin configurar nada (hay valores por defecto para desarrollo). En producción, define:

| Variable | Qué es | Default (solo dev) |
|---|---|---|
| `DB_URL` | URL de PostgreSQL | `jdbc:postgresql://localhost:5432/edufast` |
| `DB_USER` | Usuario de la BD | `edufast` |
| `DB_PASSWORD` | Contraseña de la BD | `edufast123` |
| `JWT_SECRET` | Clave para firmar tokens | valor de desarrollo |

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
Email: profesor@edufast.com
Password: 123456
Cursos: Matemática, Comunicación, Ciencia y Tecnología (10 alumnos)
```
