import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.rememberNavController
import com.example.tripwise.ui.navigation.AppNavigation
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun TripWiseApp(googleSignInClient: GoogleSignInClient) {
    val navController = rememberNavController()

    AppNavigation(
        googleSignInClient = googleSignInClient,
        navController = navController,
    )
}