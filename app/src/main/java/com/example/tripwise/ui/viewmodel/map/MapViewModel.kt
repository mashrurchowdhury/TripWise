package com.example.tripwise.ui.viewmodel.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.presentation.MapScreenEvent
import com.google.android.libraries.mapsplatform.transportation.consumer.model.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestoreRepository: FirestoreRepository // Injecting repository for testability
) : ViewModel() {

    // Event channel to communicate one-time events to the UI
    private val _eventChannel = Channel<MapScreenEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    // StateFlow to hold expenses with locations
    private val _expensesWithLocations = MutableStateFlow<List<Expense>>(emptyList())
    val expensesWithLocations: StateFlow<List<Expense>> = _expensesWithLocations.asStateFlow()

    // StateFlow to manage loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchExpensesWithLocations()
    }

    /**
     * Fetch trips and their associated expenses, filtering for those with location attributes.
     */
    fun fetchExpensesWithLocations(tripId: String? = null) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.w("MapViewModel", "User not logged in")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch all trips
                val trips = firestoreRepository.getTrips(currentUser.uid)
                val expensesWithLocation = mutableListOf<Expense>()

                if (tripId != null) {
                    val expenses = firestoreRepository.getExpenses(currentUser.uid, tripId)
                    // expensesWithLocation.addAll(expenses.filter { it.location != null })
                    expensesWithLocation.addAll(expenses)
                } else {
                    for (trip in trips) {
                        val expenses = firestoreRepository.getExpenses(currentUser.uid, trip.id)
                        // expensesWithLocation.addAll(expenses.filter { it.location != null })
                        expensesWithLocation.addAll(expenses)
                    }
                }
                _expensesWithLocations.value = expensesWithLocation
                Log.d("MapViewModel", "Fetched expenses with locations: $expensesWithLocation")
            } catch (e: FirebaseFirestoreException) {
                Log.e("MapViewModel", "Error fetching trips or expenses", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}