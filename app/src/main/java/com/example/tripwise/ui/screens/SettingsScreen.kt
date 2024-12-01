import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.data.Settings
import kotlinx.coroutines.launch
import android.util.Log
import com.example.tripwise.ui.viewmodel.auth.SignInViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    firestoreRepository: FirestoreRepository = FirestoreRepository(),
    signInViewModel: SignInViewModel
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var homeCurrency by remember { mutableStateOf(TextFieldValue("")) }
    val profilePic: Painter = painterResource(id = R.drawable.placeholder_profile)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val settings = firestoreRepository.getUserSettings()
            settings?.let {
                name = TextFieldValue(it.name)
                homeCurrency = TextFieldValue(it.homeCurrency)
            }
        } catch (e: Exception) {
            Log.e("SettingsScreen", "Error fetching user settings", e)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Picture
        Image(
            painter = profilePic,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .clickable {
                    // Handle profile picture change here (e.g., show image picker)
                }
        )

        Spacer(modifier = modifier.height(20.dp))

        // Name Field
        Text(
            text = "Name",
            fontSize = 18.sp,
            color = Color.Gray
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        Spacer(modifier = modifier.height(20.dp))

        Text(
            text = "Home Currency",
            fontSize = 18.sp,
            color = Color.Gray
        )

        TextField(
            value = homeCurrency,
            onValueChange = { homeCurrency = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Sign Out Button
            Button(
                onClick = {
                    signInViewModel.signOut()
                }
            ) {
                Text("Sign Out")
            }

            // Save Button
            Button(
                onClick = {
                    val settings = Settings(
                        name = name.text,
                        homeCurrency = homeCurrency.text
                    )
                    coroutineScope.launch {
                        try {
                            firestoreRepository.updateUserSettings(settings)
                            signInViewModel.updateUserInformation(settings.name)
                            Toast.makeText(context, "Settings updated successfully", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("SettingsScreen", "Error updating settings", e)
                        }
                    }
                }
            ) {
                Text("Save")
            }
        }
    }
}