package com.example.univibe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.univibe.presentation.theme.BackgroundWhite
import com.example.univibe.presentation.theme.BtnPrimary

/**
 * Componente reutilizable para mostrar avatar de usuario con soporte para:
 * - Foto desde URL (Google o Storage)
 * - Inicial del nombre como fallback
 * - Modo edición con ícono de cámara
 */
@Suppress("unused") // Componente reutilizable público
@Composable
fun UserAvatar(
    photoUrl: String,
    initial: String,
    size: Dp = 110.dp,
    isEditable: Boolean = false,
    onEditClick: (() -> Unit)? = null,
    borderColor: Color = Color.White,
    borderWidth: Dp = 4.dp
) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(borderWidth, borderColor, CircleShape)
                .background(if (photoUrl.isEmpty()) getColorForInitial(initial) else Color.Gray)
                .clickable(enabled = isEditable && onEditClick != null) { onEditClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            if (photoUrl.isNotEmpty()) {
                // Mostrar imagen desde URL (Google o Firebase Storage)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Mostrar inicial del nombre
                Text(
                    text = initial.uppercase(),
                    fontSize = (size.value / 2.5).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Ícono de cámara en modo edición
        if (isEditable && onEditClick != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp)
                    .background(BtnPrimary, CircleShape)
                    .border(2.dp, BackgroundWhite, CircleShape)
                    .clickable { onEditClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Editar foto",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Genera un color de fondo único basado en la inicial
 * Similar a WhatsApp o Teams
 */
private fun getColorForInitial(initial: String): Color {
    val colors = listOf(
        Color(0xFF1ABC9C), // Turquoise
        Color(0xFF3498DB), // Blue
        Color(0xFF9B59B6), // Purple
        Color(0xFFE67E22), // Orange
        Color(0xFFE74C3C), // Red
        Color(0xFF95A5A6), // Gray
        Color(0xFFF39C12), // Yellow
        Color(0xFF16A085), // Green Sea
        Color(0xFF2980B9), // Belize Hole
        Color(0xFF8E44AD), // Wisteria
    )

    val index = initial.firstOrNull()?.code?.rem(colors.size) ?: 0
    return colors[index]
}

