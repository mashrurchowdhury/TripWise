package com.example.tripwise.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.example.tripwise.ui.viewmodel.addedit.ValidationState
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tripwise.ui.viewmodel.addedit.ExpenseEvent
import com.example.tripwise.ui.common.InputField
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.ui.viewmodel.addedit.AddEditExpenseViewModel
import com.example.tripwise.ui.common.CategoryDropdown
import java.util.Currency

@Composable
fun AddEditExpenseScreen(addEditExpenseViewModel: AddEditExpenseViewModel = hiltViewModel(),
                         modifier: Modifier = Modifier,
                         onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val localFocus = LocalFocusManager.current

    LaunchedEffect(key1 = context) {
        addEditExpenseViewModel.validationEvent.collect { event ->
            when (event) {
                is ValidationState.ExpenseSuccess -> {
                    val expense = event.expense
                    println("Registered Expense is $expense")
                    Toast.makeText(context, "Expense added successfully", Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Button(
                onClick = { onBackClick() }
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go to previous screen")
            }
        },
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    InputField(
                        onValueChanged = {addEditExpenseViewModel.onAction(ExpenseEvent.NameChanged(it)) },
                        label = "Expense Name",
                        isError = addEditExpenseViewModel.errorState.value.nameStatus,
                        error = "Please enter a non-empty name"
                    )

                    // Category Dropdown
                    val categories = listOf("Accommodation", "Entertainment", "Food & Dining", "Health & Wellness",  "Shopping", "Transportation", "Miscellaneous")
                    var selectedCategory by remember { mutableStateOf(categories[0]) }

                    CategoryDropdown(
                        selectedItem = selectedCategory,
                        onItemSelected = { selectedCategory = it },
                        items = categories,
                        label = "Category",
                        itemToString = { it } // For String, just return the item itself
                    )

                    InputField(
                        onValueChanged = {addEditExpenseViewModel.onAction(ExpenseEvent.AmountChanged(it)) },
                        label = "Amount",
                        isError = addEditExpenseViewModel.errorState.value.amountStatus,
                        error = "Please enter a valid amount"
                    )

//                    //TODO: Change to a drop down field
//                    InputField(
//                        label = "Currency",
//                        onValueChanged = { addEditExpenseViewModel.onAction(ExpenseEvent.CurrencyChanged(it)) },
//                        isError = addEditExpenseViewModel.errorState.value.currencyStatus,
//                        error = "Please enter a valid currency"
//                    )

                    val currencies = listOf(Currency.getInstance("USD"),
                        Currency.getInstance("CAD"),
                        Currency.getInstance("EUR"),
                        Currency.getInstance("GBP"),
                        Currency.getInstance("CNY"),
                        Currency.getInstance("JPY"),
                        )
                    var selectedCurrency by remember { mutableStateOf(currencies[0]) }

                    CategoryDropdown(
                        selectedItem = selectedCurrency,
                        onItemSelected = { selectedCurrency = it },
                        items = currencies,
                        label = "Currency",
                        itemToString = { it.currencyCode } // Convert Currency to its code (e.g., "USD")
                    )

                    InputField(
                        onValueChanged = { addEditExpenseViewModel.onAction(ExpenseEvent.DateChanged(it)) },
                        label = "Date (YYYY-MM-DD)",
                        isError = addEditExpenseViewModel.errorState.value.datesStatus,
                        error = "Please enter a valid date"
                    )

                    OutlinedButton(
                        onClick = {
                            addEditExpenseViewModel.onAction(ExpenseEvent.Submit)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, top = 10.dp, end = 50.dp),
                    ) {
                        Text("Submit Expense")
                    }
                }
            }
        }
    )
}