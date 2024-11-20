package com.example.tripwise.data
import java.util.Currency

data class Expense(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val currency: String = "", //TODO: change to Currency eventually
    val cost: Double = 0.0,
    val convertedCost: Double? = null,
    val date: String = "",
    val latitude: Double? = null,    // Nullable, as it might not be set initially
    val longitude: Double? = null
)



