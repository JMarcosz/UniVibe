package com.example.univibe.presentation.auth

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.univibe.R
import com.example.univibe.presentation.theme.BtnPrimary
import com.example.univibe.presentation.theme.BtnSecondary
import com.example.univibe.presentation.theme.TextField
import com.example.univibe.presentation.theme.TextGray
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import com.example.univibe.presentation.theme.LightGray


@Composable
fun AuthScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onNavigateToRecovery: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Configurar GoogleSignInClient
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(Exception::class.java)
                val idToken = account?.idToken
                if (!idToken.isNullOrEmpty()) {
                    viewModel.onGoogleSignIn(idToken)
                } else {
                    // handle missing token (optional)
                }
            } catch (e: Exception) {
                // handle error (optional)
            }
        }
    }


    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.ic_logo_univibe),
            contentDescription = "",
            modifier = Modifier.size(76.dp)
        )
        Text(text = "Univibe", color = TextGray, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu aplicación para eventos \n universitarios",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = TextGray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = TextField,
                unfocusedContainerColor = TextField,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text("Correo electronico", fontSize = 14.sp) })

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = TextField,
                unfocusedContainerColor = TextField,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text("Contraseña", fontSize = 14.sp) })

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .clickable{ onNavigateToRecovery() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Olvidaste tu contraseña?", color = TextGray, fontWeight = FontWeight.Bold)

            Button(
                onClick = { viewModel.onSignInClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BtnPrimary
                ),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Entrar", modifier = Modifier.padding(end = 8.dp))
                    Icon(painter = painterResource(R.drawable.ic_flecha), contentDescription = "")
                }

            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { // lanzar intent de Google Sign-In
                val signInIntent = googleSignInClient.signInIntent
                // Forzar selector de cuenta: sign out antes de lanzar
                googleSignInClient.signOut().addOnCompleteListener {
                    launcher.launch(signInIntent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 32.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BtnSecondary
            ),

            ) {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = "",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Google")
        }

        // mostrar error desde el ViewModel
        uiState.error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(horizontal = 32.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "¿Aún no tienes una cuenta?",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                fontSize = 16.sp
            )
            Text(
                text = "Registrate",
                modifier = Modifier.clickable{ onNavigateToRegister() },
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = LightGray,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.weight(1f))
        }

    }
}