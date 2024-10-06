package com.example.tripwise.data

import java.time.LocalDate

/**
 * A simple data class to represent an Email.
 */
data class Trip(
    val id: Long,
    val name: String,
    val people: List<String>,
    val description: String,
//    TODO: change dates to use LocalDate objects or similar
    val startDate: String,
    val endDate: String,
)