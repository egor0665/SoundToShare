package com.example.soundtoshare.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.soundtoshare.R
import com.example.soundtoshare.repositories.Reaction

class CustomRecyclerAdapter(private val reactions: MutableList<Reaction>) : RecyclerView
.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTextView: TextView = itemView.findViewById(R.id.textViewSong)
        val artistTextView: TextView = itemView.findViewById(R.id.textViewArtist)
        val userTextView: TextView = itemView.findViewById(R.id.textViewUser)
        val reactionTextView: TextView = itemView.findViewById(R.id.textViewReaction)
        val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.songTextView.text = reactions[position].song
        holder.artistTextView.text = reactions[position].artist
        holder.userTextView.text = reactions[position].from
        holder.reactionTextView.text = reactions[position].reaction
        holder.timeTextView.text = reactions[position].time
    }

    override fun getItemCount() = reactions.size
}