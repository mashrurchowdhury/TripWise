import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.rememberNavController
import com.example.tripwise.ui.navigation.AppNavigation

@Composable
fun TripWiseApp(
    onGoogleSignInClicked: () -> Unit,
    shouldNavigateToDashboard: MutableState<Boolean>
) {
    val navController = rememberNavController()
    AppNavigation(
        onGoogleSignInClicked = onGoogleSignInClicked,
        shouldNavigateToDashboard = shouldNavigateToDashboard,
        navController = navController,
    )
}