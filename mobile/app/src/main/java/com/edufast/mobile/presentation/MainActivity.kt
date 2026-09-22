package com.edufast.mobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.edufast.mobile.EduFastApp
import com.edufast.mobile.domain.model.Course

// La pantalla raíz: decide qué mostrar según el estado de la sesión.
// (equivalente a App.tsx en la web)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as EduFastApp).repository

        setContent {
            var loggedIn by remember { mutableStateOf(false) }
            var course by remember { mutableStateOf<Course?>(null) }

            when {
                !loggedIn -> LoginScreen(
                    repository = repository,
                    onLogin = { loggedIn = true },
                )
                course == null -> CourseListScreen(
                    repository = repository,
                    onSelect = { course = it },
                )
                else -> AttendanceScreen(
                    repository = repository,
                    course = course!!,
                    onBack = { course = null },
                )
            }
        }
    }
}