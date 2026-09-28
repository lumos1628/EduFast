package com.edufast.mobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.edufast.mobile.EduFastApp
import com.edufast.mobile.domain.model.Section

// La pantalla raíz: decide qué mostrar según el estado de la sesión.
// (equivalente a App.tsx en la web)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as EduFastApp).repository

        setContent {
            var loggedIn by remember { mutableStateOf(false) }
            var section by remember { mutableStateOf<Section?>(null) }

            when {
                !loggedIn -> LoginScreen(
                    repository = repository,
                    onLogin = { loggedIn = true },
                )
                section == null -> SectionListScreen(
                    repository = repository,
                    onSelect = { section = it },
                )
                else -> AttendanceScreen(
                    repository = repository,
                    section = section!!,
                    onBack = { section = null },
                )
            }
        }
    }
}