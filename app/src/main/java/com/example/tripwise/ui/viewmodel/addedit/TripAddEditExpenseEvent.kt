package com.example.tripwise.ui.viewmodel.addedit

sealed class ExpenseEvent {
    data class NameChanged(val name: String) : ExpenseEvent()
    data class AmountChanged(val amount: String) : ExpenseEvent()  // Amount as a string for now, converting later
    data class CurrencyChanged(val currency: String) : ExpenseEvent()
    data class DateChanged(val date: String) : ExpenseEvent()      // Assuming a string for date input
    object Submit : ExpenseEvent()                                 // For submitting the form
}