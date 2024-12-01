package com.example.tripwise.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.ExpenseListItem
import com.example.tripwise.ui.components.ProgressBar
import com.example.tripwise.ui.viewmodel.map.MapViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBackClick: () -> Unit,
    tripId: String,
    mapViewModel: MapViewModel,
) {
    val firestoreRepository = FirestoreRepository()
    val user = FirebaseAuth.getInstance().currentUser
    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    var expenseTotal by remember { mutableStateOf(0.0) }
    var budget by remember { mutableStateOf(0.0) }
    var showLocalCurrency by remember { mutableStateOf(false) }
    var homeCurrency by remember { mutableStateOf("CAD") }

    LaunchedEffect(navController.currentBackStackEntry) {
        user?.let {
            try {
                expenses = firestoreRepository.getExpenses(it.uid, tripId)
                expenseTotal = expenses.sumOf{ expense -> expense.convertedCost ?: 0.0 }
                budget = firestoreRepository.getTrip(it.uid, tripId)?.budget ?: 0.0
                homeCurrency = firestoreRepository.getUserSettings()?.homeCurrency ?: "CAD"

                mapViewModel.fetchExpensesWithLocations(tripId)

                Log.d("DashboardScreen", "Fetched expenses: $expenses")
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching expenses", e)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trip Expenses", // TODO: Change to trip name
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {},
                modifier = Modifier.fillMaxWidth()
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier.padding(bottom = 64.dp),
                onClick = {
                    navController.navigate("add-edit-expense?editMode=false&tripId=$tripId")
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add a new expense")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Toggle for currency display
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Local Currency", modifier = Modifier.padding(end = 8.dp))
                    Switch(
                        checked = showLocalCurrency,
                        onCheckedChange = { showLocalCurrency = it }
                    )
                    Text(text = "Home Currency", modifier = Modifier.padding(start = 8.dp))
                }

                // Heading
                Text(
                    text = "Expense Summary",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // Progress bar
                if (budget > 0.0) {
                    ProgressBar(
                        currentAmount = expenseTotal,
                        totalAmount = budget
                    )
                }

                // Expense list
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = expenses, key = { it.id }) { expense ->
                        ExpenseListItem(
                            expense = expense,
                            showLocalCurrency = showLocalCurrency,
                            homeCurrency = homeCurrency,
                            onEditClick = {
                                navController.navigate("add-edit-expense?editMode=true&tripId=$tripId&expenseId=${expense.id}")
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Spacer(Modifier.height(50.dp))
                    }
                }
            }
        }
    )
}