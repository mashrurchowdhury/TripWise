package com.example.tripwise.ui.screens

import android.util.Log
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.graphics.Color
import com.example.tripwise.ui.viewmodel.addedit.RegistrationEvent
import com.example.tripwise.ui.common.InputField
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.ui.components.DatePickerField
import com.example.tripwise.ui.theme.TripWiseGray
import com.example.tripwise.ui.theme.TripWiseGreen

@Composable
fun AddEditScreen(
    addEditTripViewModel: AddEditTripViewModel = hiltViewModel(), 
    modifier: Modifier,
    editMode: Boolean,
    tripId: String? = null,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    val context = LocalContext.current
    val firestoreRepository = FirestoreRepository()

    val user = FirebaseAuth.getInstance().currentUser
    val tripState by addEditTripViewModel._tripState


    LaunchedEffect(user) {
        user?.let {
            try {
                if (editMode && tripId != null) {
                    val fetchedTrip = firestoreRepository.getTrip(it.uid, tripId)
                    fetchedTrip?.let { trip ->
                        addEditTripViewModel.onPrefill(trip)
                    }
                    Log.d("AddEditScreen", "Fetched trip: $fetchedTrip")
                }
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching trip", e)
            }
        }
    }

    LaunchedEffect(context) {
        addEditTripViewModel.validationEvent.collect { event ->
            when (event) {
                is ValidationState.TripSuccess -> {
                    val trip = event.trip
                    Log.d("AddEditScreen", "Registered Trip is $trip")
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()

                    onSubmit()
                }
                else -> {}
            }
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
                    if (editMode && tripState.id.isEmpty()) {
                        CircularProgressIndicator(modifier = modifier)
                    } else {
                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.NameChanged(it)) },
                            label = "Name *",
                            value = tripState.name
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.DescriptionChanged(it)) },
                            label = "Description",
                            value = tripState.description,
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.BudgetChanged(it)) },
                            label = "Budget *",
                            value = if (tripState.budget > 0) tripState.budget.toString() else ""
                        )

                        DatePickerField(
                            label = "Start Date (YYYY-MM-DD) *",
                            selectedDate = tripState.startDate,
                            onDateSelected = { selectedDate ->
                                addEditTripViewModel.onAction(RegistrationEvent.StartDateChanged(selectedDate))
                            }
                        )

                        // End Date Picker
                        DatePickerField(
                            label = "End Date (YYYY-MM-DD) *",
                            selectedDate = tripState.endDate,
                            onDateSelected = { selectedDate ->
                                addEditTripViewModel.onAction(RegistrationEvent.EndDateChanged(selectedDate))
                            }
                        )

                        OutlinedButton(
                            onClick = {
                                user?.let { user ->
                                    if (editMode && tripId != null) {
                                        addEditTripViewModel.updateTrip(user.uid, tripId)
                                    } else {
                                        addEditTripViewModel.submitTrip(user.uid)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 50.dp, top = 10.dp, end = 50.dp),
                            enabled = !addEditTripViewModel.hasError,
                            colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = TripWiseGray,
                                    contentColor = Color.White
                            )
                        ) {
                            if (editMode) Text("Update") else Text("Submit")
                        }

                        if (editMode && tripId != null) {
                            OutlinedButton(
                                onClick = {
                                    user?.let {
                                        addEditTripViewModel.deleteTrip(it.uid, tripId)
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
