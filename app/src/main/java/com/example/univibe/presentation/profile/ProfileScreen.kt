package com.example.univibe.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.univibe.presentation.theme.*

// Usamos BtnSecondary para el fondo principal como solicitaste
private val HeaderColor = BtnSecondary
private val TextFieldColorLocal = Color(0xFFF9F9F9)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Launcher para subir foto
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfilePhoto(it)
            viewModel.dismissPhotoDialog()
        }
    }

    Scaffold(
        containerColor = BackgroundWhite,
    ) { paddingValues ->
        // CAMBIO PRINCIPAL: El Column con scroll ahora contiene TODO (Fondo + Contenido)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(scrollState), // El scroll se aplica a todo el conjunto
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- SECCIÓN CABECERA (Fondo + Info) ---
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // 1. FONDO CURVO (Ahora es parte del scroll)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Altura ajustada para cubrir bien el contenido
                        .background(
                            color = HeaderColor,
                            shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                        )
                )

                // 2. CONTENIDO DE CABECERA (Superpuesto al fondo)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // Foto de Perfil
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, Color.White, CircleShape)
                                    .background(Color.Gray)
                                    .clickable { if(uiState.isEditing) viewModel.onEditPhotoClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.photoUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(uiState.photoUrl)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Foto",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        text = uiState.firstName.take(1).uppercase(),
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            // Icono cámara (modo edición)
                            if (uiState.isEditing) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(32.dp)
                                        .background(BtnPrimary, CircleShape)
                                        .border(2.dp, BackgroundWhite, CircleShape)
                                        .clickable { viewModel.onEditPhotoClick() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Column(){
                            // Textos Nombre/Rol
                            Text(
                                text = uiState.getFullName().ifBlank { "Usuario" },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Estudiante Universitario",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "${uiState.subscribedEventsCount}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Eventos",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )

                        }
                    }

                    // Espacio final dentro del contenedor de cabecera para asegurar que nada se corte
//                    Spacer(modifier = Modifier.height(30.dp))
                }
            }

            // --- SECCIÓN CUERPO (Formulario / Detalles) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = viewModel::toggleEditMode) {
                    Icon(
                        imageVector = if (uiState.isEditing) Icons.Default.Close else Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color.Gray
                    )
                }
                if (uiState.isEditing) {
                    FlatEditField(value = uiState.firstName, onValueChange = viewModel::onFirstNameChange, label = "Nombre", icon = Icons.Default.Person)
                    FlatEditField(value = uiState.lastName, onValueChange = viewModel::onLastNameChange, label = "Apellido", icon = Icons.Default.Person)
                    FlatEditField(value = uiState.email, onValueChange = viewModel::onEmailChange, label = "Correo", icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
                    FlatEditField(value = uiState.phone, onValueChange = viewModel::onPhoneChange, label = "Teléfono", icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = viewModel::saveProfile,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BtnPrimary)
                    ) {
                        Text("Guardar Cambios")
                    }
                } else {
                    DetailRowItem(icon = Icons.Default.Person, label = "Nombre Completo", value = uiState.getFullName())
                    DetailRowItem(icon = Icons.Default.Email, label = "Email", value = uiState.email)
                    DetailRowItem(icon = Icons.Default.Phone, label = "Teléfono", value = uiState.phone)
                    DetailRowItem(icon = Icons.Default.Settings, label = "Configuración", value = "Ajustes de cuenta", onClick = { /* Ir a ajustes */ })
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!uiState.isEditing) {
                    Button(
                        onClick = viewModel::onSignOutClick,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFEBEE),
                            contentColor = Color(0xFFD32F2F)
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Sesión", fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Dialogs y Overlays
        if (uiState.showPhotoDialog) {
            PhotoSelectionDialog(
                onDismiss = viewModel::dismissPhotoDialog,
                onSelectGallery = { imagePickerLauncher.launch("image/*") }
            )
        }

        if (uiState.isLoading || uiState.isSaving) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

// --- Componentes Auxiliares ---

@Composable
fun DetailRowItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TextFieldColorLocal, RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BtnPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = TextGray.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value.ifBlank { "No especificado" }, fontSize = 16.sp, color = TextGray, fontWeight = FontWeight.Normal)
        }
    }
}

@Composable
fun FlatEditField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = BtnPrimary) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = TextFieldColorLocal,
            unfocusedContainerColor = TextFieldColorLocal,
            disabledContainerColor = TextFieldColorLocal,
            cursorColor = BtnPrimary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = BtnPrimary
        ),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PhotoSelectionDialog(onDismiss: () -> Unit, onSelectGallery: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BackgroundWhite,
        title = { Text("Cambiar Foto", fontWeight = FontWeight.Bold, color = TextGray) },
        text = { Text("Selecciona una imagen de tu galería.", color = LightGray) },
        confirmButton = {
            Button(onClick = onSelectGallery, colors = ButtonDefaults.buttonColors(containerColor = BtnPrimary)) {
                Text("Galería")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextGray) }
        }
    )
}