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
fun LoginScreen(onGoogleSignInClicked: () -> Unit, modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TripWise",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = {
                onGoogleSignInClicked();
            }
        ) {
            Text("Google")
        }
        Button(
            onClick = { onGoogleSignInClicked() } // TODO: Add Apple Sign In
        ) {
            Text("Apple")
        }
    }
}