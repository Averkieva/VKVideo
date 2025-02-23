package com.example.feature_video_list.data.api

import com.example.feature_video_list.data.model.PexelsVideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {

    @GET("videos/popular")
    suspend fun getPopularVideos(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): Response<PexelsVideoResponse>

    @GET("videos/search")
    suspend fun searchVideos(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 10,
        @Query("page") page: Int = 1
    ): Response<PexelsVideoResponse>
}