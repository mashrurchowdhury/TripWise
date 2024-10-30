package com.example.tripwise.ui.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
): ViewModel() {
    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _state.asStateFlow()

    init {
        _state.value = LoginState.Initial
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    private fun updateUserInformation(name: String): Task<Void> {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        return getCurrentUser()!!.updateProfile(profileUpdates)
    }

    private fun addUserToFirestore(user: FirebaseUser): Task<Void> {
        val userData = hashMapOf(
            "name" to user.displayName,
            "email" to user.email
        )

        return firestore.collection("users").document(user.uid).set(userData)
    }

    // Firebase authentication with Google
    fun signInWithGoogle(idToken: String) {
        // Prevent re-trigger of auth if already in progress
        if (_state.value is LoginState.Loading) {
            return
        }
        _state.value = LoginState.Loading

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("SignInViewModel", "User's name: ${user?.displayName}")

                user?.let { u ->
                    addUserToFirestore(u).addOnSuccessListener {
                        Log.d("SignInViewModel", "User added to Firestore with UID: ${user.uid}")
                        _state.value = LoginState.LoginSuccess
                    }.addOnFailureListener { e ->
                        Log.w("SignInViewModel", "Error adding user to Firestore", e)
                        _state.value = LoginState.LoginError(e.message.toString())
                    }
                }
            } else {
                Log.w("SignInViewModel", "signInWithCredential:failure", task.exception)
                _state.value = LoginState.LoginError(task.exception?.message.toString())
            }
        }
    }

    // Firebase account creation with Email and Password
    fun createAccountWithEmail(email: String, password: String, name: String) {
        // Prevent re-trigger of auth if already in progress
        if (_state.value is LoginState.Loading) {
            return
        }
        _state.value = LoginState.Loading

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            try {
                auth.currentUser?.let { user ->
                    Log.d("SignInViewModel", "User created account through email user UID: ${user.uid}")

                    updateUserInformation(name)
                        .addOnFailureListener { e ->
                            Log.d(
                                "SignInViewModel",
                                "Could not update name for created account with email user UID: ${user.uid}"
                            )
                            _state.value = LoginState.CreateAccountError(e.message.toString())
                        }.addOnSuccessListener {
                            addUserToFirestore(user).addOnSuccessListener {
                                Log.d("SignInViewModel", "New user added to Firestore with UID: ${user.uid}")
                                _state.value = LoginState.CreateAccountSuccess
                            }.addOnFailureListener { e ->
                                Log.w("SignInViewModel", "Error adding new user to Firestore", e)
                                _state.value = LoginState.CreateAccountError(e.message.toString())
                            }
                        }
                }
            } catch (e: ApiException) {
                Log.w("SignInViewModel", "createAccountWithEmail:failure", e)
                _state.value = LoginState.CreateAccountError(e.message.toString())
            }
        }.addOnFailureListener { e ->
            Log.w("SignInViewModel", "Error creating account through email", e)
            _state.value = LoginState.CreateAccountError(e.message.toString())
        }
    }

    // Firebase authentication with Email and Password
    fun signInWithEmail(email: String, password: String) {
        // Prevent re-trigger of auth if already in progress
        if (_state.value is LoginState.Loading) {
            return
        }
        _state.value = LoginState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    Log.d("SignInViewModel", "User signed in through email with UID: ${user.uid}")
                    _state.value = LoginState.LoginSuccess
                } else {
                    Log.w("SignInViewModel", "signInWithEmail:failure", task.exception)
                    _state.value = LoginState.LoginError(task.exception?.message.toString())
                }
            }
    }

    fun signOut() {
        // Prevent re-trigger of auth if already in progress
        if (_state.value is LoginState.Loading) {
            return
        }
        _state.value = LoginState.Loading

        viewModelScope.launch {
            try {
                auth.signOut()
                _state.value = LoginState.LoggedOut
            } catch (e: Exception) {
                Log.w("SignInViewModel", "Sign out failed: ${e.message}")
                _state.value = LoginState.LoggedOutError(e.message.toString())
            }
        }
    }
}