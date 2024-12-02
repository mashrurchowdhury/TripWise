package com.example.tripwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.tripwise.ui.theme.TripWiseGreen
import java.util.*

@Composable
fun ProgressBar(
    currentAmount: Double,
    totalAmount: Double,
    modifier: Modifier = Modifier
) {
    // Calculate progress percentage
    val progress = (currentAmount / totalAmount).coerceIn(0.0, 1.0).toFloat()

    // Define color based on progress
    val progressColor = when {
        progress < 0.5f -> TripWiseGreen
        progress < 0.75f -> Color.Yellow
        else -> Color.Red
    }

    // Progress bar
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(horizontal = 12.dp)
    ) {
        // Background bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        // Foreground bar representing progress
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .clip(RoundedCornerShape(8.dp))
                .background(progressColor)
        )
        // Progress text
        Text(
            text = "${String.format("%.2f", currentAmount)} / ${String.format("%.2f", totalAmount)}",
            color = Color.Black,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}