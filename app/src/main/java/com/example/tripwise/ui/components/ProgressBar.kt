package com.example.tripwise.ui.components

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Brush

@Composable
fun ProgressBar(
    currentAmount: Float,
    totalAmount: Float,
    modifier: Modifier = Modifier
) {
    // Calculate progress percentage
    val progress = currentAmount / totalAmount

    // Define color based on progress
    val progressColor = when {
        progress < 0.5f -> Color.Magenta
        progress < 0.75f -> Color.Magenta
        else -> Color.Red
    }

    // Stack the progress bar and text using a Box
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 16.dp)
    ) {
        // Background progress bar with a gradient color
        LinearProgressIndicator(
            progress = progress,
            color = progressColor,
            trackColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )

        // Text showing the progress (e.g., "1100 / 3000")
        Text(
            text = "${currentAmount.toInt()} / ${totalAmount.toInt()}",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}