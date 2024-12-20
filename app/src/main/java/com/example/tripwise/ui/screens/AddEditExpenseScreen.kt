package com.example.tripwise.ui.screens

import android.util.Log
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.common.CategoryDropdown
import com.example.tripwise.ui.common.InputField
import com.example.tripwise.ui.components.DatePickerField
import com.example.tripwise.ui.theme.TripWiseGray
import com.example.tripwise.ui.theme.TripWiseGreen
import com.example.tripwise.ui.viewmodel.addedit.AddEditExpenseViewModel
import com.example.tripwise.ui.viewmodel.addedit.ExpenseEvent
import com.example.tripwise.ui.viewmodel.addedit.ValidationState
import com.example.tripwise.ui.viewmodel.addlocation.AddLocationViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Currency

@Composable
fun AddEditExpenseScreen(
        addEditExpenseViewModel: AddEditExpenseViewModel = hiltViewModel(),
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
    val expenseState by addEditExpenseViewModel._expenseState

    var addressQuery by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }
    val predictions by addLocationViewModel.predictions.collectAsState()
    val selectedPlaceCoordinates by addLocationViewModel.selectedPlaceCoordinates.collectAsState()
    val selectedPlaceAddress by addLocationViewModel.selectedPlaceAddress.collectAsState()

    LaunchedEffect(user) {
        user?.let {
            try {
                if (editMode && expenseId != null) {
                    editableExpense = firestoreRepository.getExpense(it.uid, tripId, expenseId)
                    editableExpense?.let { expense ->
                        addEditExpenseViewModel.onPrefill(expense)
                        addLocationViewModel.fetchPlace(expense.placeId)
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

    // Observe changes to selectedPlaceCoordinates
    LaunchedEffect(selectedPlaceAddress) {
        if (selectedPlaceAddress != "") {
            addressQuery = selectedPlaceAddress
        }
    }

    Scaffold(
            topBar = {
                Button(
                    onClick = { onBackClick() },
                    modifier = Modifier.padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TripWiseGreen)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        "Go to previous screen",
                        tint = TripWiseGray
                    )
                }
            },
            content = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                    ) {
                        if (editMode && editableExpense == null) {
                            CircularProgressIndicator(modifier = modifier)
                        } else {
                            InputField(
                                    onValueChanged = {
                                        addEditExpenseViewModel.onAction(
                                                ExpenseEvent.NameChanged(it)
                                        )
                                    },
                                    label = "Expense Name",
                                    isError = addEditExpenseViewModel.errorState.value.nameStatus,
                                    error = "Please enter a non-empty name",
                                    value = editableExpense?.name ?: "",
                            )

                            // Category Dropdown
                            val categories =
                                    listOf(
                                            "",
                                            "Accommodation",
                                            "Entertainment",
                                            "Food & Dining",
                                            "Health & Wellness",
                                            "Shopping",
                                            "Transportation",
                                            "Miscellaneous"
                                    )

                            var selectedCategory by remember {
                                mutableStateOf(
                                    editableExpense?.category?.takeIf { it.isNotEmpty() } ?: categories[0]
                                )
                            }

                            CategoryDropdown(
                                    selectedItem = selectedCategory,
                                    onItemSelected = { 
                                        selectedCategory = it
                                        addEditExpenseViewModel.onAction(ExpenseEvent.CategoryChanged(it))
                                    },
                                    items = categories,
                                    label = "Category",
                                    itemToString = { it },
                            )

                            InputField(
                                    onValueChanged = {
                                        addEditExpenseViewModel.onAction(
                                                ExpenseEvent.AmountChanged(it)
                                        )
                                    },
                                    label = "Amount",
                                    isError = addEditExpenseViewModel.errorState.value.amountStatus,
                                    error = "Please enter a valid amount",
                                    value = editableExpense?.cost?.toString() ?: "",
                            )

                            val currencies =
                                    listOf(
                                            "",
                                            Currency.getInstance("USD"),
                                            Currency.getInstance("CAD"),
                                            Currency.getInstance("EUR"),
                                            Currency.getInstance("GBP"),
                                            Currency.getInstance("CNY"),
                                            Currency.getInstance("JPY"),
                                    )

                            var selectedCurrency by remember {
                                mutableStateOf(
                                    editableExpense?.currency?.let { Currency.getInstance(it) } ?: currencies[0]
                                )
                            }

                            CategoryDropdown(
                                    selectedItem = selectedCurrency,
                                    onItemSelected = { 
                                        selectedCurrency = it
                                        addEditExpenseViewModel.onAction(ExpenseEvent.CurrencyChanged(it.toString()))
                                    },
                                    items = currencies,
                                    label = "Currency",
                                    itemToString = {
                                        if (it is Currency) it.currencyCode else it.toString()
                                    } // Convert Currency to its code or use the string itself
                            )
                            Column() {
                                InputField(
                                    onValueChanged = {
                                        addressQuery = it
                                        addLocationViewModel.fetchAutocompletePredictions(it)
                                        showDropdown = it.isNotEmpty()
                                    },
                                    label = "Address",
                                    value = addressQuery
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
                                                    addressQuery =
                                                            prediction
                                                                    .getPrimaryText(null)
                                                                    .toString()
                                                    showDropdown = false
                                                    addLocationViewModel.fetchPlace(
                                                            prediction.placeId
                                                    )
                                                    addEditExpenseViewModel.onAction(
                                                            ExpenseEvent.PlaceIdSubmitted(
                                                                    prediction.placeId
                                                            )
                                                    )
                                                },
                                                text = {
                                                    Text(prediction.getPrimaryText(null).toString())
                                                }
                                        )
                                    }
                                }
                            }

                            DatePickerField(
                                label = "Date",
                                selectedDate = expenseState.date,
                                onDateSelected = {
                                    addEditExpenseViewModel.onAction(ExpenseEvent.DateChanged(it))
                                }
                            )

                            OutlinedButton(
                                    onClick = {
                                        user?.let { user ->
                                            if (editMode && expenseId != null) {
                                                addEditExpenseViewModel.updateExpense(
                                                        user.uid,
                                                        tripId,
                                                        expenseId
                                                )
                                            } else {
                                                addEditExpenseViewModel.submitExpense(
                                                        user.uid,
                                                        tripId
                                                )
                                            }
                                        }
                                    },
                                    modifier =
                                            Modifier.fillMaxWidth()
                                                    .padding(
                                                            start = 50.dp,
                                                            top = 10.dp,
                                                            end = 50.dp
                                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = TripWiseGray,
                                            contentColor = Color.White
                                    )
                            ) { 
                                Text(if (editMode) "Update Expense" else "Submit Expense") 
                            }

                            if (editMode && expenseId != null) {
                                OutlinedButton(
                                        onClick = {
                                            user?.let {
                                                addEditExpenseViewModel.deleteExpense(
                                                        it.uid,
                                                        tripId,
                                                        expenseId
                                                )
                                            }
                                        },
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .padding(
                                                                start = 50.dp,
                                                                top = 10.dp,
                                                                end = 50.dp
                                                        ),
                                        colors =
                                                ButtonDefaults.outlinedButtonColors(
                                                        contentColor =
                                                                MaterialTheme.colorScheme.error
                                                )
                                ) { Text("Delete") }
                            }
                        }
                    }
                }
            }
    )
}
