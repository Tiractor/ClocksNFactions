package com.example.clocksnfactions.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.clocksnfactions.data.local.entities.ClockEntity
import kotlin.math.min

@Composable
fun ClockView(
    clock: ClockEntity,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
    diameter: Dp = 120.dp
) {
    // Получаем density для конвертаций dp -> px
    val density = LocalDensity.current
    // Конвертируем заранее (в пикселях)
    val diameterPx = with(density) { diameter.toPx() }
    val strokePx = with(density) { 12.dp.toPx() } // толщина линии в px
    val gapDegrees = 2f // небольшой gap между сегментами

    // Sweep одного сегмента в градусах
    val sweep = remember(clock.segments) { 360f / clock.segments }

    Canvas(
        modifier = modifier
            // Устанавливаем размер компоновки равный diameter
            .then(Modifier)
            .pointerInput(clock.id) {
                detectTapGestures(
                    onTap = { onIncrement() },
                    onLongPress = { onDecrement() }
                )
            }
    ) {
        // Внутри DrawScope доступны size.width/size.height (в px)
        // Используем минимальную из сторон (на случай, если модификатор установит другой размер)
        val usedDiameter = min(size.width, size.height).coerceAtMost(diameterPx)
        val radius = usedDiameter / 2f - strokePx / 2f

        // Центр круга
        val centerOffset = Offset(x = size.width / 2f, y = size.height / 2f)

        // Сдвигаем начало на 12 часов (rotate -90°)
        rotate(degrees = -90f, pivot = centerOffset) {
            for (i in 0 until clock.segments) {
                val startAngle = i * sweep
                val sweepAngle = sweep - gapDegrees

                val color = if (i < clock.filled) Color(0xFF4CAF50) else Color.LightGray

                // Нарисовать дугу (arc) вокруг центра
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(centerOffset.x - radius, centerOffset.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2f, radius * 2f),
                    style = Stroke(width = strokePx)
                )
            }
        }
    }
}
