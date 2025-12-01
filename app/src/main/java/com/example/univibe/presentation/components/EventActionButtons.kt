package com.example.univibe.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EventActionButtons(
    isLiked: Boolean,
    isSubscribed: Boolean,
    onLikeClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        LikeButton(
            isLiked = isLiked,
            onClick = onLikeClick,
            modifier = Modifier.weight(1f)
        )

        SubscribeButton(
            isSubscribed = isSubscribed,
            onClick = onSubscribeClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LikeButton(
    isLiked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = if (isLiked)
                MaterialTheme.colorScheme.tertiaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Icon(
            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (isLiked) "Gustado" else "Me gusta")
    }
}

@Composable
private fun SubscribeButton(
    isSubscribed: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isSubscribed)
                Icons.Default.Notifications
            else
                Icons.Default.NotificationsNone,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(if (isSubscribed) "Suscrito" else "Suscribirse")
    }
}

