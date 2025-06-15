package com.dindaka.mapsfilterapplication.presentation

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dindaka.mapsfilterapplication.presentation.theme.MapsFilterApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Scaffold
import com.dindaka.mapsfilterapplication.presentation.screens.city_coordinator.CityCoordinator

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapsFilterApplicationTheme {
                Scaffold(
                    content = { innerPadding ->
                        CityCoordinator()
                    }
                )
            }
        }
    }
}