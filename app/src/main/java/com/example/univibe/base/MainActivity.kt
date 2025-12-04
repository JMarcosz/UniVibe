package com.example.univibe.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.univibe.base.navigation.NavigationWrapper
import com.example.univibe.presentation.theme.UniVibeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.example.univibe.domain.repository.AuthRepository
import com.example.univibe.util.DeepLinkHandler


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilitar modo edge-to-edge (pantalla completa sin barras del sistema superpuestas)
        enableEdgeToEdge()

        // Configurar la ventana para que el contenido se dibuje detrás de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        handleIntent(intent)
        setContent {
            val navHostController = rememberNavController()

            UniVibeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper(navHostController, authRepository)
                }
            }
        }
    }

    // --- CORRECCIÓN AQUÍ ---
    override fun onNewIntent(intent: Intent) { // 1. Cambiado de Intent? a Intent
        super.onNewIntent(intent)              // 2. Añadida la llamada a super
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "univibe" && uri.host == "event") {
                val eventId = uri.lastPathSegment
                DeepLinkHandler.setEventId(eventId)
            }
        }
    }
}