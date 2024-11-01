package com.example.tripwise.ui.viewmodel.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object FormValidator {
    fun validateName(name: String): Boolean {
        return name.isNotEmpty()
    }

    fun validateCity(city: String): Boolean {
        // TODO: Include check for city validity
        return city.isNotEmpty()
    }

    fun validateBudget(budget: Int): Boolean {
        return budget > 0
    }

    // Expense-specific validations
    fun validateAmount(amount: Double): Boolean {
        return amount > 0
    }

    fun validateCurrency(currency: String): Boolean {
        return currency.isNotEmpty() && currency.length == 3  // Example: check if it's a 3-letter currency code
    }

    fun validateDates(startDate: String, endDate: String): Boolean {
        //        try {
//            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//            val localStartDate = LocalDate.parse(startDate, formatter)
//            val localEndDate = LocalDate.parse(endDate, formatter)
//
//            return !formatter.format(localStartDate).equals(startDate)
//                    && !formatter.format(localEndDate).equals(endDate)
//                    && localStartDate.isBefore(localEndDate)
//        } catch (e: Exception) {
//            return false
//        }

//        TODO: Implement proper date validation - LocalDate throws errors
        return true
    }
}

data class FormValidationResult(
    val nameStatus: Boolean = false,
    val cityStatus: Boolean = false,
    val budgetStatus: Boolean = false,
    val datesStatus: Boolean = false,
    val amountStatus: Boolean = false,
    val currencyStatus: Boolean = false,
)