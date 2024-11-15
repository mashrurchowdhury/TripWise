package com.example.tripwise.ui.viewmodel.addedit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwise.data.FirestoreRepository
import com.example.tripwise.data.Trip
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AddEditTripViewModel
@Inject
constructor(private val firestoreRepository: FirestoreRepository) : ViewModel() {
    var _tripState = mutableStateOf(Trip())

    private var _errorState = mutableStateOf(FormValidationResult())
    val errorState: State<FormValidationResult> = _errorState

    val validationEvent = MutableSharedFlow<ValidationState>()

    fun onAction(registrationEvent: RegistrationEvent) {
        when (registrationEvent) {
            is RegistrationEvent.NameChanged -> {
                _tripState.value = _tripState.value.copy(name = registrationEvent.name)
            }

            is RegistrationEvent.DescriptionChanged -> {
                _tripState.value =
                        _tripState.value.copy(description = registrationEvent.description)
            }

            is RegistrationEvent.BudgetChanged -> {
                if (registrationEvent.budget.toDoubleOrNull() != null) {
                    _tripState.value =
                        _tripState.value.copy(budget = registrationEvent.budget.toDouble())
                }
            }

            is RegistrationEvent.StartDateChanged -> {
                _tripState.value = _tripState.value.copy(startDate = registrationEvent.startDate)
            }

            is RegistrationEvent.EndDateChanged -> {
                _tripState.value = _tripState.value.copy(endDate = registrationEvent.endDate)
            }

            is RegistrationEvent.Submit -> {
                validateTripRegistration()
            }
        }
        validateTripRegistration()
    }

    var hasError: Boolean = true

    private fun validateTripRegistration() {
        val isNameValid = FormValidator.validateName(_tripState.value.name)
        val isBudgetValid = FormValidator.validateBudget(_tripState.value.budget)
        val isDatesValid =
                FormValidator.validateDates(_tripState.value.startDate, _tripState.value.endDate)

        _errorState.value =
                _errorState.value.copy(
                        nameStatus = !isNameValid,
                        budgetStatus = !isBudgetValid,
                        datesStatus = !isDatesValid
                )

        hasError =
                _errorState.value.nameStatus ||
                        _errorState.value.budgetStatus ||
                        _errorState.value.datesStatus

        Log.d("AddEditTripViewModel", "hasError: $hasError")
        Log.d("AddEditTripViewModel", "errorState: ${_errorState.value}")
    }

    fun onPrefill(trip: Trip) {
        _tripState.value = trip
        validateTripRegistration()
    }

    fun submitTrip(uid: String) {
        viewModelScope.launch {
            val trip = _tripState.value.copy(id = firestoreRepository.generateTripId(uid))
            firestoreRepository.addTrip(uid, trip)
            validationEvent.emit(ValidationState.TripSuccess(trip))
        }
    }

    fun updateTrip(uid: String, tripId: String) {
        viewModelScope.launch {
            val trip = _tripState.value.copy(id = tripId)
            firestoreRepository.addTrip(uid, trip)
            validationEvent.emit(ValidationState.TripSuccess(trip))
        }
    }

    fun deleteTrip(uid: String, tripId: String) {
        viewModelScope.launch {
            val success = firestoreRepository.deleteTrip(uid, tripId)
            if (success) {
                validationEvent.emit(ValidationState.TripSuccess(Trip()))
                Log.d("AddEditTripViewModel", "Trip deleted successfully")
            } else {
                Log.w("AddEditTripViewModel", "Could not delete trip from Firestore")
            }
        }
    }
}
