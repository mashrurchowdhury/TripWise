package com.example.tripwise

import TripWiseApp
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.tripwise.ui.theme.TripwiseTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val navigateToDashboard = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {
            TripwiseTheme {
                val shouldNavigateToDashboard = remember { navigateToDashboard }
                TripWiseApp(
                    onGoogleSignInClicked = { signIn() },
                    shouldNavigateToDashboard = shouldNavigateToDashboard
                )
            }
        }
    }

    private fun signIn() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("MainActivity", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in success; print the user's name
                    val user = auth.currentUser
                    Log.d("MainActivity", "User's name: ${user?.displayName}")

                    if (user != null && user.email != null) {
                        // Get Firestore instance
                        val db = FirebaseFirestore.getInstance()

                        // Create a map of user data
                        val userData = hashMapOf(
                            "name" to user.displayName,
                            "email" to user.email
                        )

                        db.collection("users").document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Log.d("MainActivity", "User added to Firestore with UID: ${user.uid}")
                                // Trigger navigation to dashboard
                                navigateToDashboard.value = true
                            }
                            .addOnFailureListener { e ->
                                Log.w("MainActivity", "Error adding user to Firestore", e)
                            }
                    } else {
                        Log.w("MainActivity", "signInWithCredential:failure", task.exception)
                    }
                }
            }
    }
}