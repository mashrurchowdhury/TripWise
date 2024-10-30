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
import com.example.tripwise.ui.viewmodel.addedit.RegistrationEvent
import com.example.tripwise.ui.common.InputField
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.data.Trip

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
    var editableTrip by remember { mutableStateOf<Trip?>(null) }

    LaunchedEffect(user) {
        user?.let {
            try {
                if (editMode && tripId != null) {
                    editableTrip = firestoreRepository.getTrip(it.uid, tripId)
                    editableTrip?.let { trip ->
                        addEditTripViewModel.onPrefill(trip)
                    }
                    Log.d("AddEditScreen", "Fetched trip: $editableTrip")
                }
            } catch (e: Exception) {
                Log.e("DashboardScreen", "Error fetching trip", e)
            }
        }
    }

    LaunchedEffect(context) {
        addEditTripViewModel.validationEvent.collect { event ->
            when (event) {
                is ValidationState.Success -> {
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
                    if (editMode && editableTrip == null) {
                        CircularProgressIndicator(modifier = modifier)
                    } else {
                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.NameChanged(it)) },
                            label = "Name",
                            value = editableTrip?.name ?: "",
                            isError = addEditTripViewModel.errorState.value.nameStatus,
                            error = "Please enter a non-empty name"
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.CityChanged(it)) },
                            label = "City",
                            value = editableTrip?.city ?: "",
                            isError = addEditTripViewModel.errorState.value.cityStatus,
                            error = "Please enter a valid city"
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.DescriptionChanged(it)) },
                            label = "Description",
                            value = editableTrip?.description ?: "",
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.BudgetChanged(it)) },
                            label = "Budget",
                            value = if ((editableTrip?.budget ?: 0) > 0) editableTrip?.budget.toString() else "",
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.StartDateChanged(it)) },
                            label = "Start Date (YYYY-MM-DD)",
                            value = editableTrip?.startDate ?: "",
                            isError = addEditTripViewModel.errorState.value.datesStatus,
                            error = "Please enter valid dates"
                        )

                        InputField(
                            onValueChanged = { addEditTripViewModel.onAction(RegistrationEvent.EndDateChanged(it)) },
                            label = "End Date (YYYY-MM-DD)",
                            value = editableTrip?.endDate ?: "",
                            isError = addEditTripViewModel.errorState.value.datesStatus,
                            error = "Please enter valid dates"
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
