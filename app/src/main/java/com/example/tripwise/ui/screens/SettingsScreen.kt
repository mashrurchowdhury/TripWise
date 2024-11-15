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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripwise.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(TextFieldValue("John Doe")) }
    val profilePic: Painter = painterResource(id = R.drawable.placeholder_profile)

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

        // Save Button
        Button(
            onClick = { /* Handle save action */ },
            modifier = modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}
