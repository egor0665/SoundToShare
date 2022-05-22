package com.example.soundtoshare.fragments.home

import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.Reaction
import java.util.*
import java.util.concurrent.TimeUnit.*

class FireBaseGetDataUseCase {
    private val firebaseDatabase = FireBaseDatabase()

    fun getReactions(vkId: String, getReactionsCallback: Reaction.() -> Unit) {
        firebaseDatabase.startListening(vkId){


            val reaction = Reaction(
                this.child("from").value.toString(),
                this.child("from_id").value.toString(),
                this.child("time").value.toString().toLong(),
                this.child("song").value.toString(),
                this.child("artist").value.toString(),
                this.child("avatar").value.toString()
            )

            getReactionsCallback(reaction)
        }
    }



}
