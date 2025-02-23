package com.example.feature_video_list.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feature_video_list.domain.model.VideoItem

@Entity(tableName = "videos")
data class VideoItemEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authorName: String,
    val duration: Int,
    val videoUrl: String,
    val thumbnailUrl: String
)

fun VideoItemEntity.toVideoItem() = VideoItem(
    id = id,
    title = title,
    authorName = authorName,
    duration = duration,
    videoUrl = videoUrl,
    thumbnailUrl = thumbnailUrl
)

fun VideoItem.toVideoItemEntity() = VideoItemEntity(
    id = id,
    title = title,
    authorName = authorName,
    duration = duration,
    videoUrl = videoUrl,
    thumbnailUrl = thumbnailUrl
)
