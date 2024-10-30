package com.example.tripwise.ui.viewmodel.auth

sealed class LoginState {
    data object Initial : LoginState()
    data object Loading : LoginState()

    /**
     * Create account
     */
    data object CreateAccountSuccess : LoginState()
    data class CreateAccountError(val message: String) : LoginState()

    /**
     * Login to a created account
     */
    data object LoginSuccess : LoginState()
    data class LoginError(val message: String) : LoginState()

    data object LoggedOut : LoginState()
    data class LoggedOutError(val message: String) : LoginState()
}