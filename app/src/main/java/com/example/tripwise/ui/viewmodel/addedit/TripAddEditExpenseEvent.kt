package com.example.tripwise.ui.viewmodel.addedit

import java.util.Currency
sealed class ExpenseEvent {
    data class NameChanged(val name: String) : ExpenseEvent()
    data class AmountChanged(val amount: String) : ExpenseEvent()  // Amount as a string for now, converting later
    data class CurrencyChanged(val currency: Currency) : ExpenseEvent()
    data class DateChanged(val date: String) : ExpenseEvent()      // Assuming a string for date input
    data class CategoryChanged(val category: String) : ExpenseEvent()
    object Submit : ExpenseEvent()                                 // For submitting the form
}