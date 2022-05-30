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
import com.example.soundtoshare.repositories.Reaction
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer
import java.util.Date
import java.util.concurrent.TimeUnit

class RecyclerAdapterReactions(private val reactions: MutableList<Reaction>) : RecyclerView
.Adapter<RecyclerAdapterReactions.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTextView: TextView = itemView.findViewById(R.id.textViewSong)
        val artistTextView: TextView = itemView.findViewById(R.id.textViewArtist)
        val userTextView: TextView = itemView.findViewById(R.id.textViewUser)
        val reactionTextView: TextView = itemView.findViewById(R.id.textViewReaction)
        val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
        val reactionAvatar: ImageView = itemView.findViewById(R.id.reactionAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recyclerview_reaction_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.songTextView.text = reactions[position].song
        holder.artistTextView.text = reactions[position].artist
        holder.userTextView.text = reactions[position].from
        holder.reactionTextView.text = "liked"
        holder.timeTextView.text = getTimeOfReaction(reactions[position].time)
        val options = DisplayImageOptions.Builder().displayer(
            RoundedBitmapDisplayer(cornerRadiusPixel)
        ).build()
        val imageLoader = ImageLoader.getInstance()
        imageLoader.init(ImageLoaderConfiguration.createDefault(holder.timeTextView.context))
        imageLoader.displayImage(reactions[position].avatar, holder.reactionAvatar, options)

        holder.itemView.setOnClickListener {
            holder.reactionAvatar.context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://vk.com/id" + reactions[position].fromId)
                )
            )
        }
    }

    override fun getItemCount() = reactions.size

    private fun getTimeOfReaction(date: Long): String {
        lateinit var timeOfReaction: String
        val dateDiff = Date().time - date

        val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
        val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
        val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
        val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)

        when {
            second < secondsInMinute -> {
                timeOfReaction = "Not long ago"
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

        const val cornerRadiusPixel = 60
    }
}
