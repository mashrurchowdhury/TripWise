package com.example.tripwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SmallFloatingActionButton
import com.example.tripwise.data.Trip
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.TripListItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

@Composable
fun DashboardScreen(modifier: Modifier, navController: NavHostController) {
    val user = FirebaseAuth.getInstance().currentUser
    val userFirstName = user?.displayName?.split(" ")?.get(0)
    val firestoreRepository = FirestoreRepository()
    var trips by remember { mutableStateOf(listOf<Trip>()) }

    LaunchedEffect(user) {
        user?.let {
            try {
                trips = firestoreRepository.getTrips(it.uid)
                Log.d("DashboardScreen", "Fetched trips: $trips")
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching trips", e)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { navController.navigate("add-edit?editMode=false") }
            ) {
                Icon(Icons.Filled.Add, "Add a new trip.")
            }
        },
        content = {
            Column(
                modifier = modifier.fillMaxSize(),
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
                            onEditClick = { navController.navigate("add-edit?editMode=true&tripId=${trip.id}") }
                        )
                    }
                    item {
                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                    }
                }
            }
        }
    )
}