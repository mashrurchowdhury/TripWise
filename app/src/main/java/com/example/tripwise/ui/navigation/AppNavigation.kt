package com.example.tripwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripwise.ui.screens.LoginScreen
import com.example.tripwise.ui.screens.DashboardScreen
import com.example.tripwise.ui.screens.TripDetailScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                modifier = modifier,
                onLoginClicked = {
                    // Navigate to the next screen
                    navController.navigate("dashboard")
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                modifier = modifier
            )
        }
        composable(route = "trip") {
            TripDetailScreen(
                modifier = modifier,
                onBackClick = { navController.popBackStack() } // Use popBackStack for back navigation
            )
        }
    }
}