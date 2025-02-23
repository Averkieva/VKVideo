package com.example.feature_video_list.data.repository

import com.example.feature_video_list.data.local.dao.VideoDao
import com.example.feature_video_list.data.api.PexelsApiService
import com.example.feature_video_list.data.mapper.toVideoItem
import com.example.feature_video_list.data.local.entity.toVideoItem
import com.example.feature_video_list.data.local.entity.toVideoItemEntity
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.domain.repository.VideoRepository

class VideoRepositoryImpl(
    private val apiService: PexelsApiService,
    private val apiKey: String,
    private val videoDao: VideoDao
) : VideoRepository {

    override suspend fun getPopularVideos(page: Int): List<VideoItem> {
        return try {
            val response = apiService.getPopularVideos(apiKey = apiKey, page = page)
            if (response.isSuccessful) {
                val videos = response.body()?.videos?.map { it.toVideoItem() } ?: emptyList()
                cacheVideos(videos)
                videos
            } else {
                loadVideosFromCache()
            }
        } catch (e: Exception) {
            loadVideosFromCache()
        }
    }

    override suspend fun searchVideos(query: String, page: Int): List<VideoItem> {
        return try {
            val searchResponse = apiService.searchVideos(query = query, apiKey = apiKey, page = page)
            if (searchResponse.isSuccessful) {
                val videos = searchResponse.body()?.videos?.map { it.toVideoItem() } ?: emptyList()
                cacheVideos(videos)
                videos
            } else {
                loadVideosFromCache()
            }
        } catch (e: Exception) {
            loadVideosFromCache()
        }
    }

    private suspend fun cacheVideos(videos: List<VideoItem>) {
        videoDao.clearVideos()
        val entities = videos.map { it.toVideoItemEntity() }
        videoDao.insertVideos(entities)
    }

    private suspend fun loadVideosFromCache(): List<VideoItem> {
        return videoDao.getAllVideos().map { it.toVideoItem() }
    }
}