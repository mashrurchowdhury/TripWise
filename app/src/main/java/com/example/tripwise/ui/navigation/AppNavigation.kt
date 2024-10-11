package com.example.tripwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tripwise.ui.screens.LoginScreen
import com.example.tripwise.ui.screens.DashboardScreen
import com.example.tripwise.ui.screens.TripDetailScreen
import com.example.tripwise.ui.screens.AddEditScreen

@Composable
fun AppNavigation(navController: NavHostController, onGoogleSignInClicked: () -> Unit, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                modifier = modifier,
                onGoogleSignInClicked = {
                    onGoogleSignInClicked()
                },
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
            DashboardScreen(modifier = modifier, onAddClick = {
                // Navigate to the add screen
                navController.navigate("add-edit")
            }, onEditClick = {
            // Navigate to the add screen
            navController.navigate("add-edit")
            })
        }
        composable("add-edit") {
            AddEditScreen(modifier = modifier, onBackClick = {
                // Go to previous screen
                navController.navigateUp()
            })
        }
    }
}