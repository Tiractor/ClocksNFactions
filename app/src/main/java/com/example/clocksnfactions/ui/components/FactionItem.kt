package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clocksnfactions.R
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
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // 🔥 Фон карточки
            Image(
                painter = painterResource(id = R.drawable.card_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // 🔥 Полупрозрачный слой, если нужно затемнение (опционально)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.25f))
            )

            // 🔥 Основное содержимое карточки
            Column(modifier = Modifier.padding(12.dp)) {

                // ------ всё, что было внутри карточки ранее ------
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = faction.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White      // фон тёмный → белый текст
                        )
                    }
                    TextButton(onClick = onDelete) {
                        Text("Удалить", color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ранг
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Ранг:", modifier = Modifier.padding(end = 8.dp), color = Color.White)
                    Text("${faction.rank}", fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onRankChange(+1) }, enabled = faction.rank < 6) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Увеличить ранг", tint = Color.White)
                    }
                    IconButton(onClick = { onRankChange(-1) }, enabled = faction.rank > 0) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Уменьшить ранг", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Отношение + подсказка
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Отношение:", modifier = Modifier.padding(end = 8.dp), color = Color.White)
                    Text(
                        "${if (faction.relationship >= 0) "+" else ""}${faction.relationship}",
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    IconButton(onClick = { hintVisible = !hintVisible }) {
                        Icon(Icons.Default.Info, contentDescription = "Подсказка", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onRelationshipChange(+1) }, enabled = faction.relationship < 3) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Увеличить отношение", tint = Color.White)
                    }
                    IconButton(onClick = { onRelationshipChange(-1) }, enabled = faction.relationship > -3) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Уменьшить отношение", tint = Color.White)
                    }
                }

                if (hintVisible) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = relationshipHint(faction.relationship),
                        color = Color.LightGray,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Контроль
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Контроль:", modifier = Modifier.weight(1f), color = Color.White)

                    val badgeColor = if (faction.controlHard) Color(0xFFD32F2F) else Color(0xFF388E3C)

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(badgeColor.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            if (faction.controlHard) "Жёсткий" else "Слабый",
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Switch(
                        checked = faction.controlHard,
                        onCheckedChange = { onToggleControl() }
                    )
                }

                // Заметка
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showEdit = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Редактировать", tint = Color.White)
                    }
                    if (!preview.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = preview, color = Color.LightGray, fontSize = 13.sp)
                    }
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
