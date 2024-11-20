import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.google.maps.android.compose.GoogleMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MapScreen(
        modifier: Modifier = Modifier,
) {
    var isMapLoaded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Add GoogleMap here
        GoogleMap(
            modifier = modifier.fillMaxWidth(),
            onMapLoaded = { isMapLoaded = true },
        )
    }
}