package com.example.tripwise.ui.viewmodel.addedit

import android.os.Build
import androidx.annotation.RequiresApi
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
)