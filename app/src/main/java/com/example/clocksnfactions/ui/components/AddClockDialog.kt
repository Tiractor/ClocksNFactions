package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddClockDialog(
    onDismiss: () -> Unit,
    onCreate: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var custom by remember { mutableStateOf("") }
    var preset by remember { mutableStateOf(6) }

    val config = LocalConfiguration.current
    val screenW = config.screenWidthDp.dp
    val screenH = config.screenHeightDp.dp

    val dialogWidth = (screenW * 0.85f).coerceAtMost(600.dp)
    val dialogHeight = (screenH * 0.5f)
        .coerceIn(220.dp, screenH * 0.7f)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp,
            modifier = Modifier
                .widthIn(min = 280.dp, max = dialogWidth)
                .heightIn(min = 200.dp, max = dialogHeight)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("Новый счётчик", style = MaterialTheme.typography.h6)

                Spacer(Modifier.height(12.dp))

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
                        label = { Text("Название (опционально)") },
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Выберите количество секторов:")

                    Spacer(Modifier.height(8.dp))

                    Row {
                        listOf(4, 6, 8).forEach { v ->
                            val selected = (preset == v)

                            OutlinedButton(
                                onClick = {
                                    preset = v
                                    custom = ""
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    backgroundColor = if (selected)
                                        MaterialTheme.colors.primary.copy(alpha = 0.15f)
                                    else
                                        MaterialTheme.colors.surface
                                ),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (selected)
                                        MaterialTheme.colors.primary
                                    else
                                        MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(
                                    "$v",
                                    color = if (selected)
                                        MaterialTheme.colors.primary
                                    else
                                        MaterialTheme.colors.onSurface
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = custom,
                        onValueChange = {
                            custom = it
                            if (it.isNotBlank()) preset = -1
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Другое") },
                        singleLine = true
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val seg = custom.toIntOrNull() ?: preset
                            val finalSeg = if (seg >= 3) seg else 6
                            onCreate(name.trim(), finalSeg)
                            onDismiss()
                        }
                    ) {
                        Text("Создать")
                    }
                }
            }
        }
    }
}
