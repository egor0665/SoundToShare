package com.example.soundtoshare.databases

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class FirestoreDatabase {
    private val database = Firebase.firestore
    var users = mutableListOf<User>()

    fun updateUserInformation(latitude: Double, longitude: Double, vkAccount: String) {
//        , song: String, artist: String
        val user = hashMapOf(
            "VKAccount" to vkAccount,
            "geoPoint" to GeoPoint(latitude, longitude),
            "geoHash" to GeoFireUtils.getGeoHashForLocation(GeoLocation(latitude, longitude)),
//            "currentSong" to song,
//            "currentArtist" to artist
        )

        database.collection("Users").document(vkAccount)
            .set(user)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added  ")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    fun getClosest(latitude: Double, longitude: Double) {

        val center = GeoLocation(latitude, longitude)
        // Радиус поиска в метрах
        val radiusInM = 100 * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        for (b in bounds) {
            val q = database.collection("Users")
                .orderBy("geoHash")
                .startAt(b.startHash)
                .endAt(b.endHash)
            tasks.add(q.get())
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener {
                val matchingDocs: MutableList<DocumentSnapshot> = ArrayList()
                for (task in tasks) {
                    val snap = task.result
                    for (doc in snap!!.documents) {
                        val geoPoint = doc.getGeoPoint("geoPoint")!!
                        val lat = geoPoint.latitude
                        val lng = geoPoint.longitude

                        // We have to filter out a few false positives due to GeoHash
                        // accuracy, but most will match
                        val docLocation = GeoLocation(lat, lng)
                        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                        if (distanceInM <= radiusInM) {
                            matchingDocs.add(doc)
                        }
                    }
                }

                // matchingDocs contains the results

                matchingDocs.forEach {
                    users.add(
                        User(
                            it.getField<GeoPoint>("geoPoint")!!,
                            it.getField<String>("VKAccount").toString()
                        )
                    )
                    Log.d(
                        "Firestore",
                        it.getField<GeoPoint>("geoPoint")!!
                            .toString() + it.getField<String>("VKAccount").toString()
                    )
                }
            }
    }
}

data class User(val geoPoint: GeoPoint, val VKAccount: String)
