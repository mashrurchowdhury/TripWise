package com.example.tripwise.ui.screens

import com.example.tripwise.ui.viewmodel.addlocation.AddLocationViewModel
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.tripwise.ui.viewmodel.addedit.ValidationState
import android.widget.Toast
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.viewmodel.addedit.AddEditExpenseViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddEditExpenseScreen(addEditExpenseViewModel: AddEditExpenseViewModel = hiltViewModel(),
                         modifier: Modifier = Modifier,
                         editMode: Boolean,
                         onBackClick: () -> Unit,
                         onSubmit: () -> Unit,
                         tripId: String,
                         expenseId: String? = null,
                         addLocationViewModel: AddLocationViewModel = hiltViewModel(),
) {
    Log.d("Edit Expenses", "Edit mode $editMode")
    val context = LocalContext.current
    val firestoreRepository = FirestoreRepository()

    var editableExpense by remember { mutableStateOf<Expense?>(null) }
    val user = FirebaseAuth.getInstance().currentUser

    var addressQuery by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }
    val predictions by addLocationViewModel.predictions.collectAsState()
    val selectedPlaceCoordinates by addLocationViewModel.selectedPlaceCoordinates.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            try {
                if (editMode  && expenseId != null) {
                    editableExpense = firestoreRepository.getExpense(it.uid, tripId, expenseId)
                    editableExpense?.let { expense ->
                        addEditExpenseViewModel.onPrefill(expense)
                    }
                    Log.d("Expense Filled", "Prefilled expense $editableExpense")
                    Log.d("EditExpenseScreen", "Fetched expense: $editableExpense")
                }
            } catch (e: Exception) {
                Log.e("EditExpenseScreen", "Error fetching expense", e)
            }
        }
    }

    LaunchedEffect(key1 = context) {
        addEditExpenseViewModel.validationEvent.collect { event ->
            when (event) {
                is ValidationState.ExpenseSuccess -> {
                    val expense = event.expense
                    println("Registered Expense is $expense")
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()

                    onSubmit()
                }
                else -> {}
            }
        }
    }

    // Observe changes to selectedPlaceCoordinates
    LaunchedEffect(selectedPlaceCoordinates) {
        if (selectedPlaceCoordinates != Pair(0.0, 0.0)) {
            addEditExpenseViewModel.onAction(
                ExpenseEvent.LocationSubmitted(selectedPlaceCoordinates)
            )
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
                    if (editMode && editableExpense == null) {
                        CircularProgressIndicator(modifier = modifier)
                    } else {
                        InputField(
                            onValueChanged = {addEditExpenseViewModel.onAction(ExpenseEvent.NameChanged(it)) },
                            label = "Expense Name",
                            isError = addEditExpenseViewModel.errorState.value.nameStatus,
                            error = "Please enter a non-empty name",
                            value = editableExpense?.name ?: "",
                        )

                        InputField(
                            onValueChanged = {addEditExpenseViewModel.onAction(ExpenseEvent.AmountChanged(it)) },
                            label = "Amount",
                            isError = addEditExpenseViewModel.errorState.value.amountStatus,
                            error = "Please enter a valid amount",
                            value = if ((editableExpense?.cost ?: 0.0) > 0.0) editableExpense?.cost.toString() else "",
                        )

                        //TODO: Change to a drop down field
                        InputField(
                            label = "Currency",
                            onValueChanged = { addEditExpenseViewModel.onAction(ExpenseEvent.CurrencyChanged(it)) },
                            isError = addEditExpenseViewModel.errorState.value.currencyStatus,
                            error = "Please enter a valid currency",
                            value = editableExpense?.currency ?: ""
                        )

                        Column(modifier = Modifier.padding(16.dp)) {
                            TextField(
                                value = addressQuery,
                                onValueChange = {
                                    addressQuery = it
                                    addLocationViewModel.fetchAutocompletePredictions(it)
                                    showDropdown = it.isNotEmpty()
                                },
                                label = { Text("Address") },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = showDropdown && predictions.isNotEmpty(),
                                onDismissRequest = { showDropdown = false },
                                modifier = Modifier.fillMaxWidth(),
                                properties = PopupProperties(focusable = false)
                            ) {
                                predictions.forEach { prediction ->
                                    DropdownMenuItem(
                                        onClick = {
                                            addressQuery = prediction.getPrimaryText(null).toString()
                                            showDropdown = false
                                            addLocationViewModel.fetchPlaceCoordinates(prediction.placeId)
                                        },
                                        text = { Text(prediction.getPrimaryText(null).toString()) }
                                    )
                                }
                            }
                        }

                        InputField(
                            onValueChanged = { addEditExpenseViewModel.onAction(ExpenseEvent.DateChanged(it)) },
                            label = "Date (YYYY-MM-DD)",
                            isError = addEditExpenseViewModel.errorState.value.datesStatus,
                            error = "Please enter a valid date",
                            value = editableExpense?.date ?: "",
                        )

                        OutlinedButton(
                            onClick = {
                                user?.let { user ->
                                    if (editMode && expenseId != null) {
                                        addEditExpenseViewModel.updateExpense(user.uid, tripId, expenseId)
                                    } else {
                                        addEditExpenseViewModel.submitExpense(user.uid, tripId)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 50.dp, top = 10.dp, end = 50.dp),
                        ) {
                            Text("Submit Expense")
                        }

                        if (editMode && expenseId != null) {
                            OutlinedButton(
                                onClick = {
                                    user?.let {
                                        addEditExpenseViewModel.deleteExpense(it.uid, tripId, expenseId)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 50.dp, top = 10.dp, end = 50.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    )
}