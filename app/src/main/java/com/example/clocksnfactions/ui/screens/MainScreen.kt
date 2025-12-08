package com.example.clocksnfactions.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clocksnfactions.ui.viewmodel.FactionViewModel
import com.example.clocksnfactions.ui.components.FactionItem
import com.example.clocksnfactions.ui.components.AddFactionDialog
/**
 * MainScreen — показывает список фракций. По клику на фракцию открывает экран деталей.
 */
@Composable
fun MainScreen(viewModel: FactionViewModel) {
    val factions by viewModel.factions.collectAsState()
    var showAddFaction by remember { mutableStateOf(false) }

    // selectedFactionId = null -> показываем список; иначе показываем детали
    var selectedFactionId by remember { mutableStateOf<Long?>(null) }
    var selectedFactionName by remember { mutableStateOf<String?>(null) }

    if (selectedFactionId != null) {
        // Переходим в экран деталей фракции
        FactionDetailsScreen(
            factionId = selectedFactionId!!,
            factionName = selectedFactionName ?: "Фракция",
            onBack = { selectedFactionId = null }
        )
        return
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Clocks & Factions") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddFaction = true }) {
                Text("+")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            if (factions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Пока нет фракций. Нажми + чтобы добавить.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(factions, key = { it.id }) { f ->
                        FactionItem(
                            faction = f,
                            onRankChange = { delta -> viewModel.updateRank(f, delta) },
                            onToggleControl = { viewModel.toggleControl(f) },
                            onRelationshipChange = { delta -> viewModel.updateRelationship(f, delta) },
                            onDelete = { viewModel.deleteFaction(f) },
                            onClick = {
                                selectedFactionId = f.id
                                selectedFactionName = f.name
                            },
                            onUpdateNote = { note -> viewModel.updateNoteForFaction(f, note) }
                        )
                    }
                }
            }
        }

        if (showAddFaction) {
            AddFactionDialog(
                onAdd = { name ->
                    viewModel.addFaction(name)
                    showAddFaction = false
                },
                onCancel = { showAddFaction = false }
            )
        }
    }
}
