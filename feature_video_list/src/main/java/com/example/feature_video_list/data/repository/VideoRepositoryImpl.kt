package com.example.feature_video_list.data.repository

import android.util.Log
import com.example.feature_video_list.data.api.PexelsApiService
import com.example.feature_video_list.data.local.dao.VideoDao
import com.example.feature_video_list.data.local.entity.toVideoItem
import com.example.feature_video_list.data.local.entity.toVideoItemEntity
import com.example.feature_video_list.data.mapper.toVideoItem
import com.example.feature_video_list.domain.model.VideoItem
import com.example.feature_video_list.domain.repository.VideoRepository
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

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
                Log.e(
                    TAG,
                    String.format(ERROR_SERVER, response.code())
                )
                throw IOException(String.format(ERROR_SERVER, response.code()))
            }
        } catch (e: UnknownHostException) {
            Log.e(
                TAG,
                String.format(LOG_NO_INTERNET, e.localizedMessage)
            )
            val cachedVideos = loadVideosFromCache()
            if (cachedVideos.isNotEmpty()) {
                return cachedVideos
            } else {
                throw IOException(ERROR_NO_INTERNET)
            }
        } catch (e: HttpException) {
            Log.e(
                TAG,
                String.format(LOG_SERVER_ERROR, e.message())
            )
            throw IOException(ERROR_SERVER_RETRY)
        } catch (e: Exception) {
            Log.e(
                TAG,
                String.format(LOG_UNKNOWN_ERROR, e.localizedMessage)
            )
            throw IOException(ERROR_UNKNOWN)
        }
    }

    override suspend fun searchVideos(query: String, page: Int): List<VideoItem> {
        return try {
            val response = apiService.searchVideos(query = query, apiKey = apiKey, page = page)
            if (response.isSuccessful) {
                val videos = response.body()?.videos?.map { it.toVideoItem() } ?: emptyList()
                cacheVideos(videos)
                videos
            } else {
                Log.e(
                    TAG,
                    String.format(ERROR_SERVER, response.code())
                )
                loadVideosFromCache()
            }
        } catch (e: UnknownHostException) {
            Log.e(
                TAG,
                String.format(LOG_NO_INTERNET, e.localizedMessage)
            )
            loadVideosFromCache()
        } catch (e: HttpException) {
            Log.e(
                TAG,
                String.format(LOG_SERVER_ERROR, e.message())
            )
            loadVideosFromCache()
        } catch (e: Exception) {
            Log.e(
                TAG,
                String.format(LOG_UNKNOWN_ERROR, e.localizedMessage)
            )
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

    companion object {
        const val TAG = "VideoRepository"
        const val LOG_NO_INTERNET = "Отсутствует подключение к интернету: %s"
        const val LOG_SERVER_ERROR = "Ошибка сервера: %s"
        const val LOG_UNKNOWN_ERROR = "Неизвестная ошибка: %s"

        const val ERROR_SERVER = "Ошибка сервера: %d"
        const val ERROR_NO_INTERNET = "Нет подключения к интернету"
        const val ERROR_SERVER_RETRY = "Ошибка сервера. Попробуйте позже"
        const val ERROR_UNKNOWN = "Произошла ошибка. Попробуйте снова"
    }
}