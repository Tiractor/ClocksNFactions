package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clocksnfactions.data.local.entities.FactionEntity
import com.example.clocksnfactions.ui.utils.relationshipHint

@Composable
fun FactionItem(
    faction: FactionEntity,
    onRankChange: (Int) -> Unit,
    onToggleControl: () -> Unit,
    onRelationshipChange: (Int) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onUpdate: (FactionEntity) -> Unit
) {
    // Видимость подсказки и диалога редактирования заметки
    var hintVisible by remember { mutableStateOf(false) }
    var showEdit by remember { mutableStateOf(false) }

    // Короткое превью заметки (если есть)
    val preview = faction.note?.takeIf { it.isNotBlank() }?.let {
        if (it.length > 160) it.take(160) + "…" else it
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        
        Column(modifier = Modifier.padding(12.dp)) {
            // Верхняя строка — название и кнопка редактирования заметки
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = faction.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                TextButton(onClick = onDelete) {
                    Text("Удалить", color = Color.Red)
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Ранг и кнопки изменения
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Ранг:", modifier = Modifier.padding(end = 8.dp))
                Text("${faction.rank}", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onRankChange(+1) }, enabled = faction.rank < 6) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Увеличить ранг")
                }
                IconButton(onClick = { onRankChange(-1) }, enabled = faction.rank > 0) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Уменьшить ранг")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Отношение и подсказка
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Отношение:", modifier = Modifier.padding(end = 8.dp))
                Text("${if (faction.relationship >= 0) "+" else ""}${faction.relationship}", fontWeight = FontWeight.Medium)
                IconButton(onClick = { hintVisible = !hintVisible }) {
                    Icon(Icons.Default.Info, contentDescription = "Подсказка по отношениям")
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { onRelationshipChange(+1) }, enabled = faction.relationship < 3) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Увеличить отношение")
                }
                IconButton(onClick = { onRelationshipChange(-1) }, enabled = faction.relationship > -3) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Уменьшить отношение")
                }
            }


            if (hintVisible) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = relationshipHint(faction.relationship),
                    color = Color.DarkGray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Контроль (переключатель)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Контроль:", modifier = Modifier.weight(1f))
                // визуальный бейдж + переключатель
                val badgeColor = if (faction.controlHard) Color(0xFFD32F2F) else Color(0xFF388E3C)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(if (faction.controlHard) "Жёсткий" else "Слабый", color = Color.White)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Switch(
                    checked = faction.controlHard,
                    onCheckedChange = { onToggleControl() }
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { showEdit = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Редактировать заметку")
                }
                // Превью заметки (короткий текст)
                if (!preview.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = preview, color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
    }

    // Диалог редактирования заметки — располагаем после карточки, чтобы он нависал поверх UI
    if (showEdit) {
        EditDialog(
            initialName = faction.name,
            initialNote = faction.note ?: "",
            title = "Комментарий к фракции",
            onDismiss = { showEdit = false },
            onSave = { newName, newNote ->
                onUpdate(faction.copy(name = newName, note = newNote.ifBlank { null }))
                showEdit = false
            },
        )
    }
}
