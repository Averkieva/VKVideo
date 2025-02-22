package com.example.feature_video_list.domain.model

data class VideoItem(
    val title: String,
    val imageUrl: String,
    val duration: String,
    val author: String,
    val viewCount: String,
    val publishedDate: String
)
