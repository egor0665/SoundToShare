package com.example.soundtoshare.fragments.home

import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.Reaction
import java.text.SimpleDateFormat
import java.util.*

class FireBaseGetDataUseCase {
    private val firebaseDatabase = FireBaseDatabase()

    fun getReactions(vkId: String, getReactionsCallback: Reaction.() -> Unit) {
        firebaseDatabase.startListening(vkId){
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")

            val formattedDate =
                    formatter.format(parser.parse(this.child("time").value.toString()))
            val reaction = Reaction(
                this.child("from").value.toString(),
                this.child("from_id").value.toString(),
                formattedDate,
                this.child("song").value.toString(),
                this.child("artist").value.toString(),
                getReaction(this.child("reaction").value.toString().toInt()),
                this.child("avatar").value.toString()
            )

            getReactionsCallback(reaction)
        }
    }

    private fun getReaction(num: Int): String{
        return when (num){
            1 -> "liked"
            2 -> "lamed"
            else -> "enjoyed"
        }
    }
}
