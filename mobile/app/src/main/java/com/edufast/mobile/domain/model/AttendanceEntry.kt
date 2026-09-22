package com.edufast.mobile.domain.model

// Entidad de dominio: un registro de asistencia (alumno presente/ausente en una fecha).
data class AttendanceEntry(
    val studentId: Long,
    val studentName: String,
    val date: String,
    val present: Boolean,
)