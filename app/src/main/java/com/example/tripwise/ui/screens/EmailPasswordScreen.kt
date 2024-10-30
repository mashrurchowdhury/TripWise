import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.ui.theme.TripWiseGray
import com.example.tripwise.ui.theme.TripWiseGreen
import com.example.tripwise.ui.viewmodel.auth.SignInViewModel

@Composable
fun EmailPasswordScreen(
    signInViewModel: SignInViewModel,
    onSignIn: () -> Unit,
    onBackClick: () -> Unit,
) {
    val customFont = FontFamily(Font(R.font.questa_sans_regular))

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignInMode by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Button(
                onClick = { onBackClick() },
                colors = ButtonDefaults.buttonColors(containerColor = TripWiseGray),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .height(45.dp)
                    .padding(10.dp)
                    .graphicsLayer {
                        shape = RoundedCornerShape(10.dp)
                    },
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, "Go to previous screen"
                )
            }
        },
        content = {
            // UI for email and password input
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = TripWiseGreen),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (isSignInMode) "Sign In" else "Register",
                    style = TextStyle(
                        fontFamily = customFont,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )

                if (!isSignInMode) {
                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .width(300.dp)
                            .graphicsLayer {
                                shape = RoundedCornerShape(10.dp)
                            },
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(300.dp)
                        .graphicsLayer {
                            shape = RoundedCornerShape(10.dp)
                        },
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .width(300.dp)
                        .graphicsLayer {
                            shape = RoundedCornerShape(10.dp)
                        },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isSignInMode) {
                            signInViewModel.signInWithEmail(email, password)
                        } else {
                            signInViewModel.createAccountWithEmail(email, password, name)
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
                    Text(text = if (isSignInMode) "Sign In" else "Register")
                }

                TextButton(
                    onClick = { isSignInMode = !isSignInMode },
                ) {
                    Text(
                        text = if (isSignInMode) "Don't have an account? Register" else "Already have an account? Sign In",
                        style = TextStyle(
                            fontFamily = customFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}