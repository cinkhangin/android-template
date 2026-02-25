package com.naulian.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

data class Particle(
    val color: Color,
    val radius: Float,
    val initialX: Float,
    val initialY: Float,
    val targetX: Float,
    val targetY: Float,
    val speed: Float
)

@Composable
fun Particles(modifier: Modifier = Modifier) {
    val animatable = remember { Animatable(0f) }
    val particles = remember { mutableListOf<Particle>() }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessVeryLow,
            )
        )
    }

    Canvas(modifier = modifier) {
        if (particles.isEmpty()) {
            repeat(1000) {
                particles.add(
                    Particle(
                        color = Color(
                            Random.nextFloat(),
                            Random.nextFloat(),
                            Random.nextFloat(),
                            alpha = 1f
                        ),
                        radius = Random.nextFloat() * 20f + 10f,
                        initialX = size.width / 2,
                        initialY = size.height / 2,
                        targetX = Random.nextFloat() * size.width,
                        targetY = Random.nextFloat() * size.height,
                        speed = Random.nextFloat() * 2f + 0.5f
                    )
                )
            }
        }

        particles.forEach { particle ->
            val progress = (animatable.value * particle.speed).coerceAtMost(1f)

            val x = particle.initialX + (particle.targetX - particle.initialX) * progress
            val y = particle.initialY + (particle.targetY - particle.initialY) * progress
            drawCircle(
                color = particle.color,
                radius = particle.radius * progress,
                center = Offset(x, y),
                alpha = (1f - progress).coerceAtLeast(0f)
            )
        }
    }
}

@Preview
@Composable
private fun ParticlesPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Particles(modifier = Modifier.fillMaxSize())
    }
}
