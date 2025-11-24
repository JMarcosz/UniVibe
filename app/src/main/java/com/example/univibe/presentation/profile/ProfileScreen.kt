package com.example.univibe.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.univibe.R
import com.example.univibe.presentation.home.HomeViewModel
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.SecondaryBlue
import com.example.univibe.presentation.theme.TerciaryBlue
import com.example.univibe.presentation.theme.Card as CardColor

@Composable
fun ProfileScreen() {
    val viewModel: ProfileViewModel = hiltViewModel()

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier
                .fillMaxWidth(1f)
                .height(25.dp))
            // Tarjeta superior: foto + nombre + eventos
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardColor.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.foto),
                        contentDescription = "Foto usuario",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.padding(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Jean Marcos", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Eventos suscritos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Text(
                            text = "0 eventos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Información Personal
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardColor.copy(alpha = 0.05f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Información Personal",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Nombre completo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "Jean Marco Marte Rivera",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Correo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "jeanmaicol2225@gmail.com",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Teléfono",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(text = "(829)-906-9256", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(25.dp))
                    // Opciones: historial y configuración
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OptionRow(
                            bgColor = SecondaryBlue,
                            icon = R.drawable.person_icon,
                            title = "Historial"
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(10.dp))
                        OptionRow(
                            bgColor = TerciaryBlue,
                            icon = R.drawable.settings_icon,
                            title = "Configuración"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Botón Cerrar Sesión (sin lógica)
            Button(
                onClick = { viewModel.onSignOutClick() },
                modifier = Modifier.width(175.dp), colors = ButtonDefaults.buttonColors(
                    containerColor = BtnPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = getLogoutDrawable()),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = "Cerrar Sesión")
                }
            }

            Spacer(modifier = Modifier.fillMaxWidth(1f))
        }
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

            Spacer(modifier = Modifier.padding(12.dp))

            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_flecha),
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

// Helper para resolver nombre del drawable de logout si hay variantes
private fun getLogoutDrawable(): Int {
    // Usar el drawable existente `log_out_icon` en el repo
    return R.drawable.log_out_icon
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ProfileScreen()
}