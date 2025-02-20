package com.example.feature_video_list.domain.repository

import com.example.feature_video_list.domain.model.VideoItem

interface VideoRepository {
    suspend fun getPopularVideos(): List<VideoItem>
    suspend fun searchVideos(query: String): List<VideoItem>
}
