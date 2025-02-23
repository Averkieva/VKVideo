package com.example.feature_video_list.domain.model

data class VideoItem(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: Int,
    val authorName: String,
    val authorUrl: String
)
