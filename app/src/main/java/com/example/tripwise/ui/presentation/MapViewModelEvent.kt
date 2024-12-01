package com.example.tripwise.ui.presentation

sealed class MapViewModelEvent {
    data object OnZoomAll : MapViewModelEvent()
    data object OnToggleAllExpenses : MapViewModelEvent()
}
