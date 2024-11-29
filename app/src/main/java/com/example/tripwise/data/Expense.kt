package com.example.tripwise.data

data class Expense(
    val id: String = "",
    val name: String = "",
    val currency: String = "", //TODO: change to Currency eventually
    val cost: Double = 0.0,
    val convertedCost: Double? = null,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val date: String = "",
)



