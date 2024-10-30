package com.example.tripwise.data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

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
}