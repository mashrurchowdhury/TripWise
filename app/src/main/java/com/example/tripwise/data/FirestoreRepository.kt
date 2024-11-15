package com.example.tripwise.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth

data class Settings(
    val name: String = "",
    val homeCurrency: String = ""
)

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    //EXPENSE RELATED FUNCTIONS

    fun generateExpenseId(uid: String, tripId: String): String {
        return db.collection("users")
            .document(uid)
            .collection("trips")
            .document(tripId)
            .collection("expenses")
            .document().id
    }

    suspend fun addExpense(uid: String, tripId: String, expense: Expense): Task<Void> {
        val expenseRef = db.collection("users").document(uid)
            .collection("trips").document(tripId)
            .collection("expenses").document(expense.id)
        val expenseSnapshot = expenseRef.get().await()
        if (expenseSnapshot.exists()) {
            Log.d("FirestoreRepository", "Updating expense ${expense.id}")
        } else {
            Log.d("FirestoreRepository", "Adding expense ${expense.id}")
        }
        return expenseRef.set(expense, SetOptions.merge())
    }

    suspend fun getExpenses(uid: String, tripId: String): List<Expense> {

        val expenses = mutableListOf<Expense>()
        val expenseCollection = db.collection("users").document(uid)
            .collection("trips").document(tripId)
            .collection("expenses")
        val expenseSnapshots = expenseCollection.get().await()
        for (document in expenseSnapshots.documents) {
            Log.d("FirestoreRepository", "Expense document data: ${document.data}")
            document.toObject(Expense::class.java)?.let { expenses.add(it) }
        }
        return expenses
    }


    suspend fun getExpense(uid: String, tripId: String, expenseId: String): Expense? {
        val expenseRef = db.collection("users").document(uid).collection("trips").document(tripId).collection("expenses").document(expenseId)
        return expenseRef.get().await()?.toObject(Expense::class.java)
    }

    suspend fun deleteExpense(uid: String, tripId: String, expenseId: String): Boolean {
        val expenseRef = db.collection("users").document(uid).collection("trips").document(tripId).collection("expenses").document(expenseId)
        val expenseSnapshot = expenseRef.get().await()
        return if (expenseSnapshot.exists()) {
            expenseRef.delete().await()
            true // Expense deleted successfully
        } else {
            false // Expense does not exist
        }
    }


    //TRIP RELATED FUNCTIONS

    fun generateTripId(uid: String): String {
        return db.collection("users").document(uid).collection("trips").document().id
    }

    suspend fun addTrip(uid: String, trip: Trip): Task<Void> {
        val tripRef = db.collection("users").document(uid).collection("trips").document(trip.id)
        val tripSnapshot = tripRef.get().await()
        if (tripSnapshot.exists()) {
            Log.d("FirestoreRepository", "Updating trip $trip.id")
        } else {
            Log.d("FirestoreRepository", "Adding trip $trip.id")
        }
        return tripRef.set(trip, SetOptions.merge())
    }

    suspend fun getTrips(uid: String): List<Trip> {
        val trips = mutableListOf<Trip>()
        val tripCollection = db.collection("users").document(uid).collection("trips")
        val tripSnapshots = tripCollection.get().await()
        for (document in tripSnapshots.documents) {
            document.toObject(Trip::class.java)?.let { trips.add(it) }
        }
        return trips
    }

    suspend fun getTrip(uid: String, tripId: String): Trip? {
        val tripRef = db.collection("users").document(uid).collection("trips").document(tripId)
        return tripRef.get().await()?.toObject(Trip::class.java)
    }

    suspend fun deleteTrip(uid: String, tripId: String): Boolean {
        val tripRef = db.collection("users").document(uid).collection("trips").document(tripId)
        val tripSnapshot = tripRef.get().await()
        return if (tripSnapshot.exists()) {
            tripRef.delete().await()
            true // Trip deleted successfully
        } else {
            false // Trip does not exist
        }
    }

    // SETTINGS RELATED FUNCTIONS

    suspend fun updateUserSettings(settings: Settings) {
        val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val settingsRef = db.collection("users").document(uid)
        settingsRef.set(settings, SetOptions.merge()).await()
    }

    suspend fun getUserSettings(): Settings? {
        val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")
        val settingsRef = db.collection("users").document(uid)
        return settingsRef.get().await()?.toObject(Settings::class.java)
    }
}