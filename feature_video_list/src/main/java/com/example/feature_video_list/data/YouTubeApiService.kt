package com.example.feature_video_list.data

import com.example.feature_video_list.data.model.YouTubePopularVideo
import com.example.feature_video_list.data.model.YouTubeResponse
import com.example.feature_video_list.data.model.YouTubeSearchVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    @GET("videos")
    suspend fun getPopularVideos(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 50,
        @Query("regionCode") regionCode: String = "RU",
        @Query("key") apiKey: String
    ): Response<YouTubeResponse<YouTubePopularVideo>>

    @GET("search")
    suspend fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 50,
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("key") apiKey: String
    ): Response<YouTubeResponse<YouTubeSearchVideo>>

    @GET("videos")
    suspend fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("id") videoIds: String,
        @Query("key") apiKey: String
    ): Response<YouTubeResponse<YouTubePopularVideo>>
}

