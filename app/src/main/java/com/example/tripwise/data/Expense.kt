package com.example.tripwise.data

data class Expense(
    val expenseId: Long,
    val tripId: Long,
    val name: String,
    val cost: Double,
    val currency: String,
    val date: String,
)


