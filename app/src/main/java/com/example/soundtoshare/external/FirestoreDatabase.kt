package com.example.soundtoshare.external

import android.util.Log
import com.example.soundtoshare.repositories.User
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.util.Date
import kotlin.collections.ArrayList

class FirestoreDatabase {
    private val database = Firebase.firestore
    private val _users = mutableListOf<User>()
    val users: List<User>
        get() = _users

    fun setUserInfo(vkAccount: String, vkId: String) {
        Log.d("Firebase", vkAccount + " " + vkId)
        val userInfo = hashMapOf(
            "VKAccount" to vkAccount,
            "vkId" to vkId
        )
        database.collection("UserInfo").document(vkId)
            .set(userInfo)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added  ")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document UserInfo", e)
            }
    }
    fun updateUserInformation(user: User) {
        val updatedUser = hashMapOf(
            "VKAccount" to user.vkAccount,
            "VKId" to user.vkAccountID,
            "geoPoint" to user.geoPoint,
            "geoHash" to GeoFireUtils.getGeoHashForLocation(
                GeoLocation(user.geoPoint.latitude, user.geoPoint.longitude)
            ),
            "currentSong" to user.song,
            "currentArtist" to user.artist,
            "lastUpdate" to Date().time,
            "avatar" to user.avatar
        )

        database.collection("Users").document(user.vkAccount)
            .set(updatedUser)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added  ")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    fun fetchClosest(
        targetDevice: LatLng,
        radiusInM: Double,
        fetchClosestCallback: List<User>.() -> Unit
    ) {
        _users.clear()
        val center = GeoLocation(targetDevice.latitude, targetDevice.longitude)

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
                    _users.add(
                        User(
                            it.getField<GeoPoint>("geoPoint")!!,
                            it.getField<String>("VKAccount").toString(),
                            song = it.getField<String>("currentSong")!!,
                            artist = it.getField<String>("currentArtist")!!,
                            vkAccountID = it.getField<String>("VKId")!!,
                            lastUpdate = it.getField<Long>("lastUpdate")!!,
                            avatar = it.getField<String>("avatar")!!
                        )
                    )
                    Log.d(
                        "Firestore",
                        it.getField<GeoPoint>("geoPoint")!!
                            .toString() + it.getField<String>("VKAccount").toString()
                    )
                }
                fetchClosestCallback(_users)
            }
    }
}
