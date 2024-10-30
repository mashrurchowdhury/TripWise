package com.example.tripwise.ui.screens

import com.example.tripwise.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.tripwise.ui.theme.TripWiseGray
import com.example.tripwise.ui.theme.TripWiseGreen

@Composable
fun LoginScreen(onGoogleSignInClicked: () -> Unit, modifier: Modifier) {
    val customFont = FontFamily(Font(R.font.questa_sans_regular))

    Column(
        modifier = modifier.fillMaxSize().background(color = TripWiseGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TripWise Logo",
            modifier = Modifier.padding(bottom = 80.dp).size(220.dp),
        )
        Button(
            onClick = { /* TODO: Implement Sign Up functionality */ },
            colors = ButtonDefaults.buttonColors(containerColor = TripWiseGray),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .width(220.dp)
                .graphicsLayer {
                    shadowElevation = 4.dp.toPx()
                    shape = RoundedCornerShape(10.dp)
                    clip = true
                },
        ) {
            Text(
                "Sign Up",
                style = TextStyle(
                    fontFamily = customFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
        }
        Button(
            onClick = { onGoogleSignInClicked() },
            colors = ButtonDefaults.buttonColors(containerColor = TripWiseGray),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(bottom = 20.dp)
                .width(220.dp)
                .graphicsLayer {
                    shadowElevation = 4.dp.toPx()
                    shape = RoundedCornerShape(10.dp)
                    clip = true
                },
        ) {
            Text(
                "Log In with Google",
                style = TextStyle(
                    fontFamily = customFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
        }
    }
}