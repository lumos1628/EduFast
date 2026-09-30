**|** **Casos de uso — primera entrega de EduFast**  
*Alcance: asistencia por sección, vínculo seguro con apoderados, notificaciones push y seguimiento básico de actividades para casa. No incluye notas, chat libre, IA, simuladores ni repositorio multimedia.*  
**Actores**  
| | |  
|-|-|  
| **Actor** | **Responsabilidad** |   
| Docente | Consulta sus secciones, registra y confirma asistencia, publica actividades y registra entregas en papel o digitales. |   
| Apoderado autorizado | Consulta a los estudiantes vinculados y recibe las notificaciones permitidas. |   
| Director/administrador de la IE | Gestiona año escolar, secciones, matrícula, docentes y vínculos de apoderados. |   
| Servicio de notificaciones | Encola, envía, reintenta y registra el resultado de cada push. |   
| App Android / web instalada | Registra dispositivos y presenta avisos del sistema operativo. La web necesita permiso y Service Worker. |   
   
Un estudiante puede estar registrado sin tener una cuenta. Una persona puede tener más de un rol; por ejemplo, ser docente y apoderada.  
**Casos de uso**  
**UC-01 — Consultar secciones y lista del día**  
- **Actor:** docente.  
- **Precondición:** la cuenta está activa y asignada a la sección durante el año escolar.  
- **Flujo:** el docente elige el año, la sección y la fecha; el sistema devuelve la lista según las matrículas y ubicaciones vigentes en esa fecha.  
- **Variantes:** si un estudiante cambió de sección, aparece únicamente en la sección válida para la fecha consultada.  
**UC-02 — Registrar y confirmar asistencia del día**  
- **Actor:** docente.  
- **Flujo:** el docente selecciona su sección y la fecha; el sistema presenta a los estudiantes matriculados en esa sección para la fecha consultada. El docente marca presente/ausente y guarda la lista.  
- **Regla:** registrar asistencia no crea ni cambia una matrícula. El guardado confirma la jornada del día: solo las jornadas confirmadas quedan como registro oficial.  
- **Fallos de red:** los registros se guardan directamente en el servidor. Si la conexión falla, se muestra un error y se permite reintentar; no hay almacenamiento local.  
**UC-03 — Consultar la asistencia registrada**  
- **Actor:** docente o apoderado autorizado.  
- **Flujo:** consulta la asistencia de una sección y fecha (docente) o de un estudiante vinculado (apoderado).  
- **Regla:** si aún no hay registro para esa fecha, la consulta devuelve lista vacía y la app presenta a los alumnos sin estado confirmado.  
**UC-04 — Corregir asistencia**  
- **Actor:** docente autorizado o administrador.  
- **Flujo:** corrige un registro, indica el motivo y guarda quién/cuándo realizó el cambio.  
- **Variantes:** si la ausencia ya se notificó, el sistema registra un evento de corrección; no borra el evento anterior ni duplica el aviso original.  
**UC-05 — Vincular un apoderado con un estudiante**  
- **Actor:** director/administrador.  
- **Flujo:** registra la relación, tipo de vínculo, vigencia y permisos para consultar información y recibir avisos. Verifica al menos un medio de contacto antes de activar notificaciones.  
- **Variantes:** un estudiante puede tener varios apoderados; una persona puede estar vinculada a varios estudiantes. El vínculo puede vencer o revocarse sin borrar su historial.  
**UC-06 — Registrar dispositivo y preferencias**  
- **Actor:** apoderado.  
- **Flujo:** inicia sesión en Android o web instalada, autoriza notificaciones y registra el token del dispositivo. Puede activar/desactivar categorías.  
- **Variantes:** el permiso puede denegarse o revocarse; en ese caso, las notificaciones permanecen en la bandeja interna de EduFast si la cuenta está habilitada.  
**UC-07 — Notificar ausencia confirmada**  
- **Actor:** servicio de notificaciones.  
- **Disparador:** asistencia confirmada con uno o más registros de ausencia.  
- **Flujo:** resuelve los apoderados vigentes y autorizados, aplica sus preferencias, crea una notificación por destinatario y una entrega por canal/dispositivo.  
- **Idempotencia:** volver a procesar el mismo evento no envía duplicados.  
- **Fuera de cobertura:** un teléfono realmente apagado no puede mostrar avisos; el proveedor puede retenerlos hasta que vuelva a conectarse, sujeto a expiración.  
**UC-08 — Abrir y consultar una notificación**  
- **Actor:** apoderado autorizado.  
- **Flujo:** toca el aviso del sistema, inicia sesión si hace falta y abre el detalle del estudiante y fecha. El servidor vuelve a verificar el vínculo vigente.  
- **Privacidad:** el texto mostrado en la pantalla bloqueada debe revelar lo mínimo; el detalle se obtiene dentro de una sesión autenticada.  
**UC-09 — Consultar historial de asistencia**  
- **Actor:** docente o apoderado autorizado.  
- **Flujo:** consulta asistencias por fecha/período. La respuesta se limita a las secciones asignadas al docente o a los estudiantes asociados al apoderado.  
**UC-10 — Publicar una actividad para casa**  
- **Actor:** docente.  
- **Flujo:** publica una actividad a una o más secciones, opcionalmente a estudiantes individuales, con instrucciones, fecha límite, recursos y si requiere entrega.  
- **Regla pedagógica:** la actividad puede ser digital, en papel, oral o revisada presencialmente. No toda actividad requiere subir un archivo.  
**UC-11 — Registrar y revisar una entrega**  
- **Actor:** docente; el estudiante/apoderado puede aportar contenido si tiene acceso.  
- **Flujo:** registra una entrega digital o marca manualmente que recibió el trabajo en papel/en clase; agrega retroalimentación si corresponde.  
- **Regla:** ausencia de archivo no equivale a “no hizo la tarea”. La docente confirma el estado de no entrega cuando corresponda.  
**UC-12 — Notificar una actividad o una entrega pendiente**  
- **Actor:** servicio de notificaciones.  
- **Disparadores configurables:** actividad publicada, vencimiento próximo o no entrega confirmada por la docente.  
- **Reglas:** solo se avisa a apoderados autorizados; se respeta la preferencia por categoría; los avisos vencidos expiran y los reintentos no duplican envíos.  
**Reglas transversales**  
1. La matrícula es por institución/año/grado/sección, nunca por curso individual.  
2. Asistencia, actividad para casa y evaluación por competencias son registros diferentes.  
3. Un estado sin confirmar no produce avisos.  
4. Las correcciones y cambios de matrícula conservan historial.  
5. Los registros se guardan directamente en el servidor; si falla la conexión, se informa el error y se permite reintentar.  
6. La entrega de un push por el proveedor no significa que el apoderado lo haya leído.  
7. Los estudiantes de prueba son sintéticos; no se cargan datos personales reales de menores.  
**Fuera de la primera entrega**  
Notas y niveles de logro, informes oficiales, chat libre, generación con IA, simuladores, video, integración automática con SIAGIE y cuentas de estudiante.  
