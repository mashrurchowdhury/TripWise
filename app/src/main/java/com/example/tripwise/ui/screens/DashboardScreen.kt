package com.example.tripwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SmallFloatingActionButton
import com.example.tripwise.data.Trip
import com.example.tripwise.ui.components.TripListItem
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold

@Composable
fun DashboardScreen(modifier: Modifier) {
    val trips = listOf(
        Trip(1,
            "Berlin",
            listOf("Ingrid"),
            "Sample Trip",
            "2024-10-20",
            "2024-11-06"),
        Trip(2,
            "San Francisco",
            listOf("Ingrid", "Rachel", "Kevin", "Mashrur"),
            "Sample Trip 2",
            "2025-01-25",
            "2025-03-02"),
    )

    Scaffold(
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { /* Add new trip */ }
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
                    text = "Trips",
                    fontSize = 30.sp,
                    modifier = modifier.padding(horizontal = 16.dp, vertical = 25.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = trips, key = { it.id }) { trip ->
                        TripListItem(
                            trip = trip,
//                          navigateToDetail = { /* Navigation to detail */ }
//                          toggleSelection: {  }
                        )
                    }
                    // Add extra spacing at the bottom if
                    item {
                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                    }
                }
            }
        }
    )
}