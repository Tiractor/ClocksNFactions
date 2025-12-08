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
    initialText: String?,
    title: String = "Комментарий",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    widthDp: Dp = 340.dp,
    heightDp: Dp = 220.dp
) {
    var text by remember { mutableStateOf(initialText ?: "") }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    // AlertDialog использует платформенную ширину, но внутри мы ограничим размер контейнера,
    // чтобы при вводе ничего резко не прыгало.
    AlertDialog(
        onDismissRequest = {
            focusManager.clearFocus()
            onDismiss()
        },
        title = { Text(text = title) },
        text = {
            // фиксированный контейнер — ширина/высота контролируемые
            Box(
                modifier = Modifier
                    .width(widthDp)
                    .height(heightDp)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                ) {
                    // Основное поле — занимает доступное пространство и сдвигается при IME
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .imePadding(), // предотвращает перекрытие текста клавиатурой
                        placeholder = { Text("Введите комментарий...") },
                        singleLine = false,
                        maxLines = 10
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = {
                            focusManager.clearFocus()
                            onDismiss()
                        }) { Text("Отмена") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            focusManager.clearFocus()
                            keyboard?.hide()
                            onSave(text.trim())
                        }) { Text("Сохранить") }
                    }
                }
            }
        },
        buttons = {  },
        properties = DialogProperties(usePlatformDefaultWidth = true)
    )
}