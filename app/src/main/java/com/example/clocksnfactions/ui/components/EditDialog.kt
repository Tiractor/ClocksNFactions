package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.*


@Composable
fun EditDialog(
    initialName: String = "",
    initialNote: String = "",
    title: String = "Редактировать",
    onDismiss: () -> Unit,
    onSave: (name: String, note: String) -> Unit,
    widthFraction: Float = 0.85f,   // ширина как доля экрана
    heightFraction: Float = 0.40f   // высота как доля экрана
) {
    var name by remember { mutableStateOf(initialName) }
    var note by remember { mutableStateOf(initialNote) }

    val configuration = LocalConfiguration.current
    val screenW = configuration.screenWidthDp.dp
    val screenH = configuration.screenHeightDp.dp

    // вычисляем размеры
    val dialogWidth = (screenW * widthFraction).coerceIn(280.dp, 600.dp)
    val dialogHeight = (screenH * heightFraction).coerceIn(200.dp, screenH * 0.8f)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp,
            modifier = Modifier
                .width(dialogWidth)
                .height(dialogHeight)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {

                // Заголовок
                Text(text = title, style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(12.dp))

                // Прокручиваемая область, которая НЕ меняет размера диалога
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scroll)
                        .imePadding()
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Название") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp), // фиксированная минимальная зона
                        label = { Text("Комментарий (опционально)") },
                        placeholder = { Text("Например: главный штаб в порту") },
                        singleLine = false,
                        maxLines = 8,
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Отмена") }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        onSave(name.trim(), note.trim())
                    }) { Text("Сохранить") }
                }
            }
        }
    }
}
