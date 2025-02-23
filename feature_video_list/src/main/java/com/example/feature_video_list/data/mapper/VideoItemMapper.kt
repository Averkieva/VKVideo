package com.example.feature_video_list.data.mapper

import com.example.feature_video_list.data.model.PexelsVideo
import com.example.feature_video_list.domain.model.VideoItem

fun PexelsVideo.toVideoItem(): VideoItem {
    return VideoItem(
        id = id.toString(),
        title = "Видео от ${user.name}",
        thumbnailUrl = image,
        videoUrl = video_files.firstOrNull { it.quality == "hd" }?.link ?: url,
        duration = duration,
        authorName = user.name,
        authorUrl = user.url
    )
}