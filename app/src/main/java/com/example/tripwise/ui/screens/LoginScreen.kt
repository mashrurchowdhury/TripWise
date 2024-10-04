package com.example.tripwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TripWise",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = { /* Handle Google login */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Google")
        }
        Button(
            onClick = { /* Handle Apple login */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apple")
        }
    }
}