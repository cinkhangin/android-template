package com.naulian.compose.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.naulian.compose.Screen

@Composable
fun HomeScreen(backStack: SnapshotStateList<Screen>) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                HomeEvent.ToSecond -> backStack.add(Screen.Second)
            }
        }
    }

    HomeScreenUI(uiState = state) {
        when (it) {
            HomeUIEvent.Back -> backStack.removeLastOrNull()
        }
    }
}