package com.example.soundtoshare.external

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FireBaseDatabase {
    private var database = Firebase.database.reference
    val childEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d("firebase", "onChildAdded:" + dataSnapshot.key!!)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d("firebase", "onChildChanged: ${dataSnapshot.key}")

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so displayed the changed comment.
//            val newComment = dataSnapshot.getValue<Comment>()
//            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.d("firebase", "onChildRemoved:" + dataSnapshot.key!!)

            // A comment has changed, use the key to determine if we are displaying this
            // comment and if so remove it.
//            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            Log.d("firebase", "onChildMoved:" + dataSnapshot.key!!)

            // A comment has changed position, use the key to determine if we are
            // displaying this comment and if so move it.
//            val movedComment = dataSnapshot.getValue<Comment>()
//            val commentKey = dataSnapshot.key

            // ...
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("firebase", "postComments:onCancelled", databaseError.toException())
//            Toast.makeText(
//                context, "Failed to load comments.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
            // ...
        }
    }

    fun startListening(vkId: String){
        database.child(vkId).addChildEventListener(childEventListener)
    }

    fun getReactions(vkId: String, getReactionsCallback: Any.() -> Unit) {
        database.child("reaction")
            .equalTo(vkId)
            .get()
            .addOnSuccessListener {
                getReactionsCallback(it.value!!)
                it.
                Log.d("firebase", "Got value ${it.value}")
            }.addOnFailureListener {
                Log.d("firebase", "Error getting data", it)
            }
    }

}