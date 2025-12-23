package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clocksnfactions.data.local.entities.ClockEntity
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun ClockItem(
    clock: ClockEntity,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: (ClockEntity) -> Unit
) {
    var showEdit by remember { mutableStateOf(false) }
    val preview = clock.note?.takeIf { it.isNotBlank() }?.let {
        if (it.length > 160) it.take(160) + "…" else it
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column() {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = if (clock.name.isNotBlank()) clock.name else "Счётчик")
                    }
                    TextButton(onClick = onDelete) {
                        Text("Удалить", color = Color.Red)
                    }

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    ClockView(
                    clock = clock,
                    onIncrement = onInc,
                    onDecrement = onDec,
                    modifier = Modifier.size(120.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { showEdit = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать заметку")
                    }
                    if (!preview.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = preview, color = Color.Gray, fontSize = 13.sp)
                    }
                }


            }


        }
    }

    if (showEdit) {
        EditDialog(
            initialName = clock.name,
            initialNote = clock.note ?: "",
            title = "Редактирование",
            onDismiss = { showEdit = false },
            onSave = { newName, newNote ->
                onUpdate(clock.copy(name = newName, note = newNote.ifBlank { null }))
                showEdit = false
            }
        )
    }
}