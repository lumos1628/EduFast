package com.edufast.mobile.domain.model

// Entidad de dominio: un curso. No sabe nada de Android ni del backend.
data class Course(
    val id: Long,
    val name: String,
    val code: String,
)