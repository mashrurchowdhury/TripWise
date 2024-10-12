package com.example.tripwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.tripwise.ui.screens.LoginScreen
import com.example.tripwise.ui.screens.DashboardScreen
import com.example.tripwise.ui.screens.AddEditScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    onGoogleSignInClicked: () -> Unit,
    shouldNavigateToDashboard: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                modifier = modifier,
                onGoogleSignInClicked = {
                    onGoogleSignInClicked()
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(modifier = modifier, navController = navController)
        }
        composable(
            route = "add-edit?editMode={editMode}&tripId={tripId}",
            arguments = listOf(
                navArgument("editMode") { type = NavType.BoolType },
                navArgument("tripId") { type = NavType.StringType; nullable = true }
            )
        ) { backStackEntry ->
            val editMode = backStackEntry.arguments?.getBoolean("editMode") ?: false
            val tripId = backStackEntry.arguments?.getString("tripId")
            AddEditScreen(
                modifier = modifier,
                navController = navController,
                editMode = editMode,
                tripId = tripId,
                onBackClick = {
                    // Go to previous screen
                    navController.navigateUp()
                }
            )
        }
    }

    // Navigate to dashboard if the state is true
    LaunchedEffect(shouldNavigateToDashboard.value) {
        if (shouldNavigateToDashboard.value) {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
            }
            shouldNavigateToDashboard.value = false
        }
    }
}