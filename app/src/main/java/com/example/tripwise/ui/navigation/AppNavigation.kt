package com.example.tripwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tripwise.ui.screens.LoginScreen
import com.example.tripwise.ui.screens.DashboardScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier, onGoogleSignInClicked: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                modifier = modifier,
                onGoogleSignInClicked = {
                    onGoogleSignInClicked()
                },
                onLoginClicked = {
                    navController.navigate("dashboard")
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(modifier = modifier)
        }
    }
}