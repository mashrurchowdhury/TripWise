package com.example.tripwise.ui.viewmodel.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.libraries.mapsplatform.transportation.consumer.model.Location
import com.google.type.LatLng
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object FormValidator {
    fun validateName(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun validateBudget(budget: Double): Boolean {
        return budget > 0
    }

    fun validateAmount(amount: Double): Boolean {
        return amount > 0
    }

    fun validateCurrency(currency: String): Boolean {
        return currency.isNotEmpty() && currency.length == 3
    }

    fun validateLocation(lat: Double, lng: Double): Boolean {
        // Not default LatLng, which is set at Null Island (middle of ocean)
        return lat != 0.0 && lng != 0.0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun validateDates(startDate: String, endDate: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)
            !end.isBefore(start)
        } catch (e: DateTimeParseException) {
            false
        }
    }
}

data class FormValidationResult(
    val nameStatus: Boolean = false,
    val budgetStatus: Boolean = false,
    val datesStatus: Boolean = false,
    val amountStatus: Boolean = false,
    val currencyStatus: Boolean = false,
    val categoryStatus: Boolean = false,
    val locationStatus: Boolean = false,
)