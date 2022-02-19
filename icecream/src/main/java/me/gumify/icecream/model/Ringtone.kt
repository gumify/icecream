package me.gumify.icecream.model

data class Ringtone(
    val id: String,
    val imageUrl: String,
    val licensed: Boolean,
    val title: String,
    val audioUrl: String,
    val gradientStart: String,
    val gradientEnd: String
)
