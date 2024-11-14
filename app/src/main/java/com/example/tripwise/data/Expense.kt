package com.example.tripwise.data
import java.util.Currency

data class Expense(
    val expenseId: Long = 0, //TODO: Change
    val tripId: Long = 0, //TODO: Change
    val name: String = "",
    val category: String = "",
    val currency: Currency = Currency.getInstance("CAD"),
    val cost: Double = 0.0,
    val date: String = "",
    val latitude: Double? = null,    // Nullable, as it might not be set initially
    val longitude: Double? = null
)



