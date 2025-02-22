package com.example.feature_video_list.data.mapper

import com.example.feature_video_list.data.model.YouTubePopularVideo
import com.example.feature_video_list.data.utils.formatDuration
import com.example.feature_video_list.data.utils.formatRelativeDate
import com.example.feature_video_list.data.utils.formatViewCount
import com.example.feature_video_list.domain.model.VideoItem

fun YouTubePopularVideo.toVideoItem(): VideoItem {
    val thumbnailUrl = snippet.thumbnails.maxres?.url
        ?: snippet.thumbnails.standard?.url
        ?: snippet.thumbnails.high?.url
        ?: snippet.thumbnails.medium?.url ?: EMPTY_URL

    return VideoItem(
        title = snippet.title,
        imageUrl = thumbnailUrl,
        duration = formatDuration(contentDetails.duration),
        author = snippet.channelTitle,
        viewCount = formatViewCount(statistics.viewCount),
        publishedDate = formatRelativeDate(snippet.publishedAt)
    )
}

private const val EMPTY_URL = ""
