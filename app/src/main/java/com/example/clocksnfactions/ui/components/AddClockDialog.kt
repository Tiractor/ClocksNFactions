package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddClockDialog(onDismiss: () -> Unit, onCreate: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var custom by remember { mutableStateOf("") }
    var preset by remember { mutableStateOf(6) } // default

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Новый счётчик") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Название (опционально)") })
                Spacer(modifier = Modifier.height(8.dp))
                Text("Выберите количество секторов:")
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    listOf(4, 6, 8).forEach { v ->
                        val selected = (preset == v)
                        OutlinedButton(onClick = { preset = v }, modifier = Modifier.padding(end = 8.dp)) {
                            Text("$v")
                        }
                    }
                }
                OutlinedTextField(
                    value = custom,
                    onValueChange = { custom = it },
                    placeholder = { Text("Другое") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val seg = custom.toIntOrNull() ?: preset
                val finalSeg = if (seg >= 3) seg else preset
                onCreate(name.trim(), finalSeg)
                onDismiss()
            }) { Text("Создать") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}
