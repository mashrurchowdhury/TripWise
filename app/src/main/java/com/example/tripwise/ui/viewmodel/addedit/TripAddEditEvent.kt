package com.example.tripwise.ui.viewmodel.addedit

sealed class RegistrationEvent  {
    data class NameChanged(val name: String): RegistrationEvent()
    data class DescriptionChanged(val description: String): RegistrationEvent()
    data class BudgetChanged(val budget: String): RegistrationEvent()
    data class StartDateChanged(val startDate: String): RegistrationEvent()
    data class EndDateChanged(val endDate: String): RegistrationEvent()
    object Submit: RegistrationEvent()
}