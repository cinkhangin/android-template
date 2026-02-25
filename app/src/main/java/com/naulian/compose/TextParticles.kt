package com.naulian.compose

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.core.graphics.createBitmap

data class TextParticle(
    val color: Color,
    val radius: Float,
    val initialX: Float,
    val initialY: Float,
    val targetX: Float,
    val targetY: Float,
    val speed: Float
)

@Composable
fun TextParticles(modifier: Modifier = Modifier) {
    val animatable = remember { Animatable(0f) }
    val particles = remember { mutableStateListOf<TextParticle>() }
    var text by remember { mutableStateOf("3") }

    // When the text changes, clear the particles to trigger regeneration
    LaunchedEffect(text) {
        particles.clear()
        animatable.snapTo(0f)
    }

    // When the particles are regenerated (i.e., size changes), start the animation
    LaunchedEffect(particles.size) {
        if (particles.isNotEmpty()) {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessVeryLow,
                )
            )
        }
    }

    // Cycle through the alphabet
    LaunchedEffect(Unit) {
        val alphabet = listOf(
            "2", "1", "2026", "Happy", "New Year"
        )
        var index = 0
        while (true) {
            delay(2000)
            index = (index + 1) % alphabet.size
            text = alphabet[index].toString()
        }
    }

    Canvas(modifier = modifier) {
        // If particles are cleared, and we have a text, regenerate them
        if (particles.isEmpty() && text.isNotEmpty()) {
            val paint = Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 200f
                textAlign = Paint.Align.CENTER
            }

            val textBounds = Rect()
            paint.getTextBounds(text, 0, text.length, textBounds)

            val bitmap = createBitmap(
                width = size.width.toInt(),
                height = size.height.toInt()
            )
            val canvas = android.graphics.Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)
            canvas.drawText(
                text,
                size.width / 2,
                size.height / 2 - textBounds.exactCenterY(),
                paint
            )

            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

            for (y in 0 until bitmap.height step 6) {
                for (x in 0 until bitmap.width step 6) {
                    if (pixels[y * bitmap.width + x] == android.graphics.Color.BLACK) {
                        particles.add(
                            TextParticle(
                                color = Color(
                                    Random.nextFloat(),
                                    Random.nextFloat(),
                                    Random.nextFloat(),
                                    alpha = 1f
                                ),
                                radius = Random.nextFloat() * 4f + 2f,
                                initialX = Random.nextFloat() * size.width,
                                initialY = Random.nextFloat() * size.height,
                                targetX = x.toFloat(),
                                targetY = y.toFloat(),
                                speed = Random.nextFloat() * 2f + 0.5f
                            )
                        )
                    }
                }
            }
        }

        particles.forEach { particle ->
            val progress = (animatable.value * particle.speed).coerceAtMost(1f)
            val x = particle.initialX + (particle.targetX - particle.initialX) * progress
            val y = particle.initialY + (particle.targetY - particle.initialY) * progress
            drawCircle(
                color = particle.color,
                radius = particle.radius,
                center = Offset(x, y),
                alpha = progress
            )
        }
    }
}

@Preview
@Composable
private fun TextParticlesPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        TextParticles(modifier = Modifier.fillMaxSize())
    }
}
