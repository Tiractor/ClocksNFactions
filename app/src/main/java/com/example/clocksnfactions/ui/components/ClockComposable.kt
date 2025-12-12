package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.clocksnfactions.data.local.entities.ClockEntity

@Composable
fun ClockView(
    clock: ClockEntity,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stroke = 12f
    val radiusInset = 20f
    val sweep = 360f / clock.segments

    Canvas(
        modifier = modifier
            .size(120.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onIncrement() },
                    onLongPress = { onDecrement() }
                )
            }
    ) {
        val radius = size.minDimension / 2 - radiusInset

        rotate(-90f) {

            // Рисуем сегменты
            for (i in 0 until clock.segments) {
                val start = i * sweep
                val color =
                    if (i < clock.filled) Color(0xFF6666FF)
                    else Color.LightGray

                drawArc(
                    color = color,
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(stroke),
                    topLeft = Offset(
                        (size.width - radius * 2) / 2,
                        (size.height - radius * 2) / 2
                    ),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                )
            }
        }
    }
}
