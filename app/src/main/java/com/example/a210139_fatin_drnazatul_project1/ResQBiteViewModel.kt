package com.example.a210139_fatin_drnazatul_project1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResQBiteViewModel : ViewModel() {
    // 1. public feed
    private val _publicFoodList = MutableStateFlow(listOf(
        FoodItem("Extra Croissants", "1.2km", "NearBakery", "Bangi", R.drawable.croissant),
        FoodItem("Tin tuna", "0.8km", "Mira", "KIY", R.drawable.canned_tuna),
        FoodItem("Roti Gardenia Pandan", "1.0km", "Amar", "KPZ", R.drawable.roti),
        FoodItem("Nasi Ayam", "0.3km", "Qila", "KUO", R.drawable.nasiayam)
    ))
    val publicFoodList: StateFlow<List<FoodItem>> = _publicFoodList.asStateFlow()

    // 2. user contribution
    private val _myContributions = MutableStateFlow(listOf<FoodItem>())
    val myContributions: StateFlow<List<FoodItem>> = _myContributions.asStateFlow()

    fun addMyFoodItem(newItem: FoodItem) {
        _myContributions.value = _myContributions.value + newItem
    }

    fun getIconForCategory(category: String): Int {
        return when (category) {
            "Bakery" -> R.drawable.croissant
            "Canned" -> R.drawable.canned_tuna
            "Packed Meals" -> R.drawable.packedmeals
            "Fruits" -> R.drawable.fruits
            "Drinks" -> R.drawable.drinks
            else -> R.drawable.resqlogo
        }
    }
    private val _communityPosts = MutableStateFlow(listOf(
        CommunityPost(1, "Mark", "saved 2 boxes of Donuts", "5m ago", 12),
        CommunityPost(2, "Fatin", "shared a tray of Muffins", "15m ago", 24),
        CommunityPost(3, "Rose", "collected Packed Meals", "1h ago", 8),
        CommunityPost(4, "Sarah", "donated 5 packs of Fried Rice", "3h ago", 45)
    ))
    val communityPosts: StateFlow<List<CommunityPost>> = _communityPosts.asStateFlow()

    fun toggleLike(postId: Int) {
        _communityPosts.value = _communityPosts.value.map { post ->
            if (post.id == postId) {
                if (post.isLiked) post.copy(likes = post.likes - 1, isLiked = false)
                else post.copy(likes = post.likes + 1, isLiked = true)
            } else post
        }
    }

    fun addStory(userName: String, action: String) {
        val newPost = CommunityPost(
            id = (_communityPosts.value.maxOfOrNull { it.id } ?: 0) + 1,
            userName = userName,
            action = action,
            time = "Just now",
            likes = 0
        )
        _communityPosts.value = listOf(newPost) + _communityPosts.value
    }
}