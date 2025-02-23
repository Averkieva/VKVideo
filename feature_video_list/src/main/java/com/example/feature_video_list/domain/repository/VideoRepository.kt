package com.example.feature_video_list.domain.repository

import com.example.feature_video_list.domain.model.VideoItem

interface VideoRepository {
    suspend fun getPopularVideos(page: Int = 1): List<VideoItem>
    suspend fun searchVideos(query: String, page: Int = 1): List<VideoItem>
}
