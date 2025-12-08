package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clocksnfactions.data.local.entities.ClockEntity
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ClockListRow(
    clock: ClockEntity,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onDelete: () -> Unit,
    onUpdateNote: (String?) -> Unit
) {
    var showEditNote by remember { mutableStateOf(false) }
    // Короткое превью заметки (если есть)
    val preview = clock.note?.takeIf { it.isNotBlank() }?.let {
        if (it.length > 160) it.take(160) + "…" else it
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = if (clock.name.isNotBlank()) clock.name else "Счётчик")
                    }

                    IconButton(onClick = { showEditNote = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать заметку")
                    }
                }


                ClockView(
                    clock = clock,
                    onIncrement = onInc,
                    onDecrement = onDec,
                    modifier = Modifier.size(120.dp)
                )

                if (!preview.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = preview, color = Color.Gray, fontSize = 13.sp)
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }

    // ДИАЛОГ РЕДАКТИРОВАНИЯ КОММЕНТАРИЯ
    if (showEditNote) {
        EditNoteDialog(
            initialText = clock.note,
            title = "Комментарий к фракции",
            onDismiss = { showEditNote = false },
            onSave = { newNote ->
                // Сохраняем null если строка пустая, чтобы не хранить пустые строки
                val normalized: String? = newNote.takeIf { it.isNotBlank() }
                onUpdateNote(normalized)
                showEditNote = false
            },
            widthDp = 360.dp,
            heightDp = 220.dp
        )
    }
}