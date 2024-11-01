package com.example.tripwise.ui.viewmodel.addedit

import com.example.tripwise.data.Expense
import com.example.tripwise.data.Trip

sealed class ValidationState {
    object Loading: ValidationState()
    data class TripSuccess(val trip: Trip) : ValidationState()
    data class ExpenseSuccess(val expense: Expense): ValidationState()
}