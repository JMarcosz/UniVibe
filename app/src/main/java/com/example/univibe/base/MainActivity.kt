package com.example.univibe.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.univibe.presentation.navgraph.NavGraph
import com.example.univibe.presentation.theme.UniVibeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // <-- Habilitando Hilt en la Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniVibeTheme { // <-- Aplicando tu Tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- AQUÍ SE LLAMA A LA VISTA ---
                    // NavGraph decide qué pantalla mostrar
                    NavGraph()
                }
            }
        }
    }
}