package com.edufast.mobile.domain.repository

import com.edufast.mobile.domain.model.AttendanceEntry
import com.edufast.mobile.domain.model.Course
import com.edufast.mobile.domain.model.Student

// PUERTO del dominio: el contrato que la app necesita.
// La implementación (ApiEduFastRepository en data/) habla con el backend.
// Si mañana cambia el backend, solo cambia la implementación, no este contrato.
interface EduFastRepository {

    suspend fun login(email: String, password: String): String

    suspend fun getMyCourses(): List<Course>

    suspend fun getCourseStudents(courseId: Long): List<Student>

    suspend fun saveAttendance(courseId: Long, date: String, presentByStudent: Map<Long, Boolean>): List<AttendanceEntry>

    suspend fun getAttendance(courseId: Long, date: String): List<AttendanceEntry>
}