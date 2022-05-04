package com.example.soundtoshare.repositories

import com.google.type.DateTime
import java.sql.Time

data class Reaction(val from: String, val fromId: String, val time: String, val song: String, val artist: String, val reaction: String) {
}
