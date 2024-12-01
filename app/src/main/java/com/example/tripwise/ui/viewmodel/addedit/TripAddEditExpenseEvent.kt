package com.example.tripwise.ui.viewmodel.addedit

import java.util.Currency
import com.google.type.LatLng

sealed class ExpenseEvent {
    data class NameChanged(val name: String) : ExpenseEvent()
    data class AmountChanged(val amount: String) : ExpenseEvent()  // Amount as a string for now, converting later
    data class CurrencyChanged(val currency: Currency) : ExpenseEvent()
    data class DateChanged(val date: String) : ExpenseEvent()      // Assuming a string for date input
    data class CategoryChanged(val category: String) : ExpenseEvent()
    data class LocationSubmitted(val location: Pair<Double, Double>): ExpenseEvent()
    data class PlaceIdSubmitted(val placeId: String) : ExpenseEvent()
    data object Submit : ExpenseEvent()                                 // For submitting the form
}