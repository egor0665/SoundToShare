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
            holder.timeTextView.text = likedSongs[position].time
            holder.itemView.setOnClickListener{
                holder.playImageView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://m.vk.com/audio?q="+likedSongs[position].song+"-"+likedSongs[position].artist)))
            }
            // ЗАПРОС ДЛЯ ПОИСКА МУЗЫКИ: https://m.vk.com/audio?q=МАЛИНОВАЯ%20ЛАДА
        }

        override fun getItemCount() = likedSongs.size
    }