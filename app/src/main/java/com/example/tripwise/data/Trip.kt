package com.example.tripwise.data

import java.time.LocalDate

/**
 * A simple data class to represent a Trip.
 */
data class Trip(
    val id: Long = 0,
    val name: String = "",
    val city: String = "",
    val description: String = "",
    val budget: Int = 0,
//    TODO: change dates to use LocalDate objects or similar
    val startDate: String = "",
    val endDate: String = "",
)