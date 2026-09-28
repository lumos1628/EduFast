package com.edufast.mobile.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edufast.mobile.domain.model.Section
import com.edufast.mobile.domain.repository.EduFastRepository
import kotlinx.coroutines.launch

// Pantalla de secciones. Igual que SectionList.tsx en la web.
@Composable
fun SectionListScreen(
    repository: EduFastRepository,
    onSelect: (Section) -> Unit,
) {
    var sections by remember { mutableStateOf<List<Section>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            sections = repository.getMySections()
        } catch (e: Exception) {
            error = "No se pudieron cargar tus secciones. Revisa la conexión."
        } finally {
            loading = false
        }
    }

    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Mis secciones", style = MaterialTheme.typography.headlineSmall)

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            if (loading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(sections) { section ->
                        Card(onClick = { onSelect(section) }) {
                            Column(Modifier.padding(16.dp)) {
                                Text(section.descripcion, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}