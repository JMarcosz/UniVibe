package com.example.univibe.presentation.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.univibe.R
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary
import com.example.univibe.presentation.theme.SecondaryBlue
import com.example.univibe.presentation.theme.TerciaryBlue
import com.example.univibe.presentation.theme.Card as CardColor
import androidx.compose.foundation.layout.offset

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfilePhoto(it)
            viewModel.dismissPhotoDialog()
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Tarjeta superior: foto + nombre + eventos
                ProfileHeaderCard(uiState = uiState, onEditPhotoClick = viewModel::onEditPhotoClick)

                // Información Personal Editable
                PersonalInfoCard(
                    uiState = uiState,
                    onFirstNameChange = viewModel::onFirstNameChange,
                    onLastNameChange = viewModel::onLastNameChange,
                    onEmailChange = viewModel::onEmailChange,
                    onPhoneChange = viewModel::onPhoneChange,
                    onEditToggle = viewModel::toggleEditMode,
                    onSave = viewModel::saveProfile
                )

                // Opciones: historial y configuración
                OptionsSection()

                Spacer(modifier = Modifier.height(10.dp))

                // Botón Cerrar Sesión
                Button(
                    onClick = viewModel::onSignOutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BtnPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.log_out_icon),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "Cerrar Sesión")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Loading overlay
            if (uiState.isLoading || uiState.isSaving) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BtnPrimary)
                }
            }

            // Mensajes de éxito/error
            uiState.successMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = Color(0xFF4CAF50)
                ) {
                    Text(message, color = Color.White)
                }
            }

            uiState.errorMessage?.let { message ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = viewModel::clearMessages) {
                            Text("OK", color = Color.White)
                        }
                    },
                    containerColor = Color(0xFFE53935)
                ) {
                    Text(message, color = Color.White)
                }
            }

            // Diálogo de foto de perfil
            if (uiState.showPhotoDialog) {
                PhotoUploadDialog(
                    onDismiss = viewModel::dismissPhotoDialog,
                    onSelectImage = { imagePickerLauncher.launch("image/*") }
                )
            }
        }
    }
}

@Composable
private fun ProfileHeaderCard(
    uiState: ProfileUiState,
    onEditPhotoClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardColor.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto de perfil con botón de editar
            Box {
                ProfileAvatar(
                    photoUrl = uiState.photoUrl,
                    email = uiState.email,
                    firstName = uiState.firstName,
                    modifier = Modifier.size(80.dp)
                )

                // Botón de editar foto
                IconButton(
                    onClick = onEditPhotoClick,
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .background(BtnPrimary, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar foto",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.getFullName().ifBlank { "Usuario" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Eventos suscritos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "${uiState.subscribedEventsCount} eventos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BtnPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    photoUrl: String,
    email: String,
    firstName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(2.dp, BtnPrimary, CircleShape)
            .background(if (photoUrl.isEmpty()) BtnPrimary else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        if (photoUrl.isNotEmpty()) {
            // Mostrar imagen de Google o subida
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
            // Mostrar inicial del email o nombre
            val initial = when {
                firstName.isNotEmpty() -> firstName.first().uppercase()
                email.isNotEmpty() -> email.first().uppercase()
                else -> "U"
            }
            Text(
                text = initial,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PersonalInfoCard(
    uiState: ProfileUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onSave: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardColor.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header con título y botón editar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Información Personal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (!uiState.isEditing) {
                    IconButton(onClick = onEditToggle) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = BtnPrimary
                        )
                    }
                }
            }

            // Campos editables o estáticos
            if (uiState.isEditing) {
                EditableFields(
                    uiState = uiState,
                    onFirstNameChange = onFirstNameChange,
                    onLastNameChange = onLastNameChange,
                    onEmailChange = onEmailChange,
                    onPhoneChange = onPhoneChange
                )

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onEditToggle,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = BtnSecondary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cancelar")
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = BtnPrimary),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !uiState.isSaving
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                }
            } else {
                StaticFields(uiState = uiState)
            }
        }
    }
}

@Composable
private fun EditableFields(
    uiState: ProfileUiState,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Nombre
        OutlinedTextField(
            value = uiState.firstName,
            onValueChange = onFirstNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errors.containsKey("firstName"),
            supportingText = {
                uiState.errors["firstName"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BtnPrimary,
                focusedLabelColor = BtnPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Apellido
        OutlinedTextField(
            value = uiState.lastName,
            onValueChange = onLastNameChange,
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.errors.containsKey("lastName"),
            supportingText = {
                uiState.errors["lastName"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BtnPrimary,
                focusedLabelColor = BtnPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.errors.containsKey("email"),
            supportingText = {
                uiState.errors["email"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BtnPrimary,
                focusedLabelColor = BtnPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )

        // Teléfono
        OutlinedTextField(
            value = uiState.phone,
            onValueChange = onPhoneChange,
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = uiState.errors.containsKey("phone"),
            supportingText = {
                uiState.errors["phone"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BtnPrimary,
                focusedLabelColor = BtnPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun StaticFields(uiState: ProfileUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Nombre completo
        InfoField(
            label = "Nombre completo",
            value = uiState.getFullName().ifBlank { "No especificado" }
        )

        // Email
        InfoField(
            label = "Correo",
            value = uiState.email.ifBlank { "No especificado" }
        )

        // Teléfono
        InfoField(
            label = "Teléfono",
            value = uiState.phone.ifBlank { "No especificado" }
        )
    }
}

@Composable
private fun InfoField(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun OptionsSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OptionRow(
            bgColor = SecondaryBlue,
            icon = R.drawable.person_icon,
            title = "Historial"
        )

        OptionRow(
            bgColor = TerciaryBlue,
            icon = R.drawable.settings_icon,
            title = "Configuración"
        )
    }
}

@Composable
private fun OptionRow(bgColor: Color, icon: Int, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_flecha),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
private fun PhotoUploadDialog(
    onDismiss: () -> Unit,
    onSelectImage: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Cambiar foto de perfil",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Selecciona una imagen de tu galería. La imagen será ajustada automáticamente a formato circular.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Button(
                    onClick = onSelectImage,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BtnPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                        contentDescription = "Galería"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Seleccionar imagen")
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        }
    }
}
