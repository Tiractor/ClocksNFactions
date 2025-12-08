package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.layout.imePadding

@Composable
fun EditNoteDialog(
                   initialName: String = "",
                   initialNote: String = "",
                   title: String = "Редактировать",
                   onDismiss: () -> Unit,
                   onSave: (name: String, note: String) -> Unit,
                   widthDp: Dp = 360.dp,
                   heightDp: Dp = 240.dp
) {
    var name by remember { mutableStateOf(initialName) }
    var note by remember { mutableStateOf(initialNote) }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = {
            focusManager.clearFocus()
            onDismiss()
        },
        title = { Text(text = title) },
        text = {
            Box(
                modifier = Modifier
                    .width(widthDp)
                    .height(heightDp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    // Имя — single line
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text("Название") },
                        singleLine = true,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Комментарий — занимает оставшееся пространство, реагирует на IME
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .imePadding(),
                        label = { Text("Комментарий (опционально)") },
                        placeholder = { Text("Например: главный штаб в порту") },
                        singleLine = false,
                        maxLines = 8
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            focusManager.clearFocus()
                            onDismiss()
                        }) {
                            Text("Отмена")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(onClick = {
                            focusManager.clearFocus()
                            keyboard?.hide()
                            onSave(name.trim(), note.trim())
                        }) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        },
        buttons = { /* кнопки внутри body */ },
        properties = DialogProperties(usePlatformDefaultWidth = true)
    )
}