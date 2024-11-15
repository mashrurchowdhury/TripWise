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
import androidx.compose.material3.Scaffold
import androidx.navigation.NavHostController

@Composable
fun DashboardScreen(
    modifier: Modifier,
    navController: NavHostController,
    signInViewModel: SignInViewModel
) {
    val userFirstName = signInViewModel.getCurrentUser()?.displayName?.split(" ")?.get(0);
    val firestoreRepository = FirestoreRepository()
    var trips by remember { mutableStateOf(listOf<Trip>()) }

    LaunchedEffect(navController.currentBackStackEntry) {
        signInViewModel.getCurrentUser()?.let {
            try {
                trips = firestoreRepository.getTrips(it.uid)
                Log.d("DashboardScreen", "Fetched trips: $trips")
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching trips", e)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = modifier,
                onClick = { navController.navigate("add-edit-trip?editMode=false") }
            ) {
                Icon(Icons.Filled.Add, "Add a new trip.")
            }
        }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = userFirstName?.let { "$it's Trips" } ?: "Trips",
                fontSize = 30.sp,
                modifier = modifier.padding(horizontal = 16.dp, vertical = 25.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = trips, key = { it.id }) { trip ->
                    TripListItem(
                        trip = trip,
                        onClick = { Log.d("Navigation", "Navigating to details screen with tripId: ${trip.id}")
                            try {
                                navController.navigate("details?tripId=${trip.id}")
                            } catch (e: Exception) {
                                Log.e("NavigationError", "Failed to navigate to details screen", e)
                            }
                        },
                        onEditClick = { navController.navigate("add-edit-trip?editMode=true&tripId=${trip.id}") }
                    )
                }
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { signInViewModel.signOut() }) {
                Text("Sign Out")
            }
        }
    }
}