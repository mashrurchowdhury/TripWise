package com.example.tripwise.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.ExpenseListItem
import com.example.tripwise.ui.components.ProgressBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onBackClick: () -> Unit,
    tripId: String
) {
    val firestoreRepository = FirestoreRepository()
    val user = FirebaseAuth.getInstance().currentUser
    var expenses by remember { mutableStateOf(listOf<Expense>()) }
    var expenseTotal by remember { mutableStateOf(0.0) }
    var budget by remember { mutableStateOf(0.0) }
    var showLocalCurrency by remember { mutableStateOf(false) }
    var homeCurrency by remember { mutableStateOf("CAD") }
    var tripName by remember { mutableStateOf("Trip Expenses") }

    LaunchedEffect(navController.currentBackStackEntry) {
        user?.let {
            try {
                expenses = firestoreRepository.getExpenses(it.uid, tripId)
                expenseTotal = expenses.sumOf { expense -> expense.convertedCost ?: 0.0 }
                budget = firestoreRepository.getTrip(it.uid, tripId)?.budget ?: 0.0
                homeCurrency = firestoreRepository.getUserSettings()?.homeCurrency ?: "CAD"
                tripName = firestoreRepository.getTrip(it.uid, tripId)?.name ?: "Trip Expenses"

                Log.d("TripDetailScreen", "Fetched expenses: $expenses")
            } catch (e: Exception) {
                Log.e("TripDetailScreen", "Error fetching expenses", e)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = tripName,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add-edit-expense?editMode=false&tripId=$tripId")
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add a new expense")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Currency Toggle
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

                // Progress Bar with Gradient
                if (budget > 0.0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${String.format("%.2f", expenseTotal)} / ${String.format("%.2f", budget)}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        ProgressBar(
                            currentAmount = expenseTotal,
                            totalAmount = budget
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Expense List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                }
            }
        }
    )
}
