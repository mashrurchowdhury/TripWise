import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.data.Expense
import com.example.tripwise.ui.viewmodel.map.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import kotlin.random.Random

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel,
) {
    // Collect the expenses with locations and loading state
    val expensesWithLocations by mapViewModel.expensesWithLocations.collectAsState()
    val isLoading by mapViewModel.isLoading.collectAsState()

//    // Calculate the bounding box
//    val latLngBounds = expensesWithLocations.let { locations ->
//        if (locations.isEmpty()) null
//        else {
//            val builder = LatLngBounds.Builder()
//            locations.forEach { builder.include(LatLng(it.lat, it.lng)) }
//            builder.build()
//        }
//    }
//
//    // Camera position state
//    val cameraPositionState = rememberCameraPositionState()
//
//    // Set the camera to fit the bounding box if available
//    latLngBounds?.let { bounds ->
//        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100) // 100px padding
//        cameraPositionState.move(cameraUpdate)
//    }

    GoogleMap(
        modifier = modifier.fillMaxWidth(),
//        cameraPositionState = cameraPositionState
    ) {
        Polyline(
            points = expensesWithLocations.map { LatLng(it.lat, it.lng) },
            clickable = true,
            color = Color.Blue,
            width = 5f
        )
    }
}