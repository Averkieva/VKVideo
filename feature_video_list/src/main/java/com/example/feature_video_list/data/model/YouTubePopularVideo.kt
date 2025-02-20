package com.example.feature_video_list.data.model

data class YouTubePopularVideo(
    val id: String,
    val snippet: Snippet,
    val contentDetails: ContentDetails,
    val statistics: Statistics
)