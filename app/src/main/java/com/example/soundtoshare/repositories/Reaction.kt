package com.example.soundtoshare.repositories

import java.sql.Time

data class Reaction(val from: String, val to: String, val fromId: Int, val toId: Int, val time: Time, val song: String, val artist: String) {
}
