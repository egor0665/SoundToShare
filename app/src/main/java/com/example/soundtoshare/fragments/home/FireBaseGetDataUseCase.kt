package com.example.soundtoshare.fragments.home

import com.example.soundtoshare.external.FireBaseDatabase
import com.example.soundtoshare.repositories.Reaction
import java.util.*
import java.util.concurrent.TimeUnit.*

class FireBaseGetDataUseCase {
    private val firebaseDatabase = FireBaseDatabase()

    fun getReactions(vkId: String, getReactionsCallback: Reaction.() -> Unit) {
        firebaseDatabase.startListening(vkId){
            val time = getTimeOfReaction(this.child("time").value.toString().toLong())

            val reaction = Reaction(
                this.child("from").value.toString(),
                this.child("from_id").value.toString(),
                time,
                this.child("song").value.toString(),
                this.child("artist").value.toString(),
                this.child("avatar").value.toString()
            )

            getReactionsCallback(reaction)
        }
    }

    private fun getTimeOfReaction(date : Long) : String {
        lateinit var timeOfReaction : String
        val dateDiff = Date().time - date

        val second: Long = MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = MILLISECONDS.toHours(dateDiff)
        val day: Long = MILLISECONDS.toDays(dateDiff)

        when {
            second < 60 -> {
                timeOfReaction = "$second Seconds ago"
            }
            minute < 60 -> {
                timeOfReaction  = "$minute Minutes ago"
            }
            hour < 24 -> {
                timeOfReaction  = "$hour Hours ago"
            }
            day >= 7 -> {
                timeOfReaction = when {
                    day > 365 -> {
                        (day / 365).toString() + " Years ago"
                    }
                    day > 30 -> {
                        (day / 30).toString() + " Months ago"
                    }
                    else -> {
                        (day / 7).toString() + " Week ago"
                    }
                }
            }
            day < 7 -> {
                timeOfReaction = "$day Days ago"
            }
        }
        return timeOfReaction
    }

}
