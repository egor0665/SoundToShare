package com.example.soundtoshare.repositories

data class Reaction(
    val from: String,
    val fromId: String,
    val time: Long,
    val song: String,
    val artist: String,
    val avatar: String
)
