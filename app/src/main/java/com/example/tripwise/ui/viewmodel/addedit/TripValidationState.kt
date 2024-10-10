package com.example.tripwise.ui.viewmodel.addedit

import com.example.tripwise.data.Trip

sealed class ValidationState {
    object Loading: ValidationState()
    data class Success(val trip: Trip) : ValidationState()
}