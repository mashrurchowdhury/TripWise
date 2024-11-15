package com.example.tripwise.ui.navigation

import android.widget.Toast
import EmailPasswordScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.tripwise.ui.screens.AddEditExpenseScreen
import com.example.tripwise.ui.screens.LoginScreen
import com.example.tripwise.ui.screens.DashboardScreen
import com.example.tripwise.ui.screens.AddEditScreen
import com.example.tripwise.ui.screens.TripDetailScreen
import com.example.tripwise.ui.viewmodel.auth.LoginState
import com.example.tripwise.ui.viewmodel.auth.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun AppNavigation(
    navController: NavHostController,
    googleSignInClient: GoogleSignInClient,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val loginState = signInViewModel.loginState.collectAsState(initial = LoginState.Initial).value

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Initial -> {}
            is LoginState.Loading -> {}
            is LoginState.CreateAccountSuccess -> navController.navigate("dashboard") { popUpTo("dashboard") }
            is LoginState.CreateAccountError -> {
                Toast.makeText(context, "Error Creating Account, " + loginState.message, Toast.LENGTH_SHORT).show()
            }
            is LoginState.LoginSuccess -> navController.navigate("dashboard") { popUpTo("dashboard") }
            is LoginState.LoginError -> {
                Toast.makeText(context, "Error Logging In, " + loginState.message, Toast.LENGTH_SHORT).show()
            }
            is LoginState.LoggedOut -> navController.navigate("login") { popUpTo("login") }
            is LoginState.LoggedOutError -> {
                Toast.makeText(context, "Error Logging Out, " + loginState.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                modifier = modifier,
                googleSignInClient = googleSignInClient,
                onEmailOptionClicked = { navController.navigate("email-password") },
                signInViewModel = signInViewModel,
            )
        }
        composable("email-password") {
            EmailPasswordScreen(
                onSignIn = {
                    navController.navigate("dashboard") { popUpTo("dashboard") { inclusive = true } }
                },
                onBackClick = {
                    // Go to previous screen
                    navController.navigateUp()
                },
                signInViewModel = signInViewModel,
            )
        }
        composable("dashboard") {
            DashboardScreen(
                modifier = modifier,
                signInViewModel = signInViewModel,
                navController = navController
            )
        }
        composable("details/{tripID}") {backStackEntry ->
            val tripID = backStackEntry.arguments?.getString("tripID")?.toLongOrNull()
//            tripID?.let {
                TripDetailScreen (
                    modifier = modifier,
                    onBackClick = {
                        // Go to previous screen
                        navController.navigateUp()
                    },
                    onAddClick = {
                        navController.navigate("add-edit-expense")
                    },
                    onEditClick = {
                        navController.navigate("add-edit-expense")
                    }
                )
//            }
        }
        composable("add-edit-expense") {
            AddEditExpenseScreen(
                modifier = modifier,
                onBackClick = {
                    navController.navigateUp()
                })
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
                editMode = editMode,
                tripId = tripId,
                onBackClick = {
                    // Go to previous screen
                    navController.navigateUp()
                },
                onSubmit = {
                    navController.navigate(route = "dashboard") {
                        popUpTo(route = "add-edit") { inclusive = true }
                    }
                }
            )
        }
    }
}