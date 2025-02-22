package com.example.feature_video_list.data.repository

import com.example.feature_video_list.data.YouTubeApiService
import com.example.feature_video_list.data.mapper.toVideoItem
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.domain.repository.VideoRepository

class VideoRepositoryImpl(
    private val apiService: YouTubeApiService,
    private val apiKey: String
) : VideoRepository {

    override suspend fun getPopularVideos(): List<VideoItem> {
        val response = apiService.getPopularVideos(apiKey = apiKey)
        if (response.isSuccessful) {
            return response.body()?.items?.map { video ->
                video.toVideoItem()
            } ?: emptyList()
        } else {
            throw Exception("Ошибка API: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun searchVideos(query: String): List<VideoItem> {
        val searchResponse = apiService.getVideos(query = query, apiKey = apiKey)
        if (searchResponse.isSuccessful) {
            val videoIds = searchResponse.body()?.items?.map { it.id.videoId }?.joinToString(",") ?: ""
            if (videoIds.isNotEmpty()) {
                val detailsResponse = apiService.getVideoDetails(videoIds = videoIds, apiKey = apiKey)
                if (detailsResponse.isSuccessful) {
                    return detailsResponse.body()?.items?.map { video ->
                        video.toVideoItem()
                    } ?: emptyList()
                }
            }
        }
        throw Exception("Ошибка поиска видео")
    }
}

