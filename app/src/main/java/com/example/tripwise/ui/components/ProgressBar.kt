package com.example.tripwise.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import java.util.*

@Composable
fun ProgressBar(
    currentAmount: Double,
    totalAmount: Double,
    modifier: Modifier = Modifier
) {
    // Calculate progress percentage
    val progress = (currentAmount / totalAmount).toFloat()

    // Define color based on progress
    val progressColor = when {
        progress < 0.5f -> Color.Yellow
        progress < 0.75f -> Color.Yellow
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
            text = "${String.format(Locale.getDefault(), "%.2f", currentAmount)} / ${String.format(Locale.getDefault(), "%.2f", totalAmount)}",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}