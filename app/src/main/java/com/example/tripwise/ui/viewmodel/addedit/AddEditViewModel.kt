package com.example.tripwise.ui.viewmodel.addedit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwise.data.Trip
import com.example.tripwise.data.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class AddEditTripViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    private var _tripState = mutableStateOf(Trip())

    private var _errorState = mutableStateOf(FormValidationResult())
    val errorState: State<FormValidationResult> = _errorState
    val validationEvent = MutableSharedFlow<ValidationState>()

    fun onAction(registrationEvent: RegistrationEvent) {
        when (registrationEvent) {
            is RegistrationEvent.NameChanged -> {
                _tripState.value = _tripState.value.copy(
                    name = registrationEvent.name
                )
            }

            is RegistrationEvent.CityChanged -> {
                _tripState.value = _tripState.value.copy(
                    city = registrationEvent.city
                )
            }

            is RegistrationEvent.DescriptionChanged -> {
                _tripState.value = _tripState.value.copy(
                    description = registrationEvent.description
                )
            }

            is RegistrationEvent.BudgetChanged -> {
                _tripState.value = _tripState.value.copy(
                    budget = registrationEvent.budget.toInt()
                )
            }

            is RegistrationEvent.StartDateChanged -> {
                _tripState.value = _tripState.value.copy(
                    startDate = registrationEvent.startDate
                )
            }

            is RegistrationEvent.EndDateChanged -> {
                _tripState.value = _tripState.value.copy(
                    endDate = registrationEvent.endDate
                )
            }

            else -> {
                validateTripRegistration()
            }
        }
    }

    private var hasError : Boolean = false

    private fun validateTripRegistration() {
        val isNameValid = FormValidator.validateName(_tripState.value.name)
        val isCityValid = FormValidator.validateCity(_tripState.value.city)
        val isBudgetValid = FormValidator.validateBudget(_tripState.value.budget)
        val isDatesValid = FormValidator.validateDates(
            _tripState.value.startDate,
            _tripState.value.endDate
        )

        _errorState.value = _errorState.value.copy(
            nameStatus = !isNameValid
        )
        _errorState.value = _errorState.value.copy(
            cityStatus = !isCityValid
        )
        _errorState.value = _errorState.value.copy(
            budgetStatus = !isBudgetValid
        )
        _errorState.value = _errorState.value.copy(
            datesStatus = !isDatesValid
        )

        hasError = (
                !_errorState.value.nameStatus
                        && !_errorState.value.cityStatus
                        && !_errorState.value.budgetStatus
                        && !_errorState.value.datesStatus
                )

        viewModelScope.launch {
            if (hasError) {
                validationEvent.emit(ValidationState.Success(_tripState.value))
            }
        }
    }

    fun submitTrip(uid: String) {
        viewModelScope.launch {
            val trip = _tripState.value.copy(id = firestoreRepository.generateTripId(uid))
            val success = firestoreRepository.addTrip(uid, trip)
            if (success) {
                validationEvent.emit(ValidationState.Success(trip))
            } else {
                Log.w("AddEditTripViewModel", "Could not add trip to Firestore")
            }
        }
    }

    fun deleteTrip(uid: String, tripId: String) {
        viewModelScope.launch {
            val success = firestoreRepository.deleteTrip(uid, tripId)
            if (success) {
                validationEvent.emit(ValidationState.Success(Trip()))
                Log.d("AddEditTripViewModel", "Trip deleted successfully")
            } else {
                Log.w("AddEditTripViewModel", "Could not delete trip from Firestore")
            }
        }
    }
}