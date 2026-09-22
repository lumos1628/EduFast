package com.edufast.mobile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edufast.mobile.domain.model.Course
import com.edufast.mobile.domain.repository.EduFastRepository
import kotlinx.coroutines.launch

// Pantalla de cursos. Igual que CourseList.tsx en la web.
@Composable
fun CourseListScreen(
    repository: EduFastRepository,
    onSelect: (Course) -> Unit,
) {
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            courses = repository.getMyCourses()
        } catch (e: Exception) {
            error = "No se pudieron cargar tus cursos. Revisa la conexión."
        } finally {
            loading = false
        }
    }

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Tus cursos", style = MaterialTheme.typography.headlineSmall)

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(courses) { course ->
                        Card(onClick = { onSelect(course) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(course.name, style = MaterialTheme.typography.titleMedium)
                                Text(course.code, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}