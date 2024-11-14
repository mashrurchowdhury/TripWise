import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem("dashboard", Icons.Default.Home, "Home")
    data object Maps : BottomNavItem("maps", Icons.Default.Place, "Maps")
    data object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}