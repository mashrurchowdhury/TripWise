package com.example.tripwise.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.tripwise.data.Trip
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.TripListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import com.example.tripwise.ui.viewmodel.auth.SignInViewModel
import androidx.navigation.NavHostController
import com.example.tripwise.ui.viewmodel.map.MapViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signInViewModel: SignInViewModel,
    mapViewModel: MapViewModel,
) {
    val userFirstName = signInViewModel.getCurrentUser()?.displayName?.split(" ")?.get(0)
    val firestoreRepository = FirestoreRepository()
    var trips by remember { mutableStateOf(listOf<Trip>()) }

    LaunchedEffect(navController.currentBackStackEntry) {
        signInViewModel.getCurrentUser()?.let {
            try {
                trips = firestoreRepository.getTrips(it.uid).sortedByDescending { trip -> trip.endDate } // Sort trips by date descending
                Log.d("DashboardScreen", "Fetched trips: $trips")
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching trips", e)
            }

            mapViewModel.fetchExpensesWithLocations()
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier.padding(bottom = 64.dp),
                onClick = { navController.navigate("add-edit-trip?editMode=false") }
            ) {
                Icon(Icons.Filled.Add, "Add a new trip.")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = userFirstName?.let { "$it's Trips" } ?: "Trips",
                fontSize = 28.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = trips, key = { it.id }) { trip ->
                    TripListItem(
                        trip = trip,
                        onClick = {
                            Log.d("Navigation", "Navigating to details screen with tripId: ${trip.id}")
                            try {
                                navController.navigate("details?tripId=${trip.id}")
                            } catch (e: Exception) {
                                Log.e("NavigationError", "Failed to navigate to details screen", e)
                            }
                        },
                        onEditClick = {
                            navController.navigate("add-edit-trip?editMode=true&tripId=${trip.id}")
                        }
                    )
                }
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }
        }
    }
}
