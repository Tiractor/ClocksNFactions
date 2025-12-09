package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AddFactionDialog(
    initialName: String = "",
    onAdd: (String) -> Unit,
    onCancel: () -> Unit,
    widthFraction: Float = 0.85f,   // доля ширины экрана (0..1)
    heightFraction: Float = 0.30f   // доля высоты экрана (0..1)
) {
    var text by remember { mutableStateOf(initialName) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // экран в dp
    val screenW = configuration.screenWidthDp.dp
    val screenH = configuration.screenHeightDp.dp

    // вычисляем размеры диалога с ограничением max/min
    val dialogWidth = remember(screenW, widthFraction) {
        // не даём диалогу быть шире 600dp даже на планшетах
        val candidate = screenW * widthFraction
        if (candidate > 600.dp) 600.dp else candidate
    }

    val dialogHeight = remember(screenH, heightFraction) {
        // минимальная высота 200dp, максимальная 70% экрана
        val candidate = screenH * heightFraction
        when {
            candidate < 200.dp -> 200.dp
            candidate > screenH * 0.7f -> screenH * 0.7f
            else -> candidate
        }
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            elevation = 8.dp,
            modifier = Modifier
                .widthIn(min = 280.dp, max = dialogWidth)
                .heightIn(min = 180.dp, max = dialogHeight)
                .padding(8.dp)
        ) {
            // Внутренний контейнер: Column с прокруткой для текста (чтобы клавиатура не "двигала" весь диалог)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Text(text = "Новая фракция", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                // scrollable box: при появлении клавиатуры содержимое можно прокрутить,
                // а внешний Surface не изменит размеров
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scroll)
                        .imePadding() // чтобы контент не был перекрыт клавиатурой
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Название") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Можно здесь добавить ещё элементы (описание, выбор и т.д.)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) { Text("Отмена") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onAdd(text.trim()) }) { Text("Добавить") }
                }
            }
        }
    }
}
