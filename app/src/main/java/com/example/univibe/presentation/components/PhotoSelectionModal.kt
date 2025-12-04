package com.example.univibe.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.univibe.presentation.theme.*

/**
 * Modal reutilizable para seleccionar y subir foto de perfil.
 * Permite al usuario elegir entre galería o cámara.
 *
 * @param isVisible Controla la visibilidad del modal
 * @param onDismiss Callback cuando se cierra el modal
 * @param onImageSelected Callback cuando se selecciona una imagen (retorna la URI)
 * @param isLoading Indica si se está procesando la imagen
 */
@Suppress("unused") // Componente reutilizable público
@Composable
fun PhotoSelectionModal(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onImageSelected: (Uri) -> Unit,
    isLoading: Boolean = false
) {
    // Launcher para seleccionar de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    // Launcher para tomar foto con cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // TODO: Implementar URI de foto tomada
            // Por ahora solo soportamos galería
        }
    }

    if (isVisible) {
        Dialog(onDismissRequest = { if (!isLoading) onDismiss() }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = BackgroundWhite,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cambiar Foto de Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Selecciona de dónde quieres cargar tu imagen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = BtnPrimary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Subiendo imagen...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightGray
                        )
                    } else {
                        // Botón Galería
                        PhotoOptionButton(
                            text = "Galería",
                            icon = Icons.Default.PhotoLibrary,
                            onClick = { galleryLauncher.launch("image/*") },
                            backgroundColor = BtnPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Botón Cámara (Deshabilitado por ahora)
                        PhotoOptionButton(
                            text = "Cámara",
                            icon = Icons.Default.CameraAlt,
                            onClick = { /* TODO: Implementar cámara */ },
                            backgroundColor = BtnSecondary,
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón Cancelar
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Cancelar",
                                color = LightGray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoOptionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    backgroundColor: Color,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White,
            disabledContainerColor = backgroundColor.copy(alpha = 0.5f)
        ),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

