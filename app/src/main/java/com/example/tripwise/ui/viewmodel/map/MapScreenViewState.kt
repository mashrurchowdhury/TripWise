package com.example.tripwise.ui.viewmodel.map

import com.example.tripwise.data.Expense
import com.example.tripwise.ui.viewmodel.auth.LoginState
import com.google.android.gms.maps.model.LatLngBounds

sealed class MapScreenViewState {
    data object Loading : MapScreenViewState()
    data class Success(val expenses: List<Expense>) : MapScreenViewState()
    data class Error(val message: String) : MapScreenViewState()
}