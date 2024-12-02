package com.example.tripwise.ui.viewmodel.addedit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripwise.data.CurrencyConverter
import com.example.tripwise.data.Expense
import com.example.tripwise.data.FirestoreRepository
import com.google.type.LatLng
import com.google.android.play.integrity.internal.c
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.text.Normalizer.Form
import javax.inject.Inject
import java.util.Currency

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val currencyConverter: CurrencyConverter 
) : ViewModel() {
    var _expenseState = mutableStateOf(Expense())
    private var _errorState = mutableStateOf(FormValidationResult())
    val errorState: State<FormValidationResult> = _errorState
    val validationEvent = MutableSharedFlow<ValidationState>()

    fun onAction(expenseEvent: ExpenseEvent) {
        when (expenseEvent) {
            is ExpenseEvent.NameChanged -> {
                _expenseState.value = _expenseState.value.copy(
                    name = expenseEvent.name
                )
            }

            is ExpenseEvent.AmountChanged -> {
                if (expenseEvent.amount.toDoubleOrNull() != null) {
                    _expenseState.value = _expenseState.value.copy(
                        cost = expenseEvent.amount.toDouble()
                    )
                }
            }
            is ExpenseEvent.CategoryChanged -> {
                _expenseState.value = _expenseState.value.copy(
                    category = expenseEvent.category
                )
            }

            is ExpenseEvent.CurrencyChanged -> {
                _expenseState.value = _expenseState.value.copy(
                    currency = expenseEvent.currency
                )
            }

            is ExpenseEvent.DateChanged -> {
                _expenseState.value = _expenseState.value.copy(
                    date = expenseEvent.date
                )
            }

            is ExpenseEvent.LocationSubmitted -> {
                _expenseState.value = _expenseState.value.copy(
                    lat = expenseEvent.location.first,
                    lng = expenseEvent.location.second
                )
            }

            is ExpenseEvent.PlaceIdSubmitted -> {
                _expenseState.value = _expenseState.value.copy(
                    placeId = expenseEvent.placeId
                )
            }

            is ExpenseEvent.Submit -> {
                validateExpense()
            }
        }
    }

    fun onPrefill(expense: Expense) {
        _expenseState.value = expense
    }

    var hasError: Boolean = true

    private fun validateExpense() {
        val isNameValid = FormValidator.validateName(_expenseState.value.name)
        val isAmountValid = FormValidator.validateAmount(_expenseState.value.cost)
        val isCategoryValid = _expenseState.value.category.isNotEmpty()
        val isCurrencyValid = FormValidator.validateCurrency(_expenseState.value.currency)
        val isLocationValid = FormValidator.validateLocation(_expenseState.value.lat, _expenseState.value.lng)

        _errorState.value = _errorState.value.copy(
            nameStatus = !isNameValid,
            amountStatus = !isAmountValid,
            currencyStatus = !isCurrencyValid,
            categoryStatus = !isCategoryValid,
            locationStatus = !isLocationValid
        )

        hasError = (
                !_errorState.value.nameStatus
                        && !_errorState.value.amountStatus
                        && !_errorState.value.currencyStatus
                        && !_errorState.value.categoryStatus
                        && !_errorState.value.datesStatus
                        && !_errorState.value.locationStatus
                )
    }

    fun submitExpense(uid: String, tripId: String) {
        viewModelScope.launch {
            try {
                // Fetch user settings
                val settings = firestoreRepository.getUserSettings()
                val homeCurrency = settings?.homeCurrency ?: "USD" // Default to USD if not set

                // Convert currency
                val convertedAmount = "%.2f".format(
                    currencyConverter.convertCurrency(
                        _expenseState.value.cost,
                        _expenseState.value.currency,
                        homeCurrency
                    )
                ).toDouble()

                // Create expense with converted cost
                val expense = _expenseState.value.copy(
                    id = firestoreRepository.generateExpenseId(uid, tripId),
                    convertedCost = convertedAmount
                )

                // Add expense to Firestore
                firestoreRepository.addExpense(uid, tripId, expense)

                // Emit success with expense
                validationEvent.emit(ValidationState.ExpenseSuccess(expense))
            } catch (e: Exception) {
                Log.e("submitExpense", "Error submitting expense", e)
            }
        }
    }

    fun updateExpense(uid: String, tripId: String, expenseId: String) {
        viewModelScope.launch {
            try {
                // Fetch user settings
                val settings = firestoreRepository.getUserSettings()
                val homeCurrency = settings?.homeCurrency ?: "USD" // Default to USD if not set

                // Convert currency
                val convertedAmount = "%.2f".format(
                    currencyConverter.convertCurrency(
                        _expenseState.value.cost,
                        _expenseState.value.currency,
                        homeCurrency
                    )
                ).toDouble()
                
                // Create expense with converted cost
                val expense = _expenseState.value.copy(
                    id = expenseId,
                    convertedCost = convertedAmount
                )

                firestoreRepository.addExpense(uid, tripId, expense)
                validationEvent.emit(ValidationState.ExpenseSuccess(expense))
            } catch (e: Exception) {
                Log.e("updateExpense", "Error updating expense", e)
            }
        }
    }

    fun deleteExpense(uid: String, tripId: String, expenseId: String) {
        viewModelScope.launch {
            val success = firestoreRepository.deleteExpense(uid, tripId, expenseId)
            if (success) {
                validationEvent.emit(ValidationState.ExpenseSuccess(Expense()))
                Log.d("AddEditExpenseViewModel", "Expense deleted successfully")
            } else {
                Log.w("AddEditExpenseViewModel", "Could not delete expense from Firestore")
            }
        }
    }
}