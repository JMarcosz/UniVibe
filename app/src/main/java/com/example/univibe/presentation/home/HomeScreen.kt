package com.example.univibe.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(text = "Estoy Logueado")
        Button(onClick = { viewModel.onSignOutClick() }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Cerrar sesión")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Column {
        Text(text = "Estoy Logueado")
    }
}