package com.example.soundtoshare.fragments.home

import android.util.Log
import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.Reaction
import java.util.*
import java.util.concurrent.TimeUnit.*

class FireBaseGetDataUseCase {
    private val firebaseDatabase = FireBaseDatabase()

    fun getReactions(vkId: String, getReactionsCallback: Reaction?.() -> Unit) {
        firebaseDatabase.startListening(vkId){
            if (!this.second && this.first.value == null) {
                getReactionsCallback(null)
            }
            else if (this.second) {
                val reaction = Reaction(
                    this.first.child("from").value.toString(),
                    this.first.child("from_id").value.toString(),
                    this.first.child("time").value.toString().toLong(),
                    this.first.child("song").value.toString(),
                    this.first.child("artist").value.toString(),
                    this.first.child("avatar").value.toString()
                )
                getReactionsCallback(reaction)
            }
        }
    }



}
