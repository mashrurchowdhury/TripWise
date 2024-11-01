package com.example.tripwise.data

data class Expense(
    val expenseId: Long = 0, //TODO: Change
    val tripId: Long = 0, //TODO: Change
    val name: String = "",
    val currency: String, //TODO: change to Currency eventually
    val cost: Double = 0.0,
    val date: String = "",
)



