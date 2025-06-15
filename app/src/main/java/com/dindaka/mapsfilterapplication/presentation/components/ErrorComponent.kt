package com.dindaka.mapsfilterapplication.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorComponent(message: String) {
    Box(Modifier.fillMaxSize()) {
        Text(message)
    }
}
