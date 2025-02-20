package com.example.feature_video_list.data

data class YouTubeSearchVideo(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String
)

data class YouTubePopularVideo(
    val id: String,
    val snippet: Snippet,
    val contentDetails: ContentDetails,
    val statistics: Statistics
)

data class ContentDetails(
    val duration: String
)

data class Statistics(
    val viewCount: String
)

data class Snippet(
    val title: String,
    val publishedAt: String,
    val thumbnails: Thumbnails
)

data class YouTubeResponse<T>(
    val items: List<T>
)

data class Thumbnails(
    val medium: Thumbnail?,
    val high: Thumbnail?,
    val standard: Thumbnail?,
    val maxres: Thumbnail?
)

data class Thumbnail(
    val url: String
)