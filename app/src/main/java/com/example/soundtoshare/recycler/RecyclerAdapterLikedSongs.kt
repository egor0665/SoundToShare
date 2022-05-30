package com.example.soundtoshare.recycler

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soundtoshare.R
import com.example.soundtoshare.repositories.roomdb.LikedSong
import java.util.Date
import java.util.concurrent.TimeUnit

class RecyclerAdapterLikedSongs(private val likedSongs: MutableList<LikedSong>) : RecyclerView
.Adapter<RecyclerAdapterLikedSongs.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTextView: TextView = itemView.findViewById(R.id.textViewSong)
        val artistTextView: TextView = itemView.findViewById(R.id.textViewArtist)
        val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
        val playImageView: ImageView = itemView.findViewById(R.id.playImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recyclerview_likedsong_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.songTextView.text = likedSongs[position].song
        holder.artistTextView.text = likedSongs[position].artist
        holder.timeTextView.text = getTimeOfReaction(likedSongs[position].time!!)
        holder.itemView.setOnClickListener {
            holder.playImageView.context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://m.vk.com/audio?q=" +
                            likedSongs[position].song + "-" +
                            likedSongs[position].artist
                    )
                )
            )
        }
        // ЗАПРОС ДЛЯ ПОИСКА МУЗЫКИ: https://m.vk.com/audio?q=МАЛИНОВАЯ%20ЛАДА
    }

    override fun getItemCount() = likedSongs.size
    private fun getTimeOfReaction(date: Long): String {
        lateinit var timeOfReaction: String
        val dateDiff = Date().time - date

        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

        when {
            second < secondsInMinute -> {
                timeOfReaction = "$second Seconds ago"
            }
            minute < minutesInHour -> {
                timeOfReaction = "$minute Minutes ago"
            }
            hour < hoursInDay -> {
                timeOfReaction = "$hour Hours ago"
            }
            day >= daysInWeek -> {
                timeOfReaction = when {
                    day > daysInYear -> {
                        (day / daysInYear).toString() + " Years ago"
                    }
                    day > daysInMonth -> {
                        (day / daysInMonth).toString() + " Months ago"
                    }
                    else -> {
                        (day / daysInWeek).toString() + " Week ago"
                    }
                }
            }
            day < daysInWeek -> {
                timeOfReaction = "$day Days ago"
            }
        }
        return timeOfReaction
    }

    companion object {
        const val daysInYear = 365
        const val daysInMonth = 30
        const val daysInWeek = 7
        const val secondsInMinute = 60
        const val minutesInHour = 60
        const val hoursInDay = 60
    }
}
