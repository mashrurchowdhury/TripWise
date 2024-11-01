package com.example.tripwise.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import com.example.tripwise.data.Expense
import com.example.tripwise.ui.components.ExpenseListItem
import com.example.tripwise.ui.components.ProgressBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    val expenses = listOf(
        Expense(
            expenseId = 1,
            tripId = 1,
            name = "Plane Ticket",
            cost = 1200.00,
            currency = "CAD",
            date = "2024-11-06"
        ),
        Expense(
            expenseId = 2,
            tripId = 1,
            name = "Food",
            cost = 300.00,
            currency = "CAD",
            date = "2024-11-06"
        ),
        Expense(
            expenseId = 3,
            tripId = 1,
            name = "Taxi",
            cost = 50.00,
            currency = "CAD",
            date = "2024-11-06"
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trip " + expenses.get(0).tripId + " Expenses",
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
                onClick = { /* Add new expense */ },
                modifier = Modifier.padding(16.dp)
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
                    items(items = expenses, key = { it.expenseId }) { expense ->
                        ExpenseListItem(
                            expense = expense,
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
