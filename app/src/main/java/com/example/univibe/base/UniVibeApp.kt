package com.example.univibe.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UniVibeApp : Application() {
    // Hilt usará esta clase para la inyección de dependencias
}