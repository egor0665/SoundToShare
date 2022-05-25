package com.example.soundtoshare.recycler_view

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
import java.util.*
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
                        "https://m.vk.com/audio?q=" + likedSongs[position].song + "-" + likedSongs[position].artist
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
            second < 60 -> {
                timeOfReaction = "$second Seconds ago"
            }
            minute < 60 -> {
                timeOfReaction = "$minute Minutes ago"
            }
            hour < 24 -> {
                timeOfReaction = "$hour Hours ago"
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
