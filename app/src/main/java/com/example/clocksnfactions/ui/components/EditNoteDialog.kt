package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditNoteDialog(
    initialText: String?,
    title: String = "Комментарий",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    widthDp: Dp = 340.dp,
    heightDp: Dp = 220.dp
) {
    var text by remember { mutableStateOf(initialText ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(widthDp)
                .height(heightDp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = title, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                // Отдельный контейнер фиксированного размера:
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                ) {
                    // Поле ввода с ограничением на строки и placeholder
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .imePadding(), // чтобы содержимое не перекрывалось клавиатурой
                        placeholder = { Text("Например: главный штаб в порту") },
                        singleLine = false,
                        maxLines = 6
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Отмена") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onSave(text.trim()) }) { Text("Сохранить") }
                }
            }
        }
    }
}