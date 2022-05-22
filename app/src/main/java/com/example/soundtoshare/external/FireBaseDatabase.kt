package com.example.soundtoshare.external

import android.util.Log
import com.example.soundtoshare.repositories.User
import com.example.soundtoshare.repositories.UserInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


class FireBaseDatabase {
    private var lastLike = LastLike("", Date().time)
    private val database = Firebase.database.reference

    fun startListening(vkId: String, listeningCallback: DataSnapshot.() -> Unit ){
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildAdded:" + dataSnapshot.key!!)
                listeningCallback(dataSnapshot)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("firebase", "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("firebase", "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("firebase", "postComments:onCancelled", databaseError.toException())
            }
        }
        database.child("reaction").child(vkId).addChildEventListener(childEventListener)
    }

//    fun getReactions(vkId: String, getReactionsCallback: DataSnapshot.() -> Unit) {
//        Log.d("firebase", "vk id:"+vkId)
//        database.child("reaction")
//            .child(vkId)
//            .get()
//            .addOnSuccessListener {
//                getReactionsCallback(it)
//                Log.d("firebase", "Got value ${it.value}")
//            }.addOnFailureListener {
//                Log.d("firebase", "Error getting data", it)
//            }
//    }

    fun likeSong(toUser: User, fromUser: UserInfo) {
        Log.d("reaction", "newreaction2")
        val reaction = hashMapOf(
            "from" to fromUser.firstName,
            "from_id" to fromUser.id,
            "artist" to toUser.artist,
            "reaction" to 1,
            "song" to toUser.song,
            "time" to Date().time,
            "avatar" to fromUser.avatar_uri
        )

        database.child("reaction").child(toUser.VKAccountID).push().setValue(reaction);
    }

}
data class LastLike(val toId: String, val time: Long)