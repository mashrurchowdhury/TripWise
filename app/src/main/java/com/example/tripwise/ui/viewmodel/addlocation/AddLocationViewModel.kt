package com.example.tripwise.ui.viewmodel.addlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {
    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions

    private val _selectedPlaceCoordinates = MutableStateFlow<Pair<Double, Double>>(Pair(0.0, 0.0))
    val selectedPlaceCoordinates: StateFlow<Pair<Double, Double>> = _selectedPlaceCoordinates

    /**
     * Fetch autocomplete predictions for the given query.
     */
    fun fetchAutocompletePredictions(query: String) {
        if (query.isEmpty()) {
            _predictions.value = emptyList()
            return
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                _predictions.value = response.autocompletePredictions
            }
            .addOnFailureListener { exception ->
                _predictions.value = emptyList()
                exception.printStackTrace()
            }
    }

    /**
     * Fetch the lat/lng coordinates of the selected place.
     */
    fun fetchPlaceCoordinates(placeId: String) {
        viewModelScope.launch {
            try {
                val location = fetchPlaceCoordinatesSuspend(placeId)
                if (location != null) {
                    _selectedPlaceCoordinates.value = Pair(location.latitude, location.longitude)
                } else {
                    _selectedPlaceCoordinates.value = Pair(0.0, 0.0) // Handle no location case
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedPlaceCoordinates.value = Pair(0.0, 0.0) // Handle error case
            }
        }
    }

    private suspend fun fetchPlaceCoordinatesSuspend(placeId: String): LatLng? {
        return suspendCancellableCoroutine { continuation ->
            val request = FetchPlaceRequest.newInstance(
                placeId,
                listOf(Place.Field.LAT_LNG)
            )

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val location = response.place.location
                    continuation.resumeWith(Result.success(location))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }
}
