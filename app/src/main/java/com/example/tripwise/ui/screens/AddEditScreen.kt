package com.example.tripwise.ui.screens

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.example.tripwise.ui.viewmodel.addedit.AddEditTripViewModel
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
import com.example.tripwise.ui.viewmodel.addedit.RegistrationEvent
import com.example.tripwise.ui.common.InputField
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddEditScreen(addEditTripViewModel: AddEditTripViewModel = hiltViewModel(), modifier: Modifier, onBackClick: () -> Unit) {
    val context = LocalContext.current
    val localFocus = LocalFocusManager.current

    LaunchedEffect(key1 = context) {
        addEditTripViewModel.validationEvent.collect { event ->
            when (event) {
                is ValidationState.Success -> {
                    val trip = event.trip
                    println("Registered Trip is $trip")
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
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
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.NameChanged(it)) },
                        label = "Name",
                        isError = addEditTripViewModel.errorState.value.nameStatus,
                        error = "Please enter a non-empty name"
                    )

                    InputField(
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.CityChanged(it)) },
                        label = "City",
                        isError = addEditTripViewModel.errorState.value.cityStatus,
                        error = "Please enter a valid city"
                    )

                    InputField(
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.DescriptionChanged(it)) },
                        label = "Description",
                    )

                    InputField(
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.BudgetChanged(it)) },
                        label = "Budget",
                    )

                    InputField(
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.StartDateChanged(it)) },
                        label = "Start Date (YYYY-MM-DD)",
                        isError = addEditTripViewModel.errorState.value.datesStatus,
                        error = "Please enter valid dates"
                    )

                    InputField(
                        onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.EndDateChanged(it)) },
                        label = "End Date (YYYY-MM-DD)",
                        isError = addEditTripViewModel.errorState.value.datesStatus,
                        error = "Please enter valid dates"
                    )

                    OutlinedButton(
                        onClick = {
                            addEditTripViewModel.onAction(RegistrationEvent.Submit)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp, top = 10.dp, end = 50.dp),
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    )
}
