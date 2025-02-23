package com.example.feature_video_list.data.repository

import com.example.feature_video_list.data.api.PexelsApiService
import com.example.feature_video_list.data.mapper.toVideoItem
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.domain.repository.VideoRepository

class VideoRepositoryImpl(
    private val apiService: PexelsApiService,
    private val apiKey: String
) : VideoRepository {

    override suspend fun getPopularVideos(page: Int): List<VideoItem> {
        val response = apiService.getPopularVideos(apiKey = apiKey, page = page)
        if (response.isSuccessful) {
            return response.body()?.videos?.map { video ->
                video.toVideoItem()
            } ?: emptyList()
        } else {
            throw Exception("Ошибка API: ${response.errorBody()?.string()}")
        }
    }

    override suspend fun searchVideos(query: String, page: Int): List<VideoItem> {
        val searchResponse = apiService.searchVideos(query = query, apiKey = apiKey, page = page)
        if (searchResponse.isSuccessful) {
            return searchResponse.body()?.videos?.map { video ->
                video.toVideoItem()
            } ?: emptyList()
        }
        throw Exception("Ошибка поиска видео")
    }
}