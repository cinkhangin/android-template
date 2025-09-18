package com.naulian.compose.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed interface HomeUIEvent {
    data object Back : HomeUIEvent
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenUI(
    uiState: HomeUIState = HomeUIState(),
    uiEvent: (HomeUIEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.title) }
            )
        }
    ) { screenPadding ->
        LazyColumn(
            modifier = Modifier.padding(screenPadding)
        ) {

        }
    }
}