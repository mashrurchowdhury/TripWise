package com.example.tripwise.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun generateTripId(uid: String): String {
        return db.collection("users").document(uid).collection("trips").document().id
    }

    suspend fun addTrip(uid: String, trip: Trip): Boolean {
        val tripRef = db.collection("users").document(uid).collection("trips").document(trip.id)
        val tripSnapshot = tripRef.get().await()
        return if (tripSnapshot.exists()) {
            false // Trip ID already exists
        } else {
            tripRef.set(trip, SetOptions.merge()).await()
            true // Trip added successfully
        }
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
}