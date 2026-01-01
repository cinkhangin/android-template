package com.naulian.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.naulian.modify.Fonts
import com.naulian.modify.White

@Composable
fun HighlightWordInText() {
    val text = "Jetpack Compose makes UI easy"
    val targetWord = "Compose makes"

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = text,
        onTextLayout = { textLayoutResult = it },
        modifier = Modifier.drawBehind {
            val layout = textLayoutResult ?: return@drawBehind

            val start = text.indexOf(targetWord)
            val end = start + targetWord.length

            // Get bounding boxes for all character ranges in the word
            val boxes = layout.getBoundingBoxesForRange(start, end)

            // Draw rounded background behind the target word
            val topLeft = boxes.first().topLeft
            val size = Size(
                width = boxes.sumOf { it.size.width.toInt() }.toFloat(),
                height = boxes.maxOf { it.size.height }
            )
            drawRoundRect(
                color = Color(0xFFEAEAEA),
                topLeft = topLeft - 5.OX,
                size = size.copy(width = size.width + 15f),
                cornerRadius = CornerRadius(size.height * 0.2f)
            )
        },
        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = Fonts.JetBrainsMono)
    )
}

// Extension to collect bounding boxes of a character range
private fun TextLayoutResult.getBoundingBoxesForRange(start: Int, end: Int): List<Rect> {
    val boxes = mutableListOf<Rect>()
    for (i in start until end) {
        val rect = getBoundingBox(i)
        boxes += rect
    }
    // Merge overlapping rects (e.g., if the word wraps lines)
    return boxes
}

@Preview
@Composable
private fun ResearchPreview() {
    Box(
        modifier = Modifier
            .background(White)
            .padding(20.dp)
    ) {
        HighlightWordInText()
    }
}

val Int.OX get() = OffsetXY.X(this)

sealed interface OffsetXY {
    data class X(val value: Int) : OffsetXY
    data class Y(val value: Int) : OffsetXY
}

operator fun Offset.plus(xValue: OffsetXY.X): Offset {
    return copy(x = x + xValue.value, y = y)
}

operator fun Offset.minus(xValue: OffsetXY.X): Offset {
    return copy(x = x - xValue.value, y = y)
}