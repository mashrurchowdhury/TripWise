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
)