package com.example.tripwise.ui.screens

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource
import com.example.tripwise.ui.viewmodel.auth.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.tripwise.ui.theme.TripWiseGray
import com.example.tripwise.ui.theme.TripWiseGreen

@Composable
fun LoginScreen(
    modifier: Modifier,
    googleSignInClient: GoogleSignInClient,
    onEmailOptionClicked: () -> Unit,
    signInViewModel: SignInViewModel
) {
    val customFont = FontFamily(Font(R.font.questa_sans_regular))
    var triggerGoogleSignIn = true

    // Handle the Google Sign-In result
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            triggerGoogleSignIn = false

            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)!!

                // Trigger Firebase authentication with Google
                signInViewModel.signInWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Lets user attempt to Sign In with Google again
                triggerGoogleSignIn = true
                Log.w("GoogleSignInScreen", "Google sign-in failed", e)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = TripWiseGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TripWise Logo",
            modifier = Modifier
                .padding(bottom = 80.dp)
                .size(220.dp),
        )
        Button(
            onClick = { onEmailOptionClicked() },
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
                "Sign In/Register",
                style = TextStyle(
                    fontFamily = customFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            )
        }
        Button(
            onClick = {
                googleSignInClient.signOut()

                // Launch the Google sign-in intent
                if (triggerGoogleSignIn) {
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                }
            },
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
