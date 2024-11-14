package com.example.tripwise.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.ExpenseListItem
import com.example.tripwise.ui.components.ProgressBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(modifier: Modifier = Modifier,
                     navController: NavHostController,
                     onBackClick: () -> Unit, //TODO: Implement
                     tripId: String) {
    val firestoreRepository = FirestoreRepository()
    val user = FirebaseAuth.getInstance().currentUser
    var expenses by remember { mutableStateOf(listOf<Expense>()) }

    LaunchedEffect(navController.currentBackStackEntry) {
        user?.let {
            try {
                expenses = firestoreRepository.getExpenses(it.uid, tripId)
                Log.d("DashboardScreen", "Fetched expenses: $expenses")
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching expenese", e)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trip " + " Expenses", //TODO get the trip name
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
                onClick = {navController.navigate("add-edit-expense?editMode=false&tripId=$tripId")}
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
                // Heading
                Text(
                    text = "Expense Summary",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )

                // Progress bar (you can adjust the progress value as needed)
                ProgressBar(
                    currentAmount = 1550f,
                    totalAmount = 3000f
                )

                // Expense list
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = expenses, key = { it.id }) { expense ->
                        ExpenseListItem(
                            expense = expense,
                            onEditClick = {navController.navigate("add-edit-expense?editMode=true&tripId=$tripId&expenseId=${expense.id}")},
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
