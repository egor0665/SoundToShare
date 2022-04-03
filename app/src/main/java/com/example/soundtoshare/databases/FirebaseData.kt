package com.example.soundtoshare.databases

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.IOException

class FirebaseData {
    private val database = Firebase.database
    private val usersRef = database.getReference("Users")
    private lateinit var spotifyId: String;

    private lateinit var databaseref: DatabaseReference

    fun initializeDbRef() {
        databaseref = Firebase.database.reference
    }

    fun updateUserLocation(altitude: Double, latitude: Double, spotifyAccount: String) {
        val user = User(altitude, latitude)
        spotifyId = spotifyAccount

        // Спотифай аккаунт получать из объекта

        Log.d("Firebase", spotifyId )
        usersRef.child(spotifyId).setValue(user)

    }
    fun setupListener(){
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Firebase", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                val comment = dataSnapshot.getValue<Comment>()

                // ...
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Firebase", "onChildChanged: ${dataSnapshot.key}")

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("Firebase", "onChildRemoved:" + dataSnapshot.key!!)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("Firebase", "onChildMoved:" + dataSnapshot.key!!)

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                val movedComment = dataSnapshot.getValue<Comment>()
                val commentKey = dataSnapshot.key

                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Firebase", "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.",
                    Toast.LENGTH_SHORT).show()
            }
        }
        databaseReference.addChildEventListener(childEventListener)
    }
    fun getUserList(altitude: Double, latitude: Double){
        usersRef.get().addOnSuccessListener {
            it.children("Users").
            Log.i("firebase", "Got value ${it.value}")
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    @IgnoreExtraProperties
    data class User(val altitude: Double? = null, val latitude: Double? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

}