import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.data.Expense
import com.example.tripwise.ui.viewmodel.map.MapViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlin.random.Random

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
) {
    // Collect the expenses with locations and loading state
    val expensesWithLocations by mapViewModel.expensesWithLocations.collectAsState()
    val isLoading by mapViewModel.isLoading.collectAsState()

    // Calculate the bounding box
//    val latLngBounds = expensesWithLocations.mapNotNull { it.location }.let { locations ->
//        if (locations.isEmpty()) null
//        else {
//            val builder = LatLngBounds.Builder()
//            locations.forEach { builder.include(LatLng(it.latitude, it.longitude)) }
//            builder.build()
//        }
//    }

    // Camera position state
    val cameraPositionState = rememberCameraPositionState()

    // Set the camera to fit the bounding box if available
//    latLngBounds?.let { bounds ->
//        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100) // 100px padding
//        cameraPositionState.move(cameraUpdate)
//    }

    GoogleMap(
        modifier = modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState
    ) {
        // Add markers for each expense with a location
        expensesWithLocations.forEach { expense ->
            // Random for now
            val location = LatLng(Random.nextInt(30, 70) + 0.52345293, Random.nextInt(30, 70) + 0.25930486)
            Marker(
                state = rememberMarkerState(position = location),
                title = expense.name,
                snippet = "Amount: ${expense.convertedCost}"
            )
        }
    }
}