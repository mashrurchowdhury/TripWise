package com.example.tripwise.ui.presentation

/**
 * Events that can be sent to the MapScreen
 */
sealed class MapScreenEvent {
    data object OnZoomAll: MapScreenEvent()
    data class ShowErrorMessage(val message: String): MapScreenEvent()
}