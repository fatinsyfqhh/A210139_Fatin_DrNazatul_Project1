package com.example.a210139_fatin_drnazatul_project1

import androidx.annotation.DrawableRes

// mainactivity.kt
data class FoodItem(
    val title: String,
    val distance: String,
    val user: String,
    val location: String,
    val imageRes: Int
)

// resqbiteviewmodel.kt
data class CommunityPost(
    val id: Int,
    val userName: String,
    val action: String,
    val time: String,
    val likes: Int,
    val isLiked: Boolean = false
)

// messages.kt
data class ChatPreview(
    val name: String,
    val lastMsg: String,
    val time: String,
    val isUnread: Boolean = false
)