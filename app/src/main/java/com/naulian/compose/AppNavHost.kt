package com.naulian.compose

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.naulian.compose.home.HomeScreen

@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.Home> {
                HomeScreen(backStack)
            }

            entry<Screen.Second> {
                SecondScreen(onBack = { backStack.removeLastOrNull() })
            }
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(400)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(400)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(400)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(400)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(400)
            )
        }
    )
}
