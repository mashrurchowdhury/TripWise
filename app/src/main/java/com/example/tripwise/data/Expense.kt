package com.example.tripwise.data

data class Expense(
    val id: String = "",
    val name: String = "",
    val currency: String = "", //TODO: change to Currency eventually
    val cost: Double = 0.0,
    val date: String = "",
)



