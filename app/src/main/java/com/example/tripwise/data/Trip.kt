package com.example.tripwise.data

import java.time.LocalDate

/**
 * A simple data class to represent a Trip.
 */
data class Trip(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val description: String = "",
    val budget: Double = 0.0,
    val startDate: String = "",
    val endDate: String = "",
)