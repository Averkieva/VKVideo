package com.example.feature_video_list.data.model

data class PexelsVideoFile(
    val id: Int,
    val quality: String,
    val file_type: String,
    val width: Int?,
    val height: Int?,
    val link: String
)