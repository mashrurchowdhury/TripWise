import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tripwise.ui.navigation.AppNavigation

@Composable
fun TripWiseApp(onGoogleSignInClicked: () -> Unit) {
    val navController = rememberNavController()
    TripWiseNavHost(
        onGoogleSignInClicked = onGoogleSignInClicked,
        navController = navController
    )
}

@Composable
fun TripWiseNavHost(
    onGoogleSignInClicked: () -> Unit,
    navController: NavHostController
) {
    val activity = (LocalContext.current as Activity)
    AppNavigation(navController, onGoogleSignInClicked)
}