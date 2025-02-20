package com.example.feature_video_list.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("videos")
    fun getPopularVideos(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 10,
        @Query("regionCode") regionCode: String = "RU",
        @Query("key") apiKey: String
    ): Call<YouTubeResponse<YouTubePopularVideo>>

    @GET("search")
    fun getVideos(
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 10,
        @Query("q") query: String,
        @Query("type") type: String = "video",
        @Query("key") apiKey: String
    ): Call<YouTubeResponse<YouTubeSearchVideo>>

    @GET("videos")
    fun getVideoDetails(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("id") videoIds: String,
        @Query("key") apiKey: String
    ): Call<YouTubeResponse<YouTubePopularVideo>>

}

